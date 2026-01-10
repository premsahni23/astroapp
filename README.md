# 🔮 Mindaro - Astrology Consultation App

A full-stack astrology consultation platform built with **React Native (Expo)** and **Spring Boot**, featuring real-time video/audio calls, chat messaging, and wallet-based billing system.

## 📱 Features

### **Frontend (React Native + Expo)**
- 🎯 **Modern UI/UX** with tab-based navigation
- 👤 **User Authentication** (Email/Password + Google OAuth ready)
- 🔮 **Mentor Discovery** with filtering and search
- 📞 **Audio/Video Calls** with WebRTC integration
- 💬 **Real-time Chat** messaging system
- 💰 **Digital Wallet** with balance management
- 📊 **Call History** and session tracking
- 🌟 **Ratings & Reviews** system
- 📱 **Cross-platform** (iOS, Android, Web)

### **Backend (Spring Boot + MariaDB)**
- 🚀 **RESTful API** with comprehensive endpoints
- 🔐 **Secure Authentication** with JWT tokens
- 💾 **MariaDB Database** with JPA/Hibernate
- 🎥 **Video Session Management** 
- 💳 **Billing & Wallet System**
- 📨 **Real-time Messaging** 
- 📊 **Session Analytics**
- 🔧 **Admin Panel** capabilities
- 📚 **Swagger Documentation**

## 🏗️ Architecture

```
mindaro/
├── MyAppName/                 # React Native Frontend
│   ├── app/                   # Expo Router pages
│   ├── components/            # Reusable UI components
│   ├── services/              # API services
│   ├── contexts/              # React contexts
│   └── hooks/                 # Custom hooks
│
└── mindaro-backend/           # Spring Boot Backend
    ├── src/main/java/         # Java source code
    ├── src/main/resources/    # Configuration files
    └── build.gradle           # Gradle build file
```

## 🚀 Quick Start

### **Prerequisites**
- Node.js 18+ and npm/yarn
- Java 17+ and Gradle
- MariaDB 10.5+
- Expo CLI (`npm install -g @expo/cli`)

### **Backend Setup**

1. **Clone and navigate to backend:**
   ```bash
   cd mindaro-backend
   ```

2. **Configure database in `application.properties`:**
   ```properties
   spring.datasource.url=jdbc:mariadb://localhost:3306/mindaro_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Run the backend:**
   ```bash
   ./gradlew bootRun
   ```
   Server starts at `http://localhost:3000`

### **Frontend Setup**

1. **Navigate to frontend:**
   ```bash
   cd MyAppName
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Update IP address in `config/auth.ts`:**
   ```typescript
   return 'http://YOUR_COMPUTER_IP:3000'; // Replace with your IP
   ```

4. **Start the development server:**
   ```bash
   npx expo start
   ```

5. **Run on device/simulator:**
   - **iOS:** Press `i` or scan QR code with Camera app
   - **Android:** Press `a` or scan QR code with Expo Go app
   - **Web:** Press `w`

## 📡 API Endpoints

### **Authentication**
- `POST /api/user/register` - User registration
- `POST /api/user/login` - User login
- `GET /api/user/{email}` - Get user profile

### **Mentors**
- `GET /api/mentor/list` - Get all mentors
- `GET /api/mentor/{id}` - Get mentor details
- `POST /api/mentor` - Create mentor (Admin)

### **Video Sessions**
- `POST /api/video_sessions/create` - Create video session
- `POST /api/video_sessions/join` - Join session
- `POST /api/video_sessions/end` - End session

### **Wallet & Billing**
- `GET /api/wallet/balance/{userId}` - Get wallet balance
- `POST /api/wallet/add-money` - Add money to wallet
- `POST /api/billing/session/start` - Start billing session

### **Chat & Messaging**
- `POST /api/messages` - Send message
- `GET /api/messages/chatroom/{id}` - Get chat messages
- `POST /api/chatrooms` - Create chat room

## 🛠️ Development

### **Project Structure**

**Frontend Components:**
- `app/(tabs)/` - Main tab screens (Home, Call, Messages, Profile)
- `components/` - Reusable UI components
- `services/` - API integration services
- `contexts/` - Global state management

**Backend Structure:**
- `controller/` - REST API controllers
- `service/` - Business logic services
- `repository/` - Data access layer
- `model/` - Entity models
- `payload/` - Request/Response DTOs

### **Key Technologies**

**Frontend:**
- React Native + Expo Router
- TypeScript
- React Context for state management
- Expo AV for media handling
- Linear Gradient for UI effects

**Backend:**
- Spring Boot 3.x
- Spring Data JPA
- MariaDB with Hibernate
- Swagger/OpenAPI documentation
- Gradle build system

## 🔧 Configuration

### **Environment Variables**

**Frontend (`config/auth.ts`):**
```typescript
export const AUTH_CONFIG = {
  API: {
    BASE_URL: 'http://your-ip:3000',
    API_KEY: 'your-api-key',
    CLIENT_SECRET: 'your-client-secret',
  },
  GOOGLE: {
    WEB_CLIENT_ID: 'your-google-client-id',
    // ... other OAuth configs
  }
};
```

**Backend (`application.properties`):**
```properties
# Database
spring.datasource.url=jdbc:mariadb://localhost:3306/mindaro_db
spring.datasource.username=root
spring.datasource.password=your_password

# Server
server.port=3000
server.address=0.0.0.0

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## 📱 Mobile App Features

### **Call Screen**
- Browse available mentors
- Filter by specialization (Love, Career, etc.)
- Start audio/video calls with one tap
- View mentor ratings and experience
- Check wallet balance before calls

### **Video Call Screen**
- WebRTC-based video/audio calls
- Real-time billing integration
- Call controls (mute, camera, end call)
- Session recording capabilities

### **Chat System**
- Real-time messaging
- Chat rooms for ongoing conversations
- Message history and pagination
- File and image sharing support

### **Wallet System**
- Digital wallet with INR currency
- Add money via multiple payment methods
- Real-time balance updates
- Transaction history tracking

## 🚀 Deployment

### **Backend Deployment**
```bash
# Build JAR file
./gradlew build

# Run production JAR
java -jar build/libs/mindaro-backend-1.0.jar
```

### **Frontend Deployment**
```bash
# Build for production
npx expo build:web

# Or create native builds
npx expo build:android
npx expo build:ios
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- 📧 Email: support@mindaro.com
- 💬 Discord: [Join our community](https://discord.gg/mindaro)
- 📖 Documentation: [docs.mindaro.com](https://docs.mindaro.com)

## 🙏 Acknowledgments

- Built with ❤️ using React Native and Spring Boot
- WebRTC integration for real-time communication
- MariaDB for reliable data storage
- Expo for cross-platform mobile development

---

**Made with 🔮 by the Mindaro Team**