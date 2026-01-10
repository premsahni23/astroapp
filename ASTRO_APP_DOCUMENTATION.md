# Astro App - Complete Development Documentation

## 📱 Project Overview

**Astro App** is a comprehensive astrology consultation platform built with React Native (Expo) and Spring Boot. It features dual interfaces for both users seeking astrological guidance and professional astrologers/mentors providing services.

### 🏗️ Architecture
- **Frontend**: React Native with Expo Router
- **Backend**: Spring Boot with H2 Database
- **Authentication**: API Key based authentication
- **Real-time**: WebSocket support for chat functionality

---

## 🚀 Quick Start Guide

### Prerequisites
- Node.js (v16 or higher)
- Java 17+
- Expo CLI
- Git

### Installation & Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd astroapp
```

2. **Backend Setup**
```bash
cd mindaro-backend
./gradlew bootRun
```
Backend runs on: `http://localhost:3000`

3. **Frontend Setup**
```bash
cd MyAppName
npm install
npx expo start
```
Frontend runs on: `http://localhost:8081`

---

## 🏛️ Project Structure

```
astroapp/
├── MyAppName/                 # React Native Frontend
│   ├── app/                   # App Router Pages
│   │   ├── (tabs)/           # User Tab Navigation
│   │   ├── auth/             # Authentication Screens
│   │   ├── mentor/           # Mentor System
│   │   │   └── (tabs)/       # Mentor Tab Navigation
│   │   ├── chatbox.tsx       # User Chat Screen
│   │   ├── video-call.tsx    # Video Call Interface
│   │   └── otp-verification.tsx
│   ├── components/           # Reusable Components
│   ├── contexts/            # React Context Providers
│   ├── services/            # API Services
│   ├── config/              # Configuration Files
│   └── constants/           # App Constants
├── mindaro-backend/          # Spring Boot Backend
│   ├── src/main/java/       # Java Source Code
│   │   └── com/dekhokaun/mindarobackend/
│   │       ├── controller/   # REST Controllers
│   │       ├── service/      # Business Logic
│   │       ├── model/        # Data Models
│   │       ├── repository/   # Data Access Layer
│   │       ├── config/       # Configuration
│   │       └── exception/    # Exception Handling
│   └── src/main/resources/  # Resources & Config
└── ASTRO_APP_DOCUMENTATION.md
```

---

## 🔐 Authentication System

### API Key Authentication
The app uses API key-based authentication for secure access control.

**Configuration** (`MyAppName/config/auth.ts`):
```typescript
export const AUTH_CONFIG = {
  API: {
    BASE_URL: 'http://localhost:3000',
    API_KEY: 'mindaro-api-key-2024',
    CLIENT_SECRET: 'mindaro-client-secret-2024',
  },
};
```

**Backend Filter** (`ApiKeyAuthFilter.java`):
```java
@Component
public class ApiKeyAuthFilter implements Filter {
    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String CLIENT_SECRET_HEADER = "X-Client-Secret";
    private static final String VALID_API_KEY = "mindaro-api-key-2024";
    private static final String VALID_CLIENT_SECRET = "mindaro-client-secret-2024";
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        // API key validation logic
    }
}
```

### User Authentication Flow
1. **Registration**: `POST /api/user/register`
2. **Login**: User credentials stored in UserContext
3. **Session Management**: React Context maintains user state

---

## 🗄️ Database Schema

### H2 In-Memory Database
The backend uses H2 database with automatic table creation via Hibernate.

**Configuration** (`application.properties`):
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

### Key Models

**User Model** (`User.java`):
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String name;
    private Long mobile;
    private String country;
    private String dateOfBirth;
    private String bio;
    private String zodiacSign;
    private String profilePicture;
    private Boolean isProfileCompleted;
    
    @Enumerated(EnumType.STRING)
    private UserType utype; // CUSTOMER, MENTOR
}
```

**Message Model** (`Message.java`):
```java
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 1000)
    private String content;
    
    @Column(nullable = false)
    private String senderId;
    
    @Column(nullable = false)
    private String chatRoomId;
    
    @Enumerated(EnumType.STRING)
    private MessageType messageType; // TEXT, IMAGE, FILE, SYSTEM
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

---

## 🌐 API Endpoints

### User Management APIs

**Register User**
```http
POST /api/user/register
Content-Type: application/json
X-API-Key: mindaro-api-key-2024
X-Client-Secret: mindaro-client-secret-2024

{
  "name": "John Doe",
  "email": "john@example.com",
  "mobile": "9876543210",
  "country": "IN",
  "password": "password123"
}
```

**Update User Profile**
```http
PUT /api/user/update
Content-Type: application/json

{
  "email": "john@example.com",
  "name": "John Doe Updated",
  "dateOfBirth": "1990-01-01",
  "bio": "Updated bio information"
}
```

**Get User by Email**
```http
GET /api/user/{email}
```

### Chat System APIs

