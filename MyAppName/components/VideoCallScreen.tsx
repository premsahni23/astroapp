import React, { useState, useEffect, useRef } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  Alert,
  Dimensions,
  StatusBar,
  SafeAreaView,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { WalletService, SessionStatus } from '../services/WalletService';
import { ApiService } from '../services/apiService';

const { width, height } = Dimensions.get('window');

// Types until react-native-webrtc is installed
type MediaStream = any;
type RTCIceConnectionState = string;

interface VideoCallScreenProps {
  roomId: string;
  isVideo: boolean;
  isCaller: boolean;
  onEndCall: () => void;
  callerId?: string;
  calleeId?: string;
  sessionType?: string;
  ratePerMinute?: string;
}

export default function VideoCallScreen({
  roomId,
  isVideo,
  isCaller,
  onEndCall,
  callerId,
  calleeId,
  sessionType = 'AUDIO_CALL',
  ratePerMinute = '11',
}: VideoCallScreenProps) {
  const [localStream, setLocalStream] = useState<MediaStream | null>(null);
  const [remoteStream, setRemoteStream] = useState<MediaStream | null>(null);
  const [isConnected, setIsConnected] = useState(false);
  const [isMuted, setIsMuted] = useState(false);
  const [isVideoEnabled, setIsVideoEnabled] = useState(isVideo);
  const [isSpeakerOn, setIsSpeakerOn] = useState(true);
  const [connectionState, setConnectionState] = useState<string>('connecting');
  const [callDuration, setCallDuration] = useState(0);
  const [sessionStatus, setSessionStatus] = useState<SessionStatus | null>(null);
  const [showLowBalanceWarning, setShowLowBalanceWarning] = useState(false);

  const callStartTime = useRef<Date | null>(null);
  const durationInterval = useRef<ReturnType<typeof setInterval> | null>(null);
  const balanceCheckInterval = useRef<ReturnType<typeof setInterval> | null>(null);

  useEffect(() => {
    // Initialize call with billing
    initializeCallWithBilling();

    return () => {
      cleanup();
    };
  }, []);

  const initializeCallWithBilling = async () => {
    try {
      // First test the API connection
      console.log('Testing API connection...');
      const connectionTest = await ApiService.testConnection();
      console.log('Connection test result:', connectionTest);
      
      if (!connectionTest.success) {
        Alert.alert(
          'Connection Error',
          `Cannot connect to server: ${connectionTest.message}\n\nPlease check your network connection.`,
          [{ text: 'OK', onPress: onEndCall }]
        );
        return;
      }

      // Check if user has sufficient balance before starting
      const canStart = await WalletService.canStartSession(
        callerId || '',
        sessionType as 'AUDIO_CALL' | 'VIDEO_CALL' | 'CHAT'
      );

      if (!canStart.canStart) {
        Alert.alert(
          'Insufficient Balance',
          canStart.message,
          [
            { text: 'Add Money', onPress: () => {
              Alert.alert('Add Money', 'Please add money to your wallet from the Call tab and try again.');
              onEndCall();
            }},
            { text: 'Cancel', onPress: onEndCall }
          ]
        );
        return;
      }

      // Create video session using real API
      try {
        console.log('Creating video session with data:', {
          userId: callerId,
          mentorId: calleeId,
          sessionType: sessionType
        });
        
        const sessionResponse = await ApiService.createVideoSession({
          userId: callerId || '',
          mentorId: calleeId || '',
          sessionType: sessionType as 'AUDIO_CALL' | 'VIDEO_CALL'
        });

        console.log('Video session response:', sessionResponse);

        if (sessionResponse.success) {
          console.log('Video session created successfully:', sessionResponse.data);
          
          Alert.alert(
            'Start Session',
            `${sessionType.replace('_', ' ')} - ₹${ratePerMinute}/minute\n\nYour balance: ₹${canStart.balance.toFixed(2)}\nEstimated time: ${Math.floor(canStart.balance / parseFloat(ratePerMinute))} minutes\n\nBilling will start when call connects.`,
            [
              { text: 'Start Session', onPress: startBillingSession },
              { text: 'Cancel', onPress: onEndCall }
            ]
          );
        } else {
          throw new Error(sessionResponse.message || sessionResponse.error || 'Failed to create video session');
        }
      } catch (error: any) {
        console.error('Error creating video session:', error);
        console.error('Error details:', {
          message: error.message,
          stack: error.stack,
          response: error.response
        });
        Alert.alert('Error', `Failed to create video session: ${error.message}`);
        onEndCall();
      }
    } catch (error) {
      console.error('Error initializing call:', error);
      Alert.alert('Error', 'Failed to initialize call. Please check your connection.');
      onEndCall();
    }
  };

  const startBillingSession = async () => {
    if (!callerId || !calleeId) {
      Alert.alert('Error', 'Missing user or mentor information');
      onEndCall();
      return;
    }

    try {
      const status = await WalletService.startSession(
        roomId,
        callerId,
        calleeId,
        sessionType as 'AUDIO_CALL' | 'VIDEO_CALL' | 'CHAT'
      );

      setSessionStatus(status);
      
      if (status.status === 'STARTED') {
        callStartTime.current = new Date();
        startDurationTimer();
        startBalanceMonitoring();
        setIsConnected(true);
        setConnectionState('connected');
      } else {
        Alert.alert('Session Error', status.message);
        onEndCall();
      }
    } catch (error: any) {
      console.error('Billing error:', error);
      Alert.alert('Billing Error', error.message || 'Failed to start billing session. Please check your wallet balance.');
      onEndCall();
    }
  };

  const startDurationTimer = () => {
    durationInterval.current = setInterval(() => {
      if (callStartTime.current) {
        const now = new Date();
        const duration = Math.floor((now.getTime() - callStartTime.current.getTime()) / 1000);
        setCallDuration(duration);
      }
    }, 1000);
  };

  const startBalanceMonitoring = () => {
    // Check balance every 30 seconds
    balanceCheckInterval.current = setInterval(async () => {
      try {
        const status = await WalletService.checkSessionStatus(roomId);
        setSessionStatus(status);
        
        if (status.status === 'LOW_BALANCE') {
          setShowLowBalanceWarning(true);
          
          if (status.estimatedMinutes < 1) {
            Alert.alert(
              'Insufficient Balance',
              'Your balance is too low to continue. The call will end now.',
              [{ text: 'OK', onPress: () => endCallWithBilling('INSUFFICIENT_BALANCE') }]
            );
          }
        } else if (status.status === 'INSUFFICIENT_BALANCE') {
          endCallWithBilling('INSUFFICIENT_BALANCE');
        }
      } catch (error) {
        console.error('Error checking session status:', error);
      }
    }, 30000);
  };

  const formatDuration = (seconds: number): string => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  const toggleMute = async () => {
    setIsMuted(!isMuted);
    // TODO: Implement actual mute functionality when WebRTC is integrated
  };

  const toggleVideo = async () => {
    setIsVideoEnabled(!isVideoEnabled);
    // TODO: Implement actual video toggle when WebRTC is integrated
  };

  const switchCamera = async () => {
    // TODO: Implement camera switching when WebRTC is integrated
  };

  const endCall = async () => {
    endCallWithBilling('NORMAL_END');
  };

  const endCallWithBilling = async (reason: string = 'NORMAL_END') => {
    try {
      if (sessionStatus) {
        // End the video session using real API
        try {
          await ApiService.endVideoSession(roomId);
          console.log('Video session ended successfully');
        } catch (error) {
          console.error('Error ending video session:', error);
        }

        // End billing session
        const endStatus = await WalletService.endSession(roomId, reason);
        
        const message = reason === 'INSUFFICIENT_BALANCE' 
          ? `Call ended due to insufficient balance.\n\nDuration: ${endStatus.durationMinutes || 0} minutes\nCost: ₹${endStatus.totalCost?.toFixed(2) || '0.00'}`
          : `Call ended successfully.\n\nDuration: ${endStatus.durationMinutes || 0} minutes\nCost: ₹${endStatus.totalCost?.toFixed(2) || '0.00'}\nRemaining Balance: ₹${endStatus.currentBalance.toFixed(2)}`;
        
        Alert.alert('Call Summary', message);
      }
    } catch (error) {
      console.error('Error ending billing session:', error);
      Alert.alert('Error', 'Failed to process billing. Please contact support.');
    } finally {
      cleanup();
      onEndCall();
    }
  };

  const cleanup = () => {
    if (durationInterval.current) {
      clearInterval(durationInterval.current);
      durationInterval.current = null;
    }
    if (balanceCheckInterval.current) {
      clearInterval(balanceCheckInterval.current);
      balanceCheckInterval.current = null;
    }
    callStartTime.current = null;
  };

  const dismissLowBalanceWarning = () => {
    setShowLowBalanceWarning(false);
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" backgroundColor="#000" />
      
      {/* Video Call Area */}
      <View style={styles.remoteVideoContainer}>
        <View style={styles.videoContainer}>
          <Ionicons name="videocam-off" size={80} color="#666" />
          <Text style={styles.videoText}>Video Call with Billing</Text>
          <Text style={styles.videoSubtext}>
            {sessionType} - ₹{ratePerMinute}/minute
          </Text>
          <Text style={styles.roomIdText}>Room: {roomId}</Text>
        </View>
        
        {/* Call Info Overlay */}
        <View style={styles.callInfoOverlay}>
          <Text style={styles.connectionStatus}>
            {connectionState === 'connected' ? 'Connected' : 'Connecting...'}
          </Text>
          <Text style={styles.callDuration}>
            {formatDuration(callDuration)}
          </Text>
          {sessionStatus && (
            <View style={styles.billingInfo}>
              <Text style={styles.balanceText}>
                Balance: ₹{sessionStatus.currentBalance.toFixed(2)}
              </Text>
              <Text style={styles.estimatedTimeText}>
                ~{sessionStatus.estimatedMinutes} min remaining
              </Text>
            </View>
          )}
        </View>
      </View>

      {/* Low Balance Warning */}
      {showLowBalanceWarning && sessionStatus && (
        <View style={styles.lowBalanceWarning}>
          <View style={styles.warningContent}>
            <Ionicons name="warning" size={20} color="#f59e0b" />
            <Text style={styles.warningText}>
              Low Balance! ~{sessionStatus.estimatedMinutes} min left
            </Text>
            <TouchableOpacity onPress={dismissLowBalanceWarning}>
              <Ionicons name="close" size={20} color="#666" />
            </TouchableOpacity>
          </View>
        </View>
      )}

      {/* Local Video */}
      <View style={styles.localVideoContainer}>
        <View style={styles.localVideo}>
          <Ionicons name="person" size={24} color="#666" />
        </View>
      </View>

      {/* Control Buttons */}
      <View style={styles.controlsContainer}>
        <View style={styles.controlsRow}>
          {/* Mute Button */}
          <TouchableOpacity
            style={[styles.controlButton, isMuted && styles.controlButtonActive]}
            onPress={toggleMute}
          >
            <Ionicons
              name={isMuted ? 'mic-off' : 'mic'}
              size={24}
              color={isMuted ? '#ff4444' : '#fff'}
            />
          </TouchableOpacity>

          {/* Video Toggle Button */}
          {isVideo && (
            <TouchableOpacity
              style={[styles.controlButton, !isVideoEnabled && styles.controlButtonActive]}
              onPress={toggleVideo}
            >
              <Ionicons
                name={isVideoEnabled ? 'videocam' : 'videocam-off'}
                size={24}
                color={!isVideoEnabled ? '#ff4444' : '#fff'}
              />
            </TouchableOpacity>
          )}

          {/* Camera Switch Button */}
          {isVideo && isVideoEnabled && (
            <TouchableOpacity
              style={styles.controlButton}
              onPress={switchCamera}
            >
              <Ionicons name="camera-reverse" size={24} color="#fff" />
            </TouchableOpacity>
          )}

          {/* Speaker Button */}
          <TouchableOpacity
            style={[styles.controlButton, isSpeakerOn && styles.controlButtonActive]}
            onPress={() => setIsSpeakerOn(!isSpeakerOn)}
          >
            <Ionicons
              name={isSpeakerOn ? 'volume-high' : 'volume-low'}
              size={24}
              color="#fff"
            />
          </TouchableOpacity>

          {/* End Call Button */}
          <TouchableOpacity
            style={styles.endCallButton}
            onPress={endCall}
          >
            <Ionicons name="call" size={24} color="#fff" />
          </TouchableOpacity>
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  remoteVideoContainer: {
    flex: 1,
    backgroundColor: '#1a1a1a',
  },
  videoContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#1a1a1a',
    padding: 20,
  },
  videoText: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
    marginTop: 20,
    textAlign: 'center',
  },
  videoSubtext: {
    color: '#999',
    fontSize: 16,
    marginTop: 10,
    textAlign: 'center',
    lineHeight: 22,
  },
  roomIdText: {
    color: '#666',
    fontSize: 14,
    marginTop: 20,
    fontFamily: 'monospace',
  },
  callInfoOverlay: {
    position: 'absolute',
    top: 50,
    left: 20,
    right: 20,
    alignItems: 'center',
  },
  connectionStatus: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '500',
  },
  callDuration: {
    color: '#fff',
    fontSize: 14,
    marginTop: 5,
    opacity: 0.8,
  },
  billingInfo: {
    backgroundColor: 'rgba(0, 0, 0, 0.7)',
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 12,
    marginTop: 10,
    alignItems: 'center',
  },
  balanceText: {
    color: '#10b981',
    fontSize: 14,
    fontWeight: '600',
  },
  estimatedTimeText: {
    color: '#fff',
    fontSize: 12,
    opacity: 0.8,
    marginTop: 2,
  },
  lowBalanceWarning: {
    position: 'absolute',
    top: 120,
    left: 20,
    right: 20,
    backgroundColor: '#fef3c7',
    borderRadius: 12,
    borderWidth: 1,
    borderColor: '#fde68a',
    zIndex: 1000,
  },
  warningContent: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 12,
  },
  warningText: {
    flex: 1,
    color: '#92400e',
    fontSize: 14,
    fontWeight: '600',
    marginLeft: 8,
  },
  localVideoContainer: {
    position: 'absolute',
    top: 100,
    right: 20,
    width: 120,
    height: 160,
    borderRadius: 12,
    overflow: 'hidden',
    borderWidth: 2,
    borderColor: '#fff',
  },
  localVideo: {
    flex: 1,
    backgroundColor: '#333',
    justifyContent: 'center',
    alignItems: 'center',
  },
  controlsContainer: {
    position: 'absolute',
    bottom: 50,
    left: 0,
    right: 0,
    paddingHorizontal: 20,
  },
  controlsRow: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
  },
  controlButton: {
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: 'rgba(255, 255, 255, 0.2)',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255, 255, 255, 0.3)',
  },
  controlButtonActive: {
    backgroundColor: 'rgba(255, 68, 68, 0.8)',
  },
  endCallButton: {
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: '#ff4444',
    justifyContent: 'center',
    alignItems: 'center',
    transform: [{ rotate: '135deg' }],
  },
});