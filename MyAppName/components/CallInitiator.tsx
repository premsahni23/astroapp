import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  Alert,
  Modal,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { router } from 'expo-router';
import { useUser } from '../contexts/UserContext';
import { WalletService } from '../services/WalletService';
import WalletBalance from './WalletBalance';

interface CallInitiatorProps {
  mentorId: string;
  mentorName: string;
  isAvailable?: boolean;
}

export default function CallInitiator({
  mentorId,
  mentorName,
  isAvailable = true,
}: CallInitiatorProps) {
  const { user } = useUser();
  const [showCallOptions, setShowCallOptions] = useState(false);
  const [walletBalance, setWalletBalance] = useState(0);
  const [checkingBalance, setCheckingBalance] = useState(false);

  useEffect(() => {
    if (user?.id) {
      loadWalletBalance();
    }
  }, [user?.id]);

  const loadWalletBalance = async () => {
    if (!user?.id) return;
    
    try {
      const balance = await WalletService.getBalance(user.id);
      setWalletBalance(balance.balance);
    } catch (error) {
      console.error('Error loading wallet balance:', error);
    }
  };

  const generateRoomId = (): string => {
    return `${user?.id}_${mentorId}_${Date.now()}`;
  };

  const checkBalanceAndInitiateCall = async (isVideo: boolean) => {
    if (!user?.id) {
      Alert.alert('Error', 'Please login to make a call.');
      return;
    }

    setCheckingBalance(true);
    
    try {
      const sessionType = isVideo ? 'VIDEO_CALL' : 'AUDIO_CALL';
      const balanceCheck = await WalletService.canStartSession(user.id, sessionType);
      
      if (!balanceCheck.canStart) {
        Alert.alert(
          'Insufficient Balance',
          balanceCheck.message + '\n\nWould you like to add money to your wallet?',
          [
            { text: 'Cancel', style: 'cancel' },
            { 
              text: 'Add Money', 
              onPress: () => {
                setShowCallOptions(false);
                // The WalletBalance component will handle adding money
              }
            }
          ]
        );
        return;
      }

      // Balance is sufficient, start the call
      const roomId = generateRoomId();
      
      try {
        // Start billing session via webhook
        const sessionStatus = await WalletService.startSession(
          roomId,
          user.id,
          mentorId,
          sessionType
        );

        if (sessionStatus.status === 'STARTED') {
          setShowCallOptions(false);
          
          // Navigate to video call screen
          router.push({
            pathname: '/video-call',
            params: {
              roomId,
              mentorId,
              mentorName,
              isVideo: isVideo.toString(),
              isCaller: 'true',
              sessionType,
              ratePerMinute: sessionStatus.ratePerMinute.toString(),
            },
          });
        } else {
          Alert.alert('Error', sessionStatus.message || 'Failed to start session');
        }
      } catch (sessionError: any) {
        Alert.alert('Session Error', sessionError.message || 'Failed to start billing session');
      }

    } catch (error: any) {
      Alert.alert('Error', error.message || 'Failed to check wallet balance');
    } finally {
      setCheckingBalance(false);
    }
  };

  const handleCallPress = () => {
    if (!isAvailable) {
      Alert.alert('Unavailable', 'This mentor is currently unavailable for calls.');
      return;
    }
    
    setShowCallOptions(true);
  };

  const getRateText = (isVideo: boolean) => {
    return isVideo ? '₹17/min' : '₹11/min';
  };

  return (
    <>
      <View style={styles.container}>
        {/* Wallet Balance Display */}
        <WalletBalance 
          onBalanceUpdate={setWalletBalance}
          showAddMoney={true}
        />

        {/* Call Button */}
        <TouchableOpacity
          style={[
            styles.callButton,
            !isAvailable && styles.callButtonDisabled,
          ]}
          onPress={handleCallPress}
          disabled={!isAvailable || checkingBalance}
        >
          <Ionicons
            name="call"
            size={24}
            color={isAvailable ? '#fff' : '#999'}
          />
          <Text style={[
            styles.callButtonText,
            !isAvailable && styles.callButtonTextDisabled,
          ]}>
            {isAvailable ? 'Call Now' : 'Unavailable'}
          </Text>
        </TouchableOpacity>
      </View>

      <Modal
        visible={showCallOptions}
        transparent
        animationType="fade"
        onRequestClose={() => setShowCallOptions(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Choose Call Type</Text>
            <Text style={styles.modalSubtitle}>
              Calling {mentorName}
            </Text>

            <View style={styles.balanceInfo}>
              <Text style={styles.balanceInfoText}>
                Your Balance: ₹{walletBalance.toFixed(2)}
              </Text>
            </View>

            <View style={styles.callOptionsContainer}>
              <TouchableOpacity
                style={styles.callOption}
                onPress={() => checkBalanceAndInitiateCall(false)}
                disabled={checkingBalance}
              >
                <View style={styles.callOptionIcon}>
                  <Ionicons name="call" size={32} color="#10b981" />
                </View>
                <Text style={styles.callOptionText}>Audio Call</Text>
                <Text style={styles.callOptionSubtext}>Voice only</Text>
                <Text style={styles.callOptionRate}>{getRateText(false)}</Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={styles.callOption}
                onPress={() => checkBalanceAndInitiateCall(true)}
                disabled={checkingBalance}
              >
                <View style={styles.callOptionIcon}>
                  <Ionicons name="videocam" size={32} color="#6366f1" />
                </View>
                <Text style={styles.callOptionText}>Video Call</Text>
                <Text style={styles.callOptionSubtext}>Audio & Video</Text>
                <Text style={styles.callOptionRate}>{getRateText(true)}</Text>
              </TouchableOpacity>
            </View>

            <View style={styles.billingNote}>
              <Text style={styles.billingNoteText}>
                • Billing starts when call connects
              </Text>
              <Text style={styles.billingNoteText}>
                • Minimum 1 minute charge
              </Text>
              <Text style={styles.billingNoteText}>
                • Auto-disconnect on low balance
              </Text>
            </View>

            <TouchableOpacity
              style={styles.cancelButton}
              onPress={() => setShowCallOptions(false)}
              disabled={checkingBalance}
            >
              <Text style={styles.cancelButtonText}>Cancel</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </>
  );
}

const styles = StyleSheet.create({
  container: {
    marginVertical: 8,
  },
  callButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#10b981',
    paddingHorizontal: 20,
    paddingVertical: 12,
    borderRadius: 25,
    justifyContent: 'center',
    marginTop: 8,
  },
  callButtonDisabled: {
    backgroundColor: '#6b7280',
  },
  callButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
    marginLeft: 8,
  },
  callButtonTextDisabled: {
    color: '#9ca3af',
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modalContent: {
    backgroundColor: '#fff',
    borderRadius: 20,
    padding: 24,
    width: '85%',
    maxWidth: 400,
  },
  modalTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 8,
    color: '#1f2937',
  },
  modalSubtitle: {
    fontSize: 16,
    textAlign: 'center',
    color: '#6b7280',
    marginBottom: 16,
  },
  balanceInfo: {
    backgroundColor: '#f0f9ff',
    padding: 12,
    borderRadius: 12,
    marginBottom: 20,
    borderWidth: 1,
    borderColor: '#e0f2fe',
  },
  balanceInfoText: {
    fontSize: 14,
    fontWeight: '600',
    color: '#0369a1',
    textAlign: 'center',
  },
  callOptionsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginBottom: 20,
  },
  callOption: {
    alignItems: 'center',
    flex: 1,
    marginHorizontal: 8,
  },
  callOptionIcon: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: '#f3f4f6',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 12,
  },
  callOptionText: {
    fontSize: 16,
    fontWeight: '600',
    color: '#1f2937',
    marginBottom: 4,
  },
  callOptionSubtext: {
    fontSize: 12,
    color: '#6b7280',
    textAlign: 'center',
    marginBottom: 4,
  },
  callOptionRate: {
    fontSize: 12,
    fontWeight: '600',
    color: '#059669',
    backgroundColor: '#d1fae5',
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 10,
  },
  billingNote: {
    backgroundColor: '#fef3c7',
    padding: 12,
    borderRadius: 12,
    marginBottom: 20,
    borderWidth: 1,
    borderColor: '#fde68a',
  },
  billingNoteText: {
    fontSize: 12,
    color: '#92400e',
    marginBottom: 2,
  },
  cancelButton: {
    backgroundColor: '#f3f4f6',
    paddingVertical: 12,
    borderRadius: 12,
    alignItems: 'center',
  },
  cancelButtonText: {
    fontSize: 16,
    color: '#6b7280',
    fontWeight: '500',
  },
});