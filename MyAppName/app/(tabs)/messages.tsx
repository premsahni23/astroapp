import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  StatusBar,
  ScrollView,
  TouchableOpacity,
  Image,
  TextInput,
  Alert,
  ActivityIndicator,
  Platform,
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { router } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { useUser, getFirstName } from '../../contexts/UserContext';
import { WalletService } from '../../services/WalletService';
import WalletBalance from '../../components/WalletBalance';
import { ApiService } from '../../services/apiService';

interface Mentor {
  id: string;
  name: string;
  email: string;
  mobile: string;
  specialization?: string;
  languages?: string;
  experience?: string;
  rating: number;
  ratingCount: number;
  price?: number;
  originalPrice?: number;
  isOnline: boolean;
  hasSpecialOffer?: boolean;
  photo?: string;
}

interface ChatHistory {
  id: string;
  mentorId: string;
  mentorName: string;
  mentorPhoto: string;
  lastMessage: string;
  lastMessageTime: string;
  unreadCount: number;
  isOnline: boolean;
}

interface CallHistory {
  id: string;
  mentorId: string;
  mentorName: string;
  mentorPhoto: string;
  callType: 'voice' | 'video';
  callStatus: 'completed' | 'missed' | 'declined';
  duration: string;
  timestamp: string;
  cost: number;
}

