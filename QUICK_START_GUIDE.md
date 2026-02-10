# 🚀 ADVIJR Quick Start Guide

## What is ADVIJR?

ADVIJR (Get-Seek-Help) is a full-stack service consultation platform that connects users with professional advisors/consultants for chat, voice, and video consultations.

**Key Features:**
- 👥 Dual app system (User app + Mentor app)
- 💬 Real-time chat messaging
- 📹 Video & voice calls
- 💰 Integrated wallet & billing
- 📊 Analytics & earnings tracking
- 🔔 Push notifications
- ⭐ Ratings & reviews

---

## 📋 Prerequisites

### Required Software
- **Node.js** (v16 or higher) - [Download](https://nodejs.org/)
- **Java JDK** (v17 or higher) - [Download](https://www.oracle.com/java/technologies/downloads/)
- **MariaDB** (v10.6 or higher) - [Download](https://mariadb.org/download/)
- **Git** - [Download](https://git-scm.com/downloads)
- **Expo CLI** - Install: `npm install -g expo-cli`

### Optional
- **Android Studio** (for Android emulator)
- **Xcode** (for iOS simulator - Mac only)
- **Expo Go** app on your phone

---

## ⚡ Quick Setup (5 Minutes)

### 1. Database Setup

```sql
-- Open MariaDB command line or HeidiSQL
CREATE DATABASE test_advj;
USE test_advj;

-- Tables will be auto-created by Spring Boot
```

### 2. Backend Setup

```bash
# Navigate to backend folder
cd mindaro-backend

# Run the backend (Windows)
gradlew.bat bootRun

# Or (Linux/Mac)
./gradlew bootRun

# Backend will start on http://localhost:8080
```

**Verify Backend:**
- Open browser: http://localhost:8080/api/mentors
- Should see JSON response (empty array or data)

### 3. Frontend Setup

```bash
# Open new terminal
# Navigate to frontend folder
cd MyAppName

# Install dependencies (first time only)
npm install

# Start Expo
npx expo start

# Or
npm start
```

**Run on Device:**
- Press `a` for Android emulator
- Press `i` for iOS simulator (Mac only)
- Scan QR code with Expo Go app on phone

---

## 🎯 First Time Usage

### Test User Login
1. Open app
2. Go to "Sign In"
3. Enter any email/password (auto-registration enabled)
4. You're logged in! 🎉

### Test Mentor Login
1. Open app
2. Go to "Sign In as Mentor"
3. Enter any email/password
4. Access mentor dashboard

---

## 📁 Project Structure Overview

```
ADVIJR/
├── MyAppName/                    # Frontend (React Native)
│   ├── app/                      # All screens
│   │   ├── (tabs)/              # User app tabs
│   │   ├── auth/                # Login/signup screens
│   │   └── mentor/              # Mentor app screens
│   ├── services/                # API calls
│   ├── constants/               # Colors, config
│   └── components/              # Reusable UI
│
└── mindaro-backend/             # Backend (Spring Boot)
    ├── src/main/java/           # Java code
    │   └── .../mindarobackend/
    │       ├── controller/      # API endpoints
    │       ├── service/         # Business logic
    │       ├── repository/      # Database
    │       └── model/           # Data models
    └── src/main/resources/
        └── application.properties  # Config
```

---

## 🎨 Customization Guide

### Change App Colors

**File:** `MyAppName/constants/Colors.ts`

```typescript
export const Colors = {
  primary: '#0052CC',        // Change this
  secondary: '#FF8C42',      // And this
  // ... rest of colors
};
```

Colors automatically apply throughout the app!

### Change App Name

**File:** `MyAppName/app.json`

```json
{
  "name": "YourAppName",
  "displayName": "Your App Display Name",
  "version": "1.0.0"
}
```

### Change API URL

**File:** `MyAppName/services/apiService.ts`

```typescript
const API_BASE_URL = 'http://your-server.com:8080/api';
```

### Change Database

**File:** `mindaro-backend/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## 🔧 Common Tasks

### Add New User Screen

1. Create file: `MyAppName/app/(tabs)/newscreen.tsx`
2. Copy template from existing screen
3. Update navigation: `MyAppName/app/(tabs)/_layout.tsx`

```typescript
<Tabs.Screen
  name="newscreen"
  options={{
    title: 'New Screen',
    tabBarIcon: ({ color }) => <Ionicons name="star" size={24} color={color} />,
  }}
/>
```

### Add New API Endpoint

**Backend:** `mindaro-backend/src/main/java/.../controller/YourController.java`

```java
@RestController
@RequestMapping("/api/your-endpoint")
public class YourController {
    
    @GetMapping
    public ResponseEntity<?> getData() {
        // Your logic
        return ResponseEntity.ok(data);
    }
}
```

**Frontend:** `MyAppName/services/apiService.ts`

```typescript
async getData() {
  const response = await fetch(`${API_BASE_URL}/your-endpoint`);
  return await response.json();
}
```

### Add New Database Table

**Create Model:** `mindaro-backend/src/main/java/.../model/YourModel.java`

```java
@Entity
@Table(name = "your_table")
public class YourModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    // ... getters and setters
}
```

**Create Repository:** `mindaro-backend/src/main/java/.../repository/YourRepository.java`

```java
public interface YourRepository extends JpaRepository<YourModel, Long> {
    // Custom queries
}
```

Table auto-creates on next backend run!

---

## 🐛 Troubleshooting

### Backend Won't Start

**Problem:** Port 8080 already in use
```bash
# Windows: Find and kill process
netstat -ano | findstr :8080
taskkill /PID <process_id> /F

# Or change port in application.properties
server.port=8081
```

**Problem:** Database connection failed
- Check MariaDB is running
- Verify credentials in `application.properties`
- Ensure database `test_advj` exists

### Frontend Won't Start

**Problem:** Module not found
```bash
# Clear cache and reinstall
cd MyAppName
rm -rf node_modules
npm install
npx expo start -c
```

**Problem:** Can't connect to backend
- Ensure backend is running on port 8080
- Check API_BASE_URL in `apiService.ts`
- For physical device, use computer's IP instead of localhost

### App Crashes on Login

**Problem:** API endpoint not found
- Check backend is running
- Verify endpoint exists in controller
- Check browser: http://localhost:8080/api/mentors

---

## 📱 Testing on Real Device

### Android
1. Install Expo Go from Play Store
2. Ensure phone and computer on same WiFi
3. Scan QR code from Expo terminal

### iOS
1. Install Expo Go from App Store
2. Ensure phone and computer on same WiFi
3. Scan QR code from Camera app

### Update API URL for Device
```typescript
// Use your computer's IP address
const API_BASE_URL = 'http://192.168.1.100:8080/api';
```

Find your IP:
- Windows: `ipconfig`
- Mac/Linux: `ifconfig`

---

## 🚀 Deployment

### Frontend (Expo)

```bash
cd MyAppName

# Build for Android
eas build --platform android

# Build for iOS (Mac only)
eas build --platform ios

# Publish update
expo publish
```

### Backend (Spring Boot)

```bash
cd mindaro-backend

# Create JAR file
./gradlew build

# Run JAR
java -jar build/libs/mindaro-backend-0.0.1-SNAPSHOT.jar

# Or deploy to cloud (Heroku, AWS, etc.)
```

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `PROJECT_STRUCTURE_GUIDE.md` | Complete file structure & what each file does |
| `GIT_WORKFLOW_GUIDE.md` | How to use Git & push to GitHub |
| `COLOR_MIGRATION_COMPLETE.md` | Color scheme documentation |
| `DEPLOYMENT.md` | Production deployment guide |
| `README.md` | Project overview |

---

## 🎓 Learning Path

### Day 1: Setup & Exploration
1. ✅ Install prerequisites
2. ✅ Setup database
3. ✅ Run backend
4. ✅ Run frontend
5. ✅ Test login

### Day 2: Understanding Structure
1. 📖 Read `PROJECT_STRUCTURE_GUIDE.md`
2. 🔍 Explore user app screens
3. 🔍 Explore mentor app screens
4. 🔍 Check backend controllers

### Day 3: Make First Changes
1. 🎨 Change app colors
2. ✏️ Update app name
3. ➕ Add new screen
4. 💾 Commit to Git

### Day 4: Backend Development
1. 📊 Add new API endpoint
2. 🗄️ Create new database table
3. 🔗 Connect frontend to new API
4. ✅ Test end-to-end

### Week 2: Advanced Features
1. 🔐 Implement proper authentication
2. 📱 Add push notifications
3. 💳 Integrate payment gateway
4. 📊 Add analytics

---

## 🆘 Getting Help

### Check Documentation
1. Read relevant `.md` file
2. Check code comments
3. Review similar existing features

### Debug Steps
1. Check console logs (frontend & backend)
2. Verify API endpoints in browser
3. Check database tables
4. Test with Postman/Insomnia

### Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Can't login | Check backend is running, verify API URL |
| Database error | Check MariaDB running, verify credentials |
| Build failed | Clear cache, reinstall dependencies |
| API not found | Check controller path matches frontend call |
| Colors not updating | Clear Expo cache: `npx expo start -c` |

---

## 🎯 Next Steps

### Immediate (Week 1)
- [ ] Complete setup
- [ ] Test all features
- [ ] Read documentation
- [ ] Make first customization

### Short Term (Month 1)
- [ ] Customize branding
- [ ] Add new features
- [ ] Implement proper auth
- [ ] Setup Git repository

### Long Term (Month 2-3)
- [ ] Production deployment
- [ ] Payment integration
- [ ] Push notifications
- [ ] Analytics & monitoring

---

## 📞 Support

### Resources
- **Documentation**: All `.md` files in project root
- **Code Examples**: Check existing screens/controllers
- **API Testing**: Use Postman or browser

### Best Practices
- ✅ Commit often with clear messages
- ✅ Test before committing
- ✅ Read error messages carefully
- ✅ Keep dependencies updated
- ✅ Document your changes

---

## 🎉 You're Ready!

You now have:
- ✅ Working frontend & backend
- ✅ Complete documentation
- ✅ Understanding of structure
- ✅ Ability to make changes
- ✅ Git workflow knowledge

**Start building amazing features! 🚀**

---

**Quick Links:**
- 📖 [Full Structure Guide](PROJECT_STRUCTURE_GUIDE.md)
- 🔧 [Git Workflow](GIT_WORKFLOW_GUIDE.md)
- 🎨 [Color Documentation](COLOR_MIGRATION_COMPLETE.md)
- 🚀 [Deployment Guide](DEPLOYMENT.md)

**Version:** 1.0.0  
**Last Updated:** After complete ADVIJR branding migration  
**Status:** Production Ready ✅
