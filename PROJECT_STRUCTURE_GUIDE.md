# ADVIJR Project Structure & Development Guide

## 📁 Project Overview

This is a full-stack service consultation application with:
- **Frontend**: React Native (Expo) mobile app
- **Backend**: Spring Boot (Java) REST API
- **Database**: MariaDB

---

## 🗂️ Directory Structure

```
ADVIJR/
├── MyAppName/                          # Frontend (React Native/Expo)
│   ├── app/                            # Main application screens
│   │   ├── (tabs)/                     # User app tab screens
│   │   ├── auth/                       # Authentication screens
│   │   └── mentor/                     # Mentor-specific screens
│   ├── components/                     # Reusable UI components
│   ├── contexts/                       # React Context providers
│   ├── services/                       # API & business logic
│   ├── constants/                      # App constants & colors
│   └── hooks/                          # Custom React hooks
│
└── mindaro-backend/                    # Backend (Spring Boot)
    ├── src/main/java/                  # Java source code
    │   └── com/dekhokaun/mindarobackend/
    │       ├── controller/             # REST API endpoints
    │       ├── service/                # Business logic
    │       ├── repository/             # Database access
    │       ├── model/                  # Entity models
    │       ├── config/                 # Configuration
    │       └── exception/              # Error handling
    └── src/main/resources/             # Configuration files
        └── application.properties      # Database & app config
```

---

## 📱 Frontend Structure (MyAppName/)

### 🎨 User Interface Files

#### **Authentication Screens** (`app/auth/`)
| File | Purpose | Key Features |
|------|---------|--------------|
| `signin.tsx` | User login | Email/password, auto-registration fallback |
| `signup.tsx` | User registration | Create new user account |
| `mentor-signin.tsx` | Mentor login | Separate mentor authentication |
| `mentor-signup.tsx` | Mentor registration | Create mentor account |
| `otp-verification.tsx` | OTP verification | Phone number verification |

**To modify**: Change login flow, add social auth, update validation

---

#### **User App Tabs** (`app/(tabs)/`)
| File | Purpose | Key Features |
|------|---------|--------------|
| `home.tsx` | Main dashboard | User greeting, quick actions, featured content |
| `profile.tsx` | User profile | Personal info, settings, account management |
| `settings.tsx` | App settings | Preferences, notifications, privacy |
| `messages.tsx` | Chat & messaging | List of mentors, chat history, start conversations |
| `call.tsx` | Voice/video calls | Call mentors, call history |
| `chat.tsx` | Chat placeholder | Redirects to messages |
| `categories.tsx` | Service categories | Browse consultation types |
| `horoscope.tsx` | Horoscope viewer | Zodiac signs, daily predictions |
| `notifications.tsx` | Notifications | App notifications, alerts |
| `_layout.tsx` | Tab navigation | Bottom tab bar configuration |

**To modify**: Change tab icons, add new tabs, update navigation

---

#### **Mentor App** (`app/mentor/(tabs)/`)
| File | Purpose | Key Features |
|------|---------|--------------|
| `dashboard.tsx` | Mentor dashboard | Stats, earnings, online status toggle |
| `profile.tsx` | Mentor profile | Professional info, ratings, reviews |
| `settings.tsx` | Mentor settings | Availability, rates, preferences |
| `earnings.tsx` | Earnings tracker | Revenue, transactions, payouts |
| `consultations.tsx` | Consultation list | Pending, ongoing, completed sessions |
| `chat.tsx` | Mentor chat list | Client conversations |
| `_layout.tsx` | Mentor tab nav | Mentor bottom tab bar |

**Additional Mentor Pages:**
| File | Purpose |
|------|---------|
| `mentor-chatbox.tsx` | Chat with clients |
| `broadcast.tsx` | Send broadcast messages |
| `chat-analytics.tsx` | Chat statistics & insights |

**To modify**: Change mentor features, add analytics, update rates

---

#### **Other Screens** (`app/`)
| File | Purpose |
|------|---------|
| `chatbox.tsx` | User chat with mentor |
| `video-call.tsx` | Video call interface |
| `_layout.tsx` | Root navigation layout |
| `index.tsx` | App entry point |

---

### 🔧 Core Services (`services/`)
| File | Purpose | Key Functions |
|------|---------|---------------|
| `apiService.ts` | **Main API client** | All backend API calls |
| `authService.ts` | User authentication | Login, signup, token management |
| `mentorAuthService.ts` | Mentor authentication | Mentor login, signup |
| `WalletService.ts` | Wallet operations | Balance, transactions, billing |
| `SignalingService.ts` | WebRTC signaling | Video call setup |

**To modify API endpoints**: Edit `apiService.ts` base URL and methods

---

### 🎯 State Management (`contexts/`)
| File | Purpose | Global State |
|------|---------|--------------|
| `UserContext.tsx` | User state | Current user, authentication status |
| `MentorContext.tsx` | Mentor state | Current mentor, online status |