export default function MessagesScreen() {
  const [selectedTab, setSelectedTab] = useState<'mentors' | 'chats' | 'calls'>('mentors');
  const [selectedFilter, setSelectedFilter] = useState('All');
  const [searchQuery, setSearchQuery] = useState('');
  const [mentors, setMentors] = useState<Mentor[]>([]);
  const [filteredMentors, setFilteredMentors] = useState<Mentor[]>([]);
  const [chatHistory, setChatHistory] = useState<ChatHistory[]>([]);
  const [callHistory, setCallHistory] = useState<CallHistory[]>([]);
  const [filteredChats, setFilteredChats] = useState<ChatHistory[]>([]);
  const [filteredCalls, setFilteredCalls] = useState<CallHistory[]>([]);
  const [loading, setLoading] = useState(true);
  const [walletBalance, setWalletBalance] = useState(0);
  const { user } = useUser();
  const filters = ['All', 'NEW!', 'Love', 'Career'];

  // Get user's first name for greeting
  const firstName = user ? getFirstName(user.name) : 'User';

  useEffect(() => {
    if (user?.id) {
      loadMentors();
      loadWalletBalance();
      loadChatHistory();
      loadCallHistory();
    }
  }, [user?.id]);

  useEffect(() => {
    filterContent();
  }, [searchQuery, selectedFilter, mentors, chatHistory, callHistory, selectedTab]);

  const loadMentors = async () => {
    try {
      setLoading(true);
      const response = await ApiService.getMentors();
      if (response.success && response.data) {
        const mentorsWithDefaults = response.data.map((mentor: any) => ({
          ...mentor,
          specialization: mentor.specialization || 'Vedic Astrology',
          languages: mentor.languages || 'English, Hindi',
          experience: mentor.experience || '5+ Years',
          price: mentor.price || 17,
          originalPrice: mentor.originalPrice || 21,
          isOnline: Math.random() > 0.3, // Random online status for demo
          hasSpecialOffer: Math.random() > 0.5,
          photo: mentor.photo || `https://via.placeholder.com/60x60/4A90E2/FFFFFF?text=${mentor.name.charAt(0)}`
        }));
        setMentors(mentorsWithDefaults);
      }
    } catch (error) {
      console.error('Error loading mentors:', error);
      Alert.alert('Error', 'Failed to load mentors. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const loadChatHistory = async () => {
    try {
      if (!user?.id) return;
      
      // Get user's chat rooms from backend
      const response = await ApiService.getUserChatRooms(user.id);
      if (response.success && response.data) {
        const chatHistoryData: ChatHistory[] = await Promise.all(
          response.data.map(async (chatRoom: any) => {
            // Get the last message for each chat room
            const messagesResponse = await ApiService.getChatRoomMessagesPaginated(chatRoom.id, 0, 1);
            const lastMessage = messagesResponse.success && messagesResponse.data?.content?.length > 0 
              ? messagesResponse.data.content[0] 
              : null;

            // Get mentor info
            const mentorResponse = await ApiService.getMentorById(chatRoom.mentorId);
            const mentor = mentorResponse.success ? mentorResponse.data : null;

            return {
              id: chatRoom.id,
              mentorId: chatRoom.mentorId || 'unknown',
              mentorName: mentor?.name || chatRoom.name || 'Unknown Mentor',
              mentorPhoto: mentor?.photo || `https://via.placeholder.com/60x60/4A90E2/FFFFFF?text=${(mentor?.name || 'M').charAt(0)}`,
              lastMessage: lastMessage?.content || 'No messages yet',
              lastMessageTime: lastMessage?.createdAt || chatRoom.createdAt || new Date().toISOString(),
              unreadCount: 0, // TODO: Implement unread count logic
              isOnline: Math.random() > 0.3, // TODO: Get real online status
            };
          })
        );
        
        setChatHistory(chatHistoryData);
      } else {
        // No chat history found
        setChatHistory([]);
      }
    } catch (error) {
      console.error('Error loading chat history:', error);
      setChatHistory([]);
    }
  };

  const loadCallHistory = async () => {
    try {
      if (!user?.id) return;
      
      // Get user's video sessions (call history) from backend
      const response = await ApiService.getUserBookings(user.id);
      if (response.success && response.data) {
        const callHistoryData: CallHistory[] = await Promise.all(
          response.data
            .filter((booking: any) => booking.serviceType === 'VIDEO_CALL' || booking.serviceType === 'AUDIO_CALL')
            .map(async (booking: any) => {
              // Get mentor info
              const mentorResponse = await ApiService.getMentorById(booking.mentorId);
              const mentor = mentorResponse.success ? mentorResponse.data : null;

              return {
                id: booking.id,
                mentorId: booking.mentorId,
                mentorName: mentor?.name || 'Unknown Mentor',
                mentorPhoto: mentor?.photo || `https://via.placeholder.com/60x60/4A90E2/FFFFFF?text=${(mentor?.name || 'M').charAt(0)}`,
                callType: booking.serviceType === 'VIDEO_CALL' ? 'video' : 'voice',
                callStatus: booking.status === 'COMPLETED' ? 'completed' : 
                          booking.status === 'CANCELLED' ? 'declined' : 'missed',
                duration: booking.duration ? `${Math.floor(booking.duration / 60)}:${(booking.duration % 60).toString().padStart(2, '0')}` : '0:00',
                timestamp: booking.scheduledTime || booking.createdAt || new Date().toISOString(),
                cost: booking.totalCost || 0,
              };
            })
        );
        
        // Sort by timestamp (most recent first)
        callHistoryData.sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());
        
        setCallHistory(callHistoryData);
      } else {
        // No call history found
        setCallHistory([]);
      }
    } catch (error) {
      console.error('Error loading call history:', error);
      // Fallback: try to get from video sessions API if bookings don't work
      try {
        // This would be a custom endpoint to get user's video sessions
        // For now, we'll just set empty array
        setCallHistory([]);
      } catch (fallbackError) {
        console.error('Fallback call history loading failed:', fallbackError);
        setCallHistory([]);
      }
    }
  };

  const loadWalletBalance = async () => {
    if (!user?.id) return;
    
    try {
      const balance = await WalletService.getBalance(user.id);
      setWalletBalance(balance.balance);
    } catch (error) {
      console.error('Error loading wallet balance:', error);
    }
  };

  const filterContent = () => {
    // Filter mentors (only apply search filter on mentors tab)
    let filteredMentorsList = mentors;
    if (selectedTab === 'mentors' && searchQuery.trim()) {
      filteredMentorsList = filteredMentorsList.filter(mentor => 
        mentor.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        (mentor.specialization && mentor.specialization.toLowerCase().includes(searchQuery.toLowerCase())) ||
        (mentor.languages && mentor.languages.toLowerCase().includes(searchQuery.toLowerCase())) ||
        (mentor.mobile && mentor.mobile.includes(searchQuery))
      );
    }
    
    if (selectedFilter !== 'All') {
      filteredMentorsList = filteredMentorsList.filter(mentor => {
        switch (selectedFilter) {
          case 'Love':
            return mentor.specialization?.toLowerCase().includes('love') || 
                   mentor.specialization?.toLowerCase().includes('relationship');
          case 'Career':
            return mentor.specialization?.toLowerCase().includes('career') || 
                   mentor.specialization?.toLowerCase().includes('business');
          case 'NEW!':
            return mentor.experience?.includes('2 Years') || mentor.experience?.includes('3 Years');
          default:
            return true;
        }
      });
    }
    setFilteredMentors(filteredMentorsList);

    // Chat history - no search filtering, show all
    setFilteredChats(chatHistory);

    // Call history - no search filtering, show all
    setFilteredCalls(callHistory);
  };

  const handleChatPress = async (mentor: Mentor) => {
    if (!user?.id) {
      Alert.alert('Error', 'Please login to start a chat.');
      return;
    }

    try {
      // Check wallet balance before starting chat
      const balanceCheck = await WalletService.canStartSession(user.id, 'CHAT');
      
      if (!balanceCheck.canStart) {
        Alert.alert(
          'Insufficient Balance',
          balanceCheck.message + '\n\nWould you like to add money to your wallet?',
          [
            { text: 'Cancel', style: 'cancel' },
            { 
              text: 'Add Money', 
              onPress: () => {
                Alert.alert('Add Money', 'Use the "Add Money" button in the wallet section above.');
              }
            }
          ]
        );
        return;
      }

      // Balance is sufficient, start the chat session
      const sessionId = `chat_${user.id}_${mentor.id}_${Date.now()}`;
      
      try {
        // Start billing session via webhook
        const sessionStatus = await WalletService.startSession(
          sessionId,
          user.id,
          mentor.id, // Use mentor.id directly (should be UUID or numeric)
          'CHAT'
        );

        if (sessionStatus.status === 'STARTED') {
          // Navigate to chat with session info
          router.push({
            pathname: '/chatbox',
            params: {
              astrologerId: mentor.id,
              astrologerName: mentor.name,
              astrologerImage: mentor.photo,
              isOnline: mentor.isOnline.toString(),
              sessionId: sessionId,
              ratePerMinute: sessionStatus.ratePerMinute.toString(),
            }
          });
        } else {
          Alert.alert('Error', sessionStatus.message || 'Failed to start chat session');
        }
      } catch (sessionError: any) {
        console.error('Session error details:', sessionError);
        Alert.alert('Session Error', 'Unable to start session. Please try again or contact support.');
      }

    } catch (error: any) {
      console.error('Balance check error:', error);
      Alert.alert('Error', 'Unable to check balance. Please try again.');
    }
  };

  const handleVideoCallPress = async (mentor: Mentor) => {
    if (!user?.id) {
      Alert.alert('Error', 'Please login to start a video call.');
      return;
    }

    try {
      // Check wallet balance before starting video call
      const balanceCheck = await WalletService.canStartSession(user.id, 'VIDEO_CALL');
      
      if (!balanceCheck.canStart) {
        Alert.alert(
          'Insufficient Balance',
          balanceCheck.message + '\n\nWould you like to add money to your wallet?',
          [
            { text: 'Cancel', style: 'cancel' },
            { 
              text: 'Add Money', 
              onPress: () => {
                Alert.alert('Add Money', 'Use the "Add Money" button in the wallet section above.');
              }
            }
          ]
        );
        return;
      }

      // Create video session first
      try {
        const videoSessionResponse = await ApiService.createVideoSession({
          userId: user.id,
          mentorId: mentor.id,
          sessionType: 'VIDEO_CALL'
        });

        if (videoSessionResponse.success) {
          // Navigate to video call with session info
          router.push({
            pathname: '/video-call',
            params: {
              roomId: videoSessionResponse.data?.roomId || `video_${user.id}_${mentor.id}_${Date.now()}`,
              mentorId: mentor.id,
              mentorName: mentor.name,
              isVideo: 'true',
              isCaller: 'true',
              sessionType: 'VIDEO_CALL',
              ratePerMinute: '17',
            }
          });
        } else {
          Alert.alert('Error', 'Failed to create video session. Please try again.');
        }
      } catch (videoError: any) {
        console.error('Video session error:', videoError);
        Alert.alert('Error', 'Unable to start video call. Please try again or contact support.');
      }
    } catch (error: any) {
      console.error('Balance check error:', error);
      Alert.alert('Error', 'Unable to check balance. Please try again.');
    }
  };

  const handleChatHistoryPress = (chat: ChatHistory) => {
    router.push({
      pathname: '/chatbox',
      params: {
        astrologerId: chat.mentorId,
        astrologerName: chat.mentorName,
        astrologerImage: chat.mentorPhoto,
        isOnline: chat.isOnline.toString(),
      }
    });
  };

  const handleCallHistoryPress = (call: CallHistory) => {
    Alert.alert(
      'Call Again?',
      `Would you like to call ${call.mentorName} again?`,
      [
        { text: 'Cancel', style: 'cancel' },
        { 
          text: call.callType === 'video' ? 'Video Call' : 'Voice Call', 
          onPress: () => {
            if (call.callType === 'video') {
              handleVideoCallPress(mentors.find(m => m.id === call.mentorId)!);
            } else {
              // Handle voice call
              Alert.alert('Voice Call', 'Voice call feature will be available soon!');
            }
          }
        }
      ]
    );
  };

  const formatTime = (dateString: string) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffInHours = (now.getTime() - date.getTime()) / (1000 * 60 * 60);
    
    if (diffInHours < 1) {
      return 'Just now';
    } else if (diffInHours < 24) {
      return date.toLocaleTimeString('en-US', { 
        hour: '2-digit', 
        minute: '2-digit',
        hour12: true 
      });
    } else if (diffInHours < 48) {
      return 'Yesterday';
    } else {
      return date.toLocaleDateString('en-US', { 
        month: 'short', 
        day: 'numeric' 
      });
    }
  };

  const getCallStatusIcon = (status: string) => {
    switch (status) {
      case 'completed':
        return 'checkmark-circle';
      case 'missed':
        return 'close-circle';
      case 'declined':
        return 'remove-circle';
      default:
        return 'call';
    }
  };

  const getCallStatusColor = (status: string) => {
    switch (status) {
      case 'completed':
        return '#4CAF50';
      case 'missed':
        return '#FF5722';
      case 'declined':
        return '#FF9800';
      default:
        return '#666';
    }
  };

  const renderStars = (rating: number) => {
    return Array.from({ length: 5 }, (_, index) => (
      <Text key={index} style={styles.star}>
        {index < rating ? '★' : '☆'}
      </Text>
    ));
  };

  const renderTabContent = () => {
    if (loading) {
      return (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color="#0052CC" />
          <Text style={styles.loadingText}>Loading...</Text>
        </View>
      );
    }

    switch (selectedTab) {
      case 'chats':
        return renderChatHistory();
      case 'calls':
        return renderCallHistory();
      default:
        return renderMentors();
    }
  };

  const renderChatHistory = () => (
    <ScrollView style={styles.content} showsVerticalScrollIndicator={false}>
      {filteredChats.length > 0 ? (
        filteredChats.map((chat) => (
          <TouchableOpacity
            key={chat.id}
            style={styles.historyItem}
            onPress={() => handleChatHistoryPress(chat)}
          >
            <View style={styles.historyImageContainer}>
              <Image source={{ uri: chat.mentorPhoto }} style={styles.historyImage} />
              {chat.isOnline && <View style={styles.onlineIndicator} />}
            </View>
            
            <View style={styles.historyContent}>
              <View style={styles.historyHeader}>
                <Text style={styles.historyName}>{chat.mentorName}</Text>
                <Text style={styles.historyTime}>{formatTime(chat.lastMessageTime)}</Text>
              </View>
              
              <View style={styles.historyFooter}>
                <Text 
                  style={[
                    styles.historyMessage,
                    chat.unreadCount > 0 && styles.unreadMessage
                  ]}
                  numberOfLines={1}
                >
                  {chat.lastMessage}
                </Text>
                
                {chat.unreadCount > 0 && (
                  <View style={styles.unreadBadge}>
                    <Text style={styles.unreadCount}>
                      {chat.unreadCount > 99 ? '99+' : chat.unreadCount}
                    </Text>
                  </View>
                )}
              </View>
            </View>
          </TouchableOpacity>
        ))
      ) : (
        <View style={styles.noResultsContainer}>
          <Ionicons name="chatbubbles-outline" size={64} color="#CCC" />
          <Text style={styles.noResultsText}>No chat history</Text>
          <Text style={styles.noResultsSubtext}>Your conversations will appear here</Text>
        </View>
      )}
    </ScrollView>
  );

  const renderCallHistory = () => (
    <ScrollView style={styles.content} showsVerticalScrollIndicator={false}>
      {filteredCalls.length > 0 ? (
        filteredCalls.map((call) => (
          <TouchableOpacity
            key={call.id}
            style={styles.historyItem}
            onPress={() => handleCallHistoryPress(call)}
          >
            <View style={styles.historyImageContainer}>
              <Image source={{ uri: call.mentorPhoto }} style={styles.historyImage} />
            </View>
            
            <View style={styles.historyContent}>
              <View style={styles.historyHeader}>
                <Text style={styles.historyName}>{call.mentorName}</Text>
                <Text style={styles.historyTime}>{formatTime(call.timestamp)}</Text>
              </View>
              
              <View style={styles.historyFooter}>
                <View style={styles.callInfo}>
                  <Ionicons 
                    name={call.callType === 'video' ? 'videocam' : 'call'} 
                    size={16} 
                    color={getCallStatusColor(call.callStatus)} 
                  />
                  <Text style={[styles.callType, { color: getCallStatusColor(call.callStatus) }]}>
                    {call.callType === 'video' ? 'Video' : 'Voice'} • {call.duration}
                  </Text>
                </View>
                
                <View style={styles.callStatusContainer}>
                  <Ionicons 
                    name={getCallStatusIcon(call.callStatus)} 
                    size={16} 
                    color={getCallStatusColor(call.callStatus)} 
                  />
                  {call.cost > 0 && (
                    <Text style={styles.callCost}>₹{call.cost.toFixed(2)}</Text>
                  )}
                </View>
              </View>
            </View>
          </TouchableOpacity>
        ))
      ) : (
        <View style={styles.noResultsContainer}>
          <Ionicons name="call-outline" size={64} color="#CCC" />
          <Text style={styles.noResultsText}>No call history</Text>
          <Text style={styles.noResultsSubtext}>Your calls will appear here</Text>
        </View>
      )}
    </ScrollView>
  );

  const renderMentors = () => (
    <ScrollView style={styles.content} showsVerticalScrollIndicator={false}>
      {filteredMentors.length > 0 ? (
        filteredMentors.map((mentor) => (
          <View key={mentor.id} style={styles.astrologerCard}>
            <View style={styles.astrologerInfo}>
              <View style={styles.astrologerImageContainer}>
                <Image 
                  source={{ uri: mentor.photo }}
                  style={styles.astrologerImage}
                />
                {mentor.isOnline && <View style={styles.onlineIndicator} />}
              </View>
              
              <View style={styles.astrologerDetails}>
                <Text style={styles.astrologerName}>{mentor.name}</Text>
                <Text style={styles.specialization}>{mentor.specialization}</Text>
                <Text style={styles.languages}>{mentor.languages}</Text>
                <Text style={styles.experience}>Exp- {mentor.experience}</Text>
                
                <View style={styles.ratingContainer}>
                  <View style={styles.stars}>
                    {renderStars(mentor.rating)}
                  </View>
                  <Text style={styles.orders}>{mentor.ratingCount} ratings</Text>
                </View>
                
                {mentor.hasSpecialOffer && (
                  <View style={styles.specialOfferContainer}>
                    <Text style={styles.specialOfferIcon}>🎁</Text>
                    <Text style={styles.specialOfferText}>Special offer for new users</Text>
                  </View>
                )}
              </View>
            </View>
            
            <View style={styles.priceAndAction}>
              <View style={styles.priceContainer}>
                <Text style={styles.originalPrice}>₹ {mentor.originalPrice}</Text>
                <Text style={styles.currentPrice}>₹ {mentor.price}/min</Text>
              </View>
              
              <View style={styles.actionButtons}>
                <TouchableOpacity 
                  style={styles.chatButton}
                  onPress={() => handleChatPress(mentor)}
                >
                  <Ionicons name="chatbubbles" size={14} color="#4CAF50" />
                  <Text style={styles.chatButtonText}>Chat</Text>
                </TouchableOpacity>
                
                <TouchableOpacity 
                  style={styles.videoCallButton}
                  onPress={() => handleVideoCallPress(mentor)}
                >
                  <Ionicons name="videocam" size={14} color="#FFFFFF" />
                  <Text style={styles.videoCallButtonText}>Video</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        ))
      ) : (
        <View style={styles.noResultsContainer}>
          <Text style={styles.noResultsText}>
            {searchQuery ? `No mentors found for "${searchQuery}"` : 'No mentors available'}
          </Text>
          <Text style={styles.noResultsSubtext}>
            {searchQuery ? 'Try searching by name, specialization, or phone number' : 'Please try again later'}
          </Text>
        </View>
      )}
    </ScrollView>
  );

  return (
    <View style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#0052CC" />
      
      {/* Header */}
      <LinearGradient
        colors={['#0052CC', '#0066FF']}
        style={styles.header}
      >
        <View style={styles.headerTop}>
          <View style={styles.userInfo}>
            <Image 
              source={{ uri: user?.profilePicture || `https://via.placeholder.com/40x40/4A90E2/FFFFFF?text=${firstName.charAt(0)}` }}
              style={styles.userAvatar}
            />
            <Text style={styles.greeting}>Hi {firstName}</Text>
          </View>
          
          <WalletBalance 
            onBalanceUpdate={setWalletBalance}
            showAddMoney={true}
          />
        </View>
        
        {/* Search Bar - Only show for mentors tab */}
        {selectedTab === 'mentors' && (
          <View style={styles.searchRow}>
            <View style={styles.searchContainer}>
              <Ionicons name="search" size={16} color="#666" style={styles.searchIcon} />
              <TextInput
                style={styles.searchInput}
                placeholder="Search mentors..."
                placeholderTextColor="#999"
                value={searchQuery}
                onChangeText={setSearchQuery}
              />
              {searchQuery.length > 0 && (
                <TouchableOpacity onPress={() => setSearchQuery('')}>
                  <Ionicons name="close-circle" size={20} color="#666" />
                </TouchableOpacity>
              )}
            </View>
          </View>
        )}
      </LinearGradient>

      {/* Tab Navigation */}
      <View style={styles.tabContainer}>
        <TouchableOpacity
          style={[styles.tab, selectedTab === 'mentors' && styles.activeTab]}
          onPress={() => {
            setSelectedTab('mentors');
            setSearchQuery(''); // Clear search when switching tabs
          }}
        >
          <Ionicons 
            name="people" 
            size={20} 
            color={selectedTab === 'mentors' ? '#0052CC' : '#666'} 
          />
          <Text style={[styles.tabText, selectedTab === 'mentors' && styles.activeTabText]}>
            Mentors
          </Text>
        </TouchableOpacity>
        
        <TouchableOpacity
          style={[styles.tab, selectedTab === 'chats' && styles.activeTab]}
          onPress={() => {
            setSelectedTab('chats');
            setSearchQuery(''); // Clear search when switching tabs
          }}
        >
          <Ionicons 
            name="chatbubbles" 
            size={20} 
            color={selectedTab === 'chats' ? '#0052CC' : '#666'} 
          />
          <Text style={[styles.tabText, selectedTab === 'chats' && styles.activeTabText]}>
            Chats
          </Text>
          {filteredChats.reduce((total, chat) => total + chat.unreadCount, 0) > 0 && (
            <View style={styles.tabBadge}>
              <Text style={styles.tabBadgeText}>
                {filteredChats.reduce((total, chat) => total + chat.unreadCount, 0)}
              </Text>
            </View>
          )}
        </TouchableOpacity>
        
        <TouchableOpacity
          style={[styles.tab, selectedTab === 'calls' && styles.activeTab]}
          onPress={() => {
            setSelectedTab('calls');
            setSearchQuery(''); // Clear search when switching tabs
          }}
        >
          <Ionicons 
            name="call" 
            size={20} 
            color={selectedTab === 'calls' ? '#0052CC' : '#666'} 
          />
          <Text style={[styles.tabText, selectedTab === 'calls' && styles.activeTabText]}>
            Calls
          </Text>
        </TouchableOpacity>
      </View>

      {/* Filter Tabs - Only show for mentors tab */}
      {selectedTab === 'mentors' && (
        <View style={styles.filterContainer}>
          <TouchableOpacity style={styles.filterButton}>
            <Text style={styles.filterIcon}>⚙️</Text>
            <Text style={styles.filterText}>Filter</Text>
          </TouchableOpacity>
          
          <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.filterScroll}>
            {filters.map((filter) => (
              <TouchableOpacity
                key={filter}
                style={[
                  styles.filterTab,
                  selectedFilter === filter && styles.activeFilterTab
                ]}
                onPress={() => setSelectedFilter(filter)}
              >
                <Text style={[
                  styles.filterTabText,
                  selectedFilter === filter && styles.activeFilterTabText
                ]}>
                  {filter}
                </Text>
              </TouchableOpacity>
            ))}
          </ScrollView>
        </View>
      )}

      {/* Content */}
      {renderTabContent()}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5F5F5',
  },
  header: {
    paddingTop: 50,
    paddingBottom: 15,
    paddingHorizontal: 20,
  },
  headerTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 15,
  },
  headerContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  userInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  userAvatar: {
    width: 40,
    height: 40,
    borderRadius: 20,
    marginRight: 10,
  },
  greeting: {
    fontSize: 18,
    fontWeight: '600',
    color: '#333',
  },
  searchRow: {
    width: '100%',
  },
  headerActions: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  searchContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(255, 255, 255, 0.9)',
    borderRadius: 25,
    paddingHorizontal: 15,
    paddingVertical: 10,
    flex: 1,
  },
  searchIcon: {
    marginRight: 10,
  },
  searchInput: {
    flex: 1,
    fontSize: 16,
    color: '#333',
  },
  balanceContainer: {
    backgroundColor: 'rgba(255, 255, 255, 0.3)',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 15,
    borderWidth: 1,
    borderColor: 'rgba(255, 255, 255, 0.5)',
  },
  balanceText: {
    fontSize: 14,
    fontWeight: '600',
    color: '#333',
  },
  loadingContainer: {
    alignItems: 'center',
    paddingVertical: 50,
  },
  loadingText: {
    fontSize: 16,
    color: '#666',
    marginTop: 10,
  },
  searchButton: {
    width: 35,
    height: 35,
    borderRadius: 17.5,
    backgroundColor: 'rgba(255, 255, 255, 0.3)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  filterContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingVertical: 15,
    backgroundColor: '#FFFFFF',
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
  },
  filterButton: {
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: 15,
  },
  filterIcon: {
    fontSize: 16,
    marginRight: 5,
  },
  filterText: {
    fontSize: 14,
    color: '#666',
  },
  filterScroll: {
    flex: 1,
  },
  filterTab: {
    paddingHorizontal: 20,
    paddingVertical: 8,
    marginRight: 10,
    borderRadius: 20,
    backgroundColor: '#F0F0F0',
  },
  activeFilterTab: {
    backgroundColor: '#0052CC',
  },
  filterTabText: {
    fontSize: 14,
    color: '#666',
    fontWeight: '500',
  },
  activeFilterTabText: {
    color: '#FFFFFF',
    fontWeight: '600',
  },
  content: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },
  astrologerCard: {
    flexDirection: 'row',
    padding: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#F0F0F0',
    backgroundColor: '#FFFFFF',
  },
  astrologerInfo: {
    flex: 1,
    flexDirection: 'row',
  },
  astrologerImageContainer: {
    position: 'relative',
    marginRight: 15,
  },
  astrologerImage: {
    width: 60,
    height: 60,
    borderRadius: 30,
  },
  onlineIndicator: {
    position: 'absolute',
    bottom: 2,
    right: 2,
    width: 16,
    height: 16,
    borderRadius: 8,
    backgroundColor: '#4CAF50',
    borderWidth: 2,
    borderColor: '#FFFFFF',
  },
  astrologerDetails: {
    flex: 1,
  },
  astrologerName: {
    fontSize: 18,
    fontWeight: '600',
    color: '#333',
    marginBottom: 4,
  },
  specialization: {
    fontSize: 14,
    color: '#666',
    marginBottom: 2,
  },
  languages: {
    fontSize: 14,
    color: '#666',
    marginBottom: 2,
  },
  experience: {
    fontSize: 14,
    color: '#666',
    marginBottom: 8,
  },
  ratingContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  stars: {
    flexDirection: 'row',
    marginRight: 10,
  },
  star: {
    fontSize: 14,
    color: '#FF8C42',
  },
  orders: {
    fontSize: 12,
    color: '#999',
  },
  specialOfferContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  specialOfferIcon: {
    fontSize: 12,
    marginRight: 5,
  },
  specialOfferText: {
    fontSize: 12,
    color: '#999',
  },
  priceAndAction: {
    alignItems: 'flex-end',
    justifyContent: 'space-between',
  },
  priceContainer: {
    alignItems: 'flex-end',
    marginBottom: 10,
  },
  originalPrice: {
    fontSize: 14,
    color: '#999',
    textDecorationLine: 'line-through',
  },
  currentPrice: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
  },
  actionButtons: {
    gap: 8,
  },
  chatButton: {
    backgroundColor: '#E8F5E8',
    borderWidth: 1,
    borderColor: '#4CAF50',
    borderRadius: 20,
    paddingHorizontal: 16,
    paddingVertical: 8,
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  chatButtonText: {
    fontSize: 14,
    fontWeight: '600',
    color: '#4CAF50',
  },
  videoCallButton: {
    backgroundColor: '#FF6B6B',
    borderRadius: 20,
    paddingHorizontal: 16,
    paddingVertical: 8,
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  videoCallButtonText: {
    fontSize: 14,
    fontWeight: '600',
    color: '#FFFFFF',
  },
  noResultsContainer: {
    alignItems: 'center',
    paddingVertical: 50,
    paddingHorizontal: 20,
  },
  noResultsText: {
    fontSize: 16,
    color: '#666',
    marginBottom: 5,
    textAlign: 'center',
  },
  noResultsSubtext: {
    fontSize: 14,
    color: '#999',
    textAlign: 'center',
  },
  
  // Tab Navigation Styles
  tabContainer: {
    flexDirection: 'row',
    backgroundColor: '#FFFFFF',
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
  },
  tab: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 15,
    paddingHorizontal: 10,
    position: 'relative',
  },
  activeTab: {
    borderBottomWidth: 2,
    borderBottomColor: '#0052CC',
  },
  tabText: {
    fontSize: 14,
    color: '#666',
    marginLeft: 6,
    fontWeight: '500',
  },
  activeTabText: {
    color: '#0052CC',
    fontWeight: '600',
  },
  tabBadge: {
    position: 'absolute',
    top: 8,
    right: 20,
    backgroundColor: '#FF4444',
    borderRadius: 10,
    paddingHorizontal: 6,
    paddingVertical: 2,
    minWidth: 20,
    alignItems: 'center',
  },
  tabBadgeText: {
    color: '#FFFFFF',
    fontSize: 12,
    fontWeight: '600',
  },
  
  // History Item Styles
  historyItem: {
    flexDirection: 'row',
    padding: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#F0F0F0',
    backgroundColor: '#FFFFFF',
  },
  historyImageContainer: {
    position: 'relative',
    marginRight: 15,
  },
  historyImage: {
    width: 50,
    height: 50,
    borderRadius: 25,
  },
  historyContent: {
    flex: 1,
  },
  historyHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 5,
  },
  historyName: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
    flex: 1,
  },
  historyTime: {
    fontSize: 12,
    color: '#999',
  },
  historyFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  historyMessage: {
    fontSize: 14,
    color: '#666',
    flex: 1,
    marginRight: 10,
  },
  unreadMessage: {
    color: '#333',
    fontWeight: '500',
  },
  unreadBadge: {
    backgroundColor: '#4CAF50',
    borderRadius: 10,
    paddingHorizontal: 6,
    paddingVertical: 2,
    minWidth: 20,
    alignItems: 'center',
  },
  unreadCount: {
    color: '#FFFFFF',
    fontSize: 12,
    fontWeight: '600',
  },
  
  // Call History Styles
  callInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  callType: {
    fontSize: 14,
    marginLeft: 6,
    fontWeight: '500',
  },
  callStatusContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  callCost: {
    fontSize: 12,
    color: '#666',
    marginLeft: 6,
  },
});