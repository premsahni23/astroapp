import { useState, useEffect, useCallback } from 'react';
import { Alert } from 'react-native';
import { signalingService } from '../services/SignalingService';

// MediaStream type until react-native-webrtc is installed
type MediaStream = any;

export interface CallState {
  isInCall: boolean;
  isConnected: boolean;
  connectionState: string;
  callDuration: number;
  isMuted: boolean;
  isVideoEnabled: boolean;
  localStream: MediaStream | null;
  remoteStream: MediaStream | null;
}

export interface UseVideoCallProps {
  roomId?: string;
  callerId?: string;
  calleeId?: string;
  isVideo?: boolean;
  onCallEnd?: () => void;
  onCallStart?: () => void;
  onError?: (error: string) => void;
}

export const useVideoCall = ({
  roomId,
  callerId,
  calleeId,
  isVideo = true,
  onCallEnd,
  onCallStart,
  onError,
}: UseVideoCallProps = {}) => {
  const [callState, setCallState] = useState<CallState>({
    isInCall: false,
    isConnected: false,
    connectionState: 'new',
    callDuration: 0,
    isMuted: false,
    isVideoEnabled: isVideo,
    localStream: null,
    remoteStream: null,
  });

  const [callStartTime, setCallStartTime] = useState<Date | null>(null);

  // Initialize call
  const initializeCall = useCallback(async (
    currentRoomId: string,
    isCaller: boolean = true
  ) => {
    try {
      setCallState(prev => ({ ...prev, isInCall: true, connectionState: 'connecting' }));

      // Get user media
      const stream = await signalingService.getUserMedia(isVideo);
      const remoteStreamInstance = null; // Remote stream will be set when connection is established

      setCallState(prev => ({
        ...prev,
        localStream: stream,
        remoteStream: remoteStreamInstance,
      }));

      // Setup callbacks
      signalingService.onAddRemoteStream = (remoteStream: MediaStream) => {
        console.log('Remote stream received');
        setCallState(prev => ({
          ...prev,
          remoteStream,
          isConnected: true,
          connectionState: 'connected',
        }));
        setCallStartTime(new Date());
        onCallStart?.();
      };

      signalingService.onAddConnectionStream = (state) => {
        console.log('Connection state changed:', state);
        setCallState(prev => ({
          ...prev,
          connectionState: state,
          isConnected: state === 'connected' || state === 'completed',
        }));

        if (state === 'failed' || state === 'disconnected') {
          Alert.alert('Connection Lost', 'The call has been disconnected');
          endCall();
        }
      };

      // Create call entry
      if (callerId && calleeId) {
        await signalingService.createCallEntry(
          currentRoomId,
          callerId,
          calleeId,
          isVideo ? 'video' : 'audio'
        );
      }

      // Join or create room
      if (isCaller) {
        await signalingService.createRoom(currentRoomId, stream, remoteStreamInstance, isVideo);
      } else {
        await signalingService.joinRoom(currentRoomId, stream, remoteStreamInstance, isVideo);
      }

      console.log('Call initialized successfully');
    } catch (error) {
      console.error('Error initializing call:', error);
      onError?.('Failed to initialize call');
      setCallState(prev => ({ ...prev, isInCall: false }));
    }
  }, [isVideo, callerId, calleeId, onCallStart, onError]);

  // End call
  const endCall = useCallback(async () => {
    try {
      if (callState.localStream && callState.remoteStream && roomId) {
        const endTime = new Date();
        const duration = callStartTime 
          ? Math.floor((endTime.getTime() - callStartTime.getTime()) / 1000)
          : 0;

        await signalingService.hangUp(
          roomId,
          callState.localStream,
          callState.remoteStream,
          isVideo,
          endTime,
          duration.toString()
        );
      }

      // Reset state
      setCallState({
        isInCall: false,
        isConnected: false,
        connectionState: 'new',
        callDuration: 0,
        isMuted: false,
        isVideoEnabled: isVideo,
        localStream: null,
        remoteStream: null,
      });

      setCallStartTime(null);
      signalingService.dispose();
      onCallEnd?.();
    } catch (error) {
      console.error('Error ending call:', error);
      onError?.('Failed to end call properly');
    }
  }, [callState.localStream, callState.remoteStream, roomId, isVideo, callStartTime, onCallEnd, onError]);

  // Toggle mute
  const toggleMute = useCallback(async () => {
    const newMutedState = !callState.isMuted;
    setCallState(prev => ({ ...prev, isMuted: newMutedState }));
    signalingService.toggleAudio(!newMutedState);

    // Update media status in database
    if (roomId && callerId && calleeId) {
      await signalingService.updateMediaStatusInCall(
        roomId,
        'caller', // This should be determined based on user role
        undefined,
        !newMutedState,
        callerId,
        calleeId
      );
    }
  }, [callState.isMuted, roomId, callerId, calleeId]);

  // Toggle video
  const toggleVideo = useCallback(async () => {
    const newVideoState = !callState.isVideoEnabled;
    setCallState(prev => ({ ...prev, isVideoEnabled: newVideoState }));
    signalingService.toggleVideo(newVideoState);

    // Update media status in database
    if (roomId && callerId && calleeId) {
      await signalingService.updateMediaStatusInCall(
        roomId,
        'caller', // This should be determined based on user role
        newVideoState,
        undefined,
        callerId,
        calleeId
      );
    }
  }, [callState.isVideoEnabled, roomId, callerId, calleeId]);

  // Switch camera
  const switchCamera = useCallback(async () => {
    try {
      await signalingService.switchCamera();
    } catch (error) {
      console.error('Error switching camera:', error);
      onError?.('Failed to switch camera');
    }
  }, [onError]);

  // Update call duration
  useEffect(() => {
    let interval: ReturnType<typeof setInterval> | null = null;

    if (callState.isConnected && callStartTime) {
      interval = setInterval(() => {
        const now = new Date();
        const duration = Math.floor((now.getTime() - callStartTime.getTime()) / 1000);
        setCallState(prev => ({ ...prev, callDuration: duration }));
      }, 1000);
    }

    return () => {
      if (interval) {
        clearInterval(interval);
      }
    };
  }, [callState.isConnected, callStartTime]);

  // Cleanup on unmount
  useEffect(() => {
    return () => {
      if (callState.isInCall) {
        endCall();
      }
    };
  }, []);

  return {
    callState,
    initializeCall,
    endCall,
    toggleMute,
    toggleVideo,
    switchCamera,
  };
};