**To modify**: Add new global state, update user/mentor data

---

### 🎨 UI Components (`components/`)
| File | Purpose |
|------|---------|
| `WalletBalance.tsx` | Wallet display widget |
| `CallInitiator.tsx` | Start call component |
| `VideoCallScreen.tsx` | Video call UI |
| `DatabaseManager.tsx` | Admin database tools |

**To modify**: Update UI components, add new reusable widgets

---

### 🎨 Design System (`constants/`)
| File | Purpose |
|------|---------|
| `Colors.ts` | **Brand colors** - Blue (#0052CC) & Orange (#FF8C42) |

**To change colors**: Edit `Colors.ts` and update throughout app

---

### ⚙️ Configuration Files
| File | Purpose |
|------|---------|
| `app.json` | Expo configuration, app name, version |
| `package.json` | Dependencies, scripts |
| `tsconfig.json` | TypeScript configuration |

---

## 🖥️ Backend Structure (mindaro-backend/)

### 🎯 Controllers (`controller/`)
**REST API Endpoints** - Handle HTTP requests

| File | Endpoints | Purpose |
|------|-----------|---------|
| `UserController.java` | `/api/users/**` | User CRUD operations |
| `MentorController.java` | `/api/mentors/**` | Mentor management, authentication |
| `MessageController.java` | `/api/messages/**` | Chat messages |
| `ChatRoomController.java` | `/api/chatrooms/**` | Chat room management |
| `BookingController.java` | `/api/bookings/**` | Consultation bookings |
| `BillingController.java` | `/api/billing/**` | Wallet, payments, billing |
| `VideoSessionController.java` | `/api/video-sessions/**` | Video call sessions |
| `CategoryController.java` | `/api/categories/**` | Service categories |
| `NotificationController.java` | `/api/notifications/**` | Push notifications |
| `OtpController.java` | `/api/otp/**` | OTP verification |
| `PaymentController.java` | `/api/payments/**` | Payment processing |

**To add new endpoints**: Create new controller or add methods to existing ones

---

### 💼 Services (`service/`)
**Business Logic Layer**

| File | Purpose |
|------|---------|
| `UserService.java` | User business logic |
| `MentorService.java` | Mentor operations, authentication |
| `MessageService.java` | Chat message handling |
| `BillingService.java` | Wallet, billing, transactions |
| `VideoSessionService.java` | Video call management |
| `BookingService.java` | Consultation booking logic |
| `NotificationService.java` | Notification delivery |
| `OtpService.java` | OTP generation & verification |

**To modify business logic**: Edit service classes

---

### 🗄️ Repositories (`repository/`)
**Database Access Layer** - JPA repositories

| File | Database Table |
|------|----------------|
| `UserRepository.java` | `users` |
| `MentorRepository.java` | `mentors` |
| `MessageRepository.java` | `messages` |
| `ChatRoomRepository.java` | `chat_rooms` |
| `BookingRepository.java` | `bookings` |
| `WalletRepository.java` | `wallets` |
| `TransactionRepository.java` | `transactions` |
| `CategoryRepository.java` | `categories` |

**To add database queries**: Add methods to repository interfaces

---

### 📊 Models (`model/`)
**Entity Classes** - Database table mappings

| File | Represents |
|------|------------|
| `User.java` | User entity |
| `Mentor.java` | Mentor entity (uses `pwd` field for password) |
| `Message.java` | Chat message |
| `ChatRoom.java` | Chat room |
| `Booking.java` | Consultation booking |
| `Wallet.java` | User wallet |
| `Transaction.java` | Financial transaction |
| `Category.java` | Service category |

**To modify database schema**: Update entity classes and run migrations

---

### ⚙️ Configuration (`config/`)
| File | Purpose |
|------|---------|
| `WebConfig.java` | CORS, web configuration |
| `WebSocketConfig.java` | WebSocket for real-time chat |
| `FirebaseConfig.java` | Firebase notifications |
| `SwaggerConfig.java` | API documentation |
| `CustomErrorHandler.java` | Global error handling |

---

### 🔧 Application Configuration
**File**: `src/main/resources/application.properties`

```properties
# Database Configuration
spring.datasource.url=jdbc:mariadb://localhost:3306/test_advj
spring.datasource.username=root
spring.datasource.password=your_password

# Server Configuration
server.port=8080

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
```

**To change database**: Update datasource URL, username, password

---

## 🚀 How to Make Changes

### 1️⃣ **Change App Colors/Branding**
- Edit: `MyAppName/constants/Colors.ts`
- Update: Brand colors, gradients
- Apply: Colors automatically used throughout app

### 2️⃣ **Add New User Screen**
- Create: `MyAppName/app/(tabs)/newscreen.tsx`
- Update: `MyAppName/app/(tabs)/_layout.tsx` (add tab)
- Style: Use colors from `Colors.ts`

### 3️⃣ **Add New API Endpoint**
- Create: `mindaro-backend/src/main/java/.../controller/NewController.java`
- Add: Service in `service/` folder
- Update: `MyAppName/services/apiService.ts` (frontend)

### 4️⃣ **Modify Database Schema**
- Edit: Entity in `model/` folder
- Update: Repository if needed
- Run: Backend to auto-update schema (ddl-auto=update)

### 5️⃣ **Change Authentication Flow**
- Frontend: `MyAppName/app/auth/signin.tsx`
- Backend: `MentorController.java` or `UserController.java`
- Service: `authService.ts` or `mentorAuthService.ts`

### 6️⃣ **Update App Name/Version**
- Edit: `MyAppName/app.json`
- Change: `name`, `displayName`, `version`

### 7️⃣ **Add New Mentor Feature**
- Create: `MyAppName/app/mentor/newfeature.tsx`
- Update: Mentor navigation if needed
- Backend: Add endpoints in `MentorController.java`

---

## 📤 Git Workflow

### Initial Setup
```bash
# Initialize git (if not already done)
cd /path/to/ADVIJR
git init

# Add remote repository
git remote add origin https://github.com/yourusername/advijr.git
```

### Making Changes
```bash
# 1. Check current status
git status

# 2. Add all changes
git add .

# Or add specific files
git add MyAppName/app/(tabs)/home.tsx
git add mindaro-backend/src/main/java/com/dekhokaun/mindarobackend/controller/UserController.java

# 3. Commit with descriptive message
git commit -m "feat: updated color scheme to ADVIJR branding"

# 4. Push to remote
git push origin main
```

### Commit Message Conventions
```bash
# Features
git commit -m "feat: add video call functionality"

# Bug fixes
git commit -m "fix: resolve login authentication issue"

# UI changes
git commit -m "ui: update color scheme to blue/orange"

# Backend changes
git commit -m "backend: add mentor authentication endpoint"

# Documentation
git commit -m "docs: update project structure guide"

# Configuration
git commit -m "config: update database connection settings"
```

### Common Git Commands
```bash
# View commit history
git log --oneline

# Create new branch
git checkout -b feature/new-feature

# Switch branches
git checkout main

# Pull latest changes
git pull origin main

# View differences
git diff

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Discard local changes
git checkout -- filename.tsx
```

---

## 🔑 Key Configuration Points

### Frontend Configuration
| What | Where | Purpose |
|------|-------|---------|
| API Base URL | `services/apiService.ts` | Backend connection |
| App Name | `app.json` | Display name |
| Colors | `constants/Colors.ts` | Brand colors |
| Navigation | `app/(tabs)/_layout.tsx` | Tab structure |

### Backend Configuration
| What | Where | Purpose |
|------|-------|---------|
| Database | `application.properties` | DB connection |
| Server Port | `application.properties` | API port (8080) |
| CORS | `config/WebConfig.java` | Frontend access |
| API Routes | `controller/*.java` | Endpoint paths |

---

## 📝 Important Notes

### Database
- **Name**: `test_advj`
- **Type**: MariaDB
- **Port**: 3306
- **Schema**: Auto-updated by Hibernate

### Authentication
- **Users**: Email/password (auto-registration fallback)
- **Mentors**: Separate auth system, uses `pwd` field
- **Tokens**: Managed by backend

### API Communication
- **Base URL**: `http://localhost:8080/api`
- **Format**: JSON
- **Auth**: Token-based (if implemented)

### File Naming
- **Frontend**: camelCase for files, PascalCase for components
- **Backend**: PascalCase for classes, camelCase for methods
- **Database**: snake_case for tables/columns

---

## 🎯 Quick Reference

### To Change...
| What | Edit This File |
|------|----------------|
| App colors | `constants/Colors.ts` |
| API endpoint | `services/apiService.ts` |
| Database config | `application.properties` |
| User login | `app/auth/signin.tsx` + `UserController.java` |
| Mentor login | `app/auth/mentor-signin.tsx` + `MentorController.java` |
| Tab navigation | `app/(tabs)/_layout.tsx` |
| App name | `app.json` |

---

## 📚 Additional Documentation

- `COLOR_MIGRATION_COMPLETE.md` - Color scheme changes
- `DEPLOYMENT.md` - Deployment instructions
- `PRODUCTION_READY.md` - Production checklist
- `README.md` - Project overview

---

## 🆘 Troubleshooting

### Frontend Issues
```bash
# Clear cache
cd MyAppName
npx expo start -c

# Reinstall dependencies
rm -rf node_modules
npm install
```

### Backend Issues
```bash
# Clean build
cd mindaro-backend
./gradlew clean build

# Check database connection
# Verify application.properties settings
```

---

**Last Updated**: After complete color migration to ADVIJR branding
**Version**: 1.0.0
**Status**: Production Ready ✅