**Create Chat Room**
```http
POST /api/chatrooms
Content-Type: application/json

{
  "name": "chat_user1_mentor1"
}
```

**Send Message**
```http
POST /api/messages
Content-Type: application/json

{
  "content": "Hello, I need astrological guidance",
  "senderId": "user-uuid",
  "chatRoomId": "room-uuid",
  "messageType": "TEXT"
}
```

**Get Chat Messages**
```http
GET /api/messages/chatroom/{chatRoomId}
```

### Astrologer APIs

**Get All Astrologers**
```http
GET /api/astrologers
```

**Get Astrology Services**
```http
GET /api/services
```

---

## 📱 Frontend Architecture

### Navigation Structure

**Root Navigation** (`app/_layout.tsx`):
```typescript
<Stack screenOptions={{ headerShown: false }}>
  <Stack.Screen name="index" />
  <Stack.Screen name="auth" />
  <Stack.Screen name="(tabs)" />      // User Interface
  <Stack.Screen name="mentor" />      // Mentor Interface
  <Stack.Screen name="chatbox" />
  <Stack.Screen name="video-call" />
  <Stack.Screen name="otp-verification" />
</Stack>
```

**User Tabs** (`app/(tabs)/_layout.tsx`):
- Home - Browse astrologers and services
- Call - Voice call with astrologers
- Messages - Chat conversations
- Horoscope - Daily horoscope
- Profile - User profile management

**Mentor Tabs** (`app/mentor/(tabs)/_layout.tsx`):
- Dashboard - Performance overview
- Chat - Client conversations
- Consultations - Booking management
- Earnings - Revenue tracking
- Profile - Mentor profile
- Settings - Preferences

### State Management

**User Context** (`contexts/UserContext.tsx`):
```typescript
interface UserContextType {
  user: User | null;
  login: (userData: User) => Promise<void>;
  logout: () => Promise<void>;
  updateUser: (updates: Partial<User>) => Promise<void>;
  isLoading: boolean;
}

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  
  // Context implementation
};
```

### API Service Layer

**API Service** (`services/apiService.ts`):
```typescript
export class ApiService {
  private static baseUrl = AUTH_CONFIG.API.BASE_URL;
  
  private static getAuthHeaders(): HeadersInit {
    return {
      'Content-Type': 'application/json',
      'X-API-Key': AUTH_CONFIG.API.API_KEY,
      'X-Client-Secret': AUTH_CONFIG.API.CLIENT_SECRET,
    };
  }
  
  static async makeRequest<T>(endpoint: string, options: RequestInit = {}): Promise<ApiResponse<T>> {
    // API request implementation
  }
  
  // User APIs
  static async registerUser(userData: UserRequest): Promise<ApiResponse<any>> { }
  static async updateUserProfile(userData: UpdateUserRequest): Promise<ApiResponse<any>> { }
  
  // Chat APIs
  static async createChatRoom(name: string): Promise<ApiResponse<any>> { }
  static async sendMessage(messageData: MessageRequest): Promise<ApiResponse<any>> { }
}
```

---

## 💬 Chat System Implementation

### Real-time Messaging

**WebSocket Configuration** (`MessageSocketController.java`):
```java
@Controller
public class MessageSocketController {
    
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message message) {
        return messageService.saveMessage(message);
    }
    
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSenderName());
        return message;
    }
}
```

**Frontend Chat Implementation** (`app/chatbox.tsx`):
```typescript
export default function ChatBoxScreen() {
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputText, setInputText] = useState('');
  const [chatRoomId, setChatRoomId] = useState<string | null>(null);
  
  const initializeChatRoom = async () => {
    // Create or join chat room
    const roomName = `chat_${user?.id}_${astrologerId}`;
    const response = await ApiService.createChatRoom(roomName);
    setChatRoomId(response.data.id);
    await loadMessages(response.data.id);
  };
  
  const sendMessage = async () => {
    const response = await ApiService.sendMessage({
      content: inputText,
      senderId: user!.id,
      chatRoomId: chatRoomId!,
      messageType: 'TEXT',
    });
    
    if (response.success) {
      setMessages(prev => [...prev, response.data]);
    }
  };
}
```

---

## 👨‍🏫 Mentor System

### Mentor Dashboard Features

**Dashboard Overview** (`app/mentor/(tabs)/dashboard.tsx`):
- Performance statistics (consultations, earnings, ratings)
- Online/offline status toggle
- Quick action buttons
- Today's overview metrics

**Key Features**:
```typescript
const [mentorData] = useState({
  name: 'Dr. Rajesh Sharma',
  specialization: 'Vedic Astrology, Numerology',
  rating: 4.8,
  totalConsultations: 1247,
  todayConsultations: 8,
  pendingConsultations: 3,
  earnings: {
    today: 2400,
    thisMonth: 45600,
    total: 234500,
  },
});
```

### Mentor Chat System

**Chat Management** (`app/mentor/(tabs)/chat.tsx`):
- List of client conversations
- Unread message indicators
- Client online status
- Quick actions (broadcast, settings, analytics)

