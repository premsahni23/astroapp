# 🚀 Deployment Guide

This guide covers deploying the Mindaro astrology consultation app to production.

## 📋 Prerequisites

- Java 17+
- Node.js 18+
- MariaDB 10.5+
- Domain name (optional)
- SSL certificate (for production)

## 🗄️ Database Setup

### 1. Create Production Database

```sql
CREATE DATABASE mindaro_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'mindaro_user'@'%' IDENTIFIED BY 'secure_password_here';
GRANT ALL PRIVILEGES ON mindaro_db.* TO 'mindaro_user'@'%';
FLUSH PRIVILEGES;
```

### 2. Environment Variables

Create a `.env` file for the backend:

```bash
DATABASE_URL=jdbc:mariadb://localhost:3306/mindaro_db
DATABASE_USERNAME=mindaro_user
DATABASE_PASSWORD=secure_password_here
SERVER_PORT=3000
```

## 🖥️ Backend Deployment

### Option 1: JAR Deployment

1. **Build the application:**
   ```bash
   cd mindaro-backend
   ./gradlew clean build
   ```

2. **Run the JAR:**
   ```bash
   java -jar build/libs/mindaro-backend-1.0.jar
   ```

### Option 2: Docker Deployment

1. **Create Dockerfile in `mindaro-backend/`:**
   ```dockerfile
   FROM openjdk:17-jdk-slim
   
   WORKDIR /app
   COPY build/libs/mindaro-backend-1.0.jar app.jar
   
   EXPOSE 3000
   
   CMD ["java", "-jar", "app.jar"]
   ```

2. **Build and run:**
   ```bash
   docker build -t mindaro-backend .
   docker run -p 3000:3000 -e DATABASE_URL=your_db_url mindaro-backend
   ```

### Option 3: Cloud Deployment (AWS/GCP/Azure)

Upload the JAR file to your cloud provider and configure environment variables.

## 📱 Frontend Deployment

### Web Deployment

1. **Build for web:**
   ```bash
   cd MyAppName
   npx expo export:web
   ```

2. **Deploy to hosting service:**
   - Netlify: Drag and drop `dist/` folder
   - Vercel: Connect GitHub repo
   - AWS S3: Upload to S3 bucket with static hosting

### Mobile App Deployment

#### iOS App Store

1. **Build iOS app:**
   ```bash
   npx expo build:ios
   ```

2. **Submit to App Store Connect**

#### Google Play Store

1. **Build Android app:**
   ```bash
   npx expo build:android
   ```

2. **Upload to Google Play Console**

## 🔧 Production Configuration

### Backend (`application.properties`)

```properties
# Production database
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}

# Production server settings
server.port=${SERVER_PORT:3000}
server.address=0.0.0.0

# Security settings
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Logging
logging.level.org.springframework.web=WARN
logging.level.com.dekhokaun.mindarobackend=INFO
```

### Frontend (`config/auth.ts`)

```typescript
const getBaseUrl = () => {
  if (Platform.OS === 'web') {
    return 'https://api.yourdomain.com'; // Your production API URL
  } else {
    return 'https://api.yourdomain.com'; // Same for mobile
  }
};
```

## 🔒 Security Checklist

- [ ] Use HTTPS in production
- [ ] Set strong database passwords
- [ ] Configure CORS properly
- [ ] Enable rate limiting
- [ ] Set up monitoring and logging
- [ ] Regular security updates
- [ ] Backup database regularly

## 📊 Monitoring

### Health Check Endpoint

The backend provides a health check at `/actuator/health`

### Logging

Configure logging levels in `application.properties`:

```properties
logging.level.com.dekhokaun.mindarobackend=INFO
logging.file.name=logs/mindaro.log
```

## 🔄 CI/CD Pipeline

### GitHub Actions Example

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  deploy-backend:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Build with Gradle
      run: |
        cd mindaro-backend
        ./gradlew build
    - name: Deploy to server
      # Add your deployment steps here
      
  deploy-frontend:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Setup Node.js
      uses: actions/setup-node@v2
      with:
        node-version: '18'
    - name: Install dependencies
      run: |
        cd MyAppName
        npm install
    - name: Build for web
      run: |
        cd MyAppName
        npx expo export:web
    - name: Deploy to hosting
      # Add your deployment steps here
```

## 🆘 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check database credentials
   - Verify database server is running
   - Check firewall settings

2. **CORS Errors**
   - Verify CORS configuration in backend
   - Check frontend API URL configuration

3. **Mobile App Not Connecting**
   - Ensure backend is accessible from mobile network
   - Check if using HTTPS in production

### Logs Location

- Backend logs: `logs/mindaro.log`
- Frontend logs: Browser console or React Native debugger

## 📞 Support

For deployment support:
- 📧 Email: devops@mindaro.com
- 📖 Documentation: [docs.mindaro.com/deployment](https://docs.mindaro.com/deployment)

---

**Happy Deploying! 🚀**