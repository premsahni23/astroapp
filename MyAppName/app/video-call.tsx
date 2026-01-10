import React, { useEffect, useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Alert,
  BackHandler,
} from 'react-native';
import { useLocalSearchParams, router } from 'expo-router';
import VideoCallScreen from '../components/VideoCallScreen';
import { useUser } from '../contexts/UserContext';

export default function VideoCallPage() {
  const params = useLocalSearchParams();
  const { user } = useUser();
  
  const roomId = params.roomId as string;
  const mentorId = params.mentorId as string;
  const isVideo = params.isVideo === 'true';
  const isCaller = params.isCaller === 'true';
  const mentorName = params.mentorName as string;
  const sessionType = params.sessionType as string || 'AUDIO_CALL';
  const ratePerMinute = params.ratePerMinute as string || '11';

  const [isCallActive, setIsCallActive] = useState(true);

  useEffect(() => {
    // Prevent back button during call
    const backHandler = BackHandler.addEventListener('hardwareBackPress', () => {
      Alert.alert(
        'End Call',
        'Are you sure you want to end the call?',
        [
          { text: 'Cancel', style: 'cancel' },
          { text: 'End Call', style: 'destructive', onPress: handleEndCall },
        ]
      );
      return true;
    });

    return () => backHandler.remove();
  }, []);

  const handleEndCall = () => {
    setIsCallActive(false);
    router.back();
  };

  if (!roomId || !user) {
    return (
      <View style={styles.errorContainer}>
        <Text style={styles.errorText}>Invalid call parameters</Text>
      </View>
    );
  }

  if (!isCallActive) {
    return (
      <View style={styles.endedContainer}>
        <Text style={styles.endedText}>Call Ended</Text>
      </View>
    );
  }

  return (
    <VideoCallScreen
      roomId={roomId}
      isVideo={isVideo}
      isCaller={isCaller}
      onEndCall={handleEndCall}
      callerId={isCaller ? user.id : mentorId}
      calleeId={isCaller ? mentorId : user.id}
      sessionType={sessionType}
      ratePerMinute={ratePerMinute}
    />
  );
}

const styles = StyleSheet.create({
  errorContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#000',
  },
  errorText: {
    color: '#fff',
    fontSize: 18,
  },
  endedContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#000',
  },
  endedText: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
  },
});