**Individual Chat** (`app/mentor/mentor-chatbox.tsx`):
- One-on-one client conversations
- Quick response templates
- Professional astrology responses
- Voice/video call integration

### Mentor Settings

**Comprehensive Settings** (`app/mentor/(tabs)/settings.tsx`):
```typescript
const [settings, setSettings] = useState({
  notifications: {
    newMessages: true,
    consultationReminders: true,
    paymentAlerts: true,
  },
  privacy: {
    showOnlineStatus: true,
    allowDirectMessages: true,
    showRatings: true,
  },
  consultation: {
    autoAcceptBookings: false,
    requireAdvancePayment: true,
    allowCancellations: true,
  },
  chat: {
    readReceipts: true,
    typingIndicators: true,
    messageSound: true,
  },
});
```

---

## 🎨 UI/UX Design System

### Color Scheme
- **Primary Gold**: `#FFD700` - Headers, buttons, accents
- **Secondary Orange**: `#FFA500` - Gradients, highlights
- **Success Green**: `#4CAF50` - Success states, online status
- **Error Red**: `#FF4444` - Error states, warnings
- **Text Dark**: `#333333` - Primary text
- **Text Light**: `#666666` - Secondary text
- **Background**: `#F8F8F8` - App background

### Component Patterns

**Gradient Headers**:
```typescript
<LinearGradient
  colors={['#FFD700', '#FFA500']}
  style={styles.header}
>
  {/* Header content */}
</LinearGradient>
```

**Action Cards**:
```typescript
<TouchableOpacity style={styles.actionCard}>
  <Ionicons name="icon-name" size={32} color="#4CAF50" />
  <Text style={styles.actionTitle}>Title</Text>
  <Text style={styles.actionSubtitle}>Subtitle</Text>
</TouchableOpacity>
```

---

## 🔧 Development Tools & Scripts

### Backend Scripts
```bash
# Start backend server
./gradlew bootRun

# Run tests
./gradlew test

# Build JAR
./gradlew build
```

### Frontend Scripts
```bash
# Start development server
npx expo start

# Start with cache cleared
npx expo start --clear

# Build for production
npx expo build

# Run on specific platform
npx expo start --web
npx expo start --android
npx expo start --ios
```

---

## 🚀 Deployment Guide

### Backend Deployment
1. **Build JAR**: `./gradlew build`
2. **Deploy to server**: Copy JAR to production server
3. **Environment variables**: Set production database URL
4. **Run**: `java -jar mindaro-backend-1.0.jar`

### Frontend Deployment
1. **Build**: `npx expo build:web`
2. **Deploy**: Upload build files to web server
3. **Environment**: Update API base URL for production

---

## 🧪 Testing

### Backend Testing
- Unit tests for services and controllers
- Integration tests for API endpoints
- Database tests with H2 test database

### Frontend Testing
- Component testing with Jest
- Integration testing with Expo
- Manual testing on multiple devices

---

## 📊 Features Summary

### ✅ Completed Features

**User System**:
- User registration and authentication
- Profile management with real API integration
- Chat system with real-time messaging
- Video call interface
- Astrologer browsing and selection

**Mentor System**:
- Complete mentor dashboard
- Client chat management
- Settings and preferences
- Broadcast messaging
- Chat analytics
- Earnings tracking
- Consultation management

**Backend**:
- RESTful API with Spring Boot
- H2 database with Hibernate
- WebSocket support for real-time chat
- API key authentication
- Exception handling and validation

**Technical**:
- Expo Router navigation
- React Context state management
- TypeScript throughout
- Responsive design
- Professional UI/UX

---

## 🔮 Future Enhancements

### Planned Features
- Payment integration (Stripe/Razorpay)
- Push notifications
- Advanced astrology calculations
- Video call implementation
- Admin dashboard
- Multi-language support
- Advanced analytics
- Appointment scheduling
- Review and rating system

### Technical Improvements
- Production database (PostgreSQL/MySQL)
- Redis for caching
- JWT authentication
- API rate limiting
- Comprehensive testing suite
- CI/CD pipeline
- Docker containerization

---

## 🤝 Contributing

### Development Workflow
1. Fork the repository
2. Create feature branch: `git checkout -b feature/new-feature`
3. Make changes and test thoroughly
4. Commit: `git commit -m "Add new feature"`
5. Push: `git push origin feature/new-feature`
6. Create Pull Request

### Code Standards
- Use TypeScript for type safety
- Follow React Native best practices
- Maintain consistent code formatting
- Write meaningful commit messages
- Add comments for complex logic

---

## 📞 Support & Contact

For technical support or questions about the codebase:
- Create an issue in the GitHub repository
- Review this documentation for implementation details
- Check the API endpoints section for backend integration

---

## 📄 License

This project is proprietary software. All rights reserved.

---

*Last Updated: January 2026*
*Version: 1.0.0*