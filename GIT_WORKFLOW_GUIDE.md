# Git Workflow Guide for ADVIJR Project

## 🚀 Quick Start - Push Your Code to GitHub

### Step 1: Initialize Git Repository (First Time Only)

```bash
# Navigate to your project root
cd C:\path\to\your\ADVIJR

# Initialize git repository
git init

# Configure your identity
git config user.name "Your Name"
git config user.email "your.email@example.com"
```

### Step 2: Create .gitignore (If Not Exists)

The project should already have `.gitignore` files, but verify they include:

**Root `.gitignore`:**
```
# Node modules
node_modules/
MyAppName/node_modules/

# Build outputs
MyAppName/.expo/
MyAppName/dist/
mindaro-backend/build/
mindaro-backend/.gradle/

# Environment files
.env
.env.local
*.env

# IDE files
.vscode/
.idea/
*.iml

# OS files
.DS_Store
Thumbs.db

# Database files
*.db
*.lock.db
mindaro-backend/data/

# Logs
*.log
npm-debug.log*
```

### Step 3: Create GitHub Repository

1. Go to https://github.com
2. Click "New Repository" (+ icon, top right)
3. Repository name: `advijr` or `advijr-app`
4. Description: "ADVIJR - Service Consultation Platform"
5. Choose: Public or Private
6. **DO NOT** initialize with README (we already have one)
7. Click "Create repository"

### Step 4: Connect Local Repository to GitHub

```bash
# Add remote repository (replace with your GitHub URL)
git remote add origin https://github.com/yourusername/advijr.git

# Verify remote was added
git remote -v
```

### Step 5: Stage All Files

```bash
# Check what files will be committed
git status

# Add all files to staging
git add .

# Or add specific directories
git add MyAppName/
git add mindaro-backend/
git add *.md
```

### Step 6: Create Initial Commit

```bash
# Commit with descriptive message
git commit -m "Initial commit: ADVIJR service consultation platform

- Complete React Native frontend with user and mentor apps
- Spring Boot backend with REST API
- MariaDB database integration
- Authentication system for users and mentors
- Chat, video call, and booking features
- Wallet and billing system
- Updated branding to ADVIJR colors (blue/orange)"
```

### Step 7: Push to GitHub

```bash
# Push to main branch
git push -u origin main

# If you get an error about 'master' vs 'main', try:
git branch -M main
git push -u origin main
```

---

## 📋 Daily Workflow

### Making Changes and Pushing

```bash
# 1. Check current status
git status

# 2. See what changed
git diff

# 3. Add changes
git add .

# 4. Commit with message
git commit -m "feat: add new feature description"

# 5. Push to GitHub
git push origin main
```

---

## 📝 Commit Message Guidelines

### Format
```
<type>: <short description>

[optional detailed description]

[optional footer]
```

### Types

| Type | When to Use | Example |
|------|-------------|---------|
| `feat` | New feature | `feat: add video call recording` |
| `fix` | Bug fix | `fix: resolve login timeout issue` |
| `ui` | UI/UX changes | `ui: update button colors to match brand` |
| `style` | Code style (formatting) | `style: format code with prettier` |
| `refactor` | Code refactoring | `refactor: simplify authentication logic` |
| `perf` | Performance improvement | `perf: optimize database queries` |
| `test` | Add/update tests | `test: add unit tests for wallet service` |
| `docs` | Documentation | `docs: update API documentation` |
| `build` | Build system changes | `build: update gradle dependencies` |
| `ci` | CI/CD changes | `ci: add GitHub Actions workflow` |
| `chore` | Maintenance tasks | `chore: update dependencies` |
| `backend` | Backend changes | `backend: add mentor authentication endpoint` |
| `frontend` | Frontend changes | `frontend: redesign home screen` |

### Examples

```bash
# Feature addition
git commit -m "feat: implement mentor broadcast messaging"

# Bug fix
git commit -m "fix: correct wallet balance calculation error"

# UI update
git commit -m "ui: migrate all colors from yellow to blue/orange branding"

# Backend change
git commit -m "backend: add pagination to chat messages API"

# Multiple changes
git commit -m "feat: complete color migration to ADVIJR branding

- Updated all user app screens
- Updated all mentor app screens
- Created Colors.ts constants file
- Updated gradients and icons
- Changed yellow/gold to blue/orange theme"
```

---

## 🌿 Branching Strategy

### Main Branch
- `main` - Production-ready code

### Feature Branches
```bash
# Create new feature branch
git checkout -b feature/video-call-recording

# Work on feature...
git add .
git commit -m "feat: add video call recording"

# Push feature branch
git push origin feature/video-call-recording

# Merge to main (after testing)
git checkout main
git merge feature/video-call-recording
git push origin main

# Delete feature branch
git branch -d feature/video-call-recording
git push origin --delete feature/video-call-recording
```

### Branch Naming Convention
- `feature/` - New features: `feature/mentor-analytics`
- `fix/` - Bug fixes: `fix/login-error`
- `ui/` - UI changes: `ui/redesign-profile`
- `backend/` - Backend work: `backend/add-payment-gateway`
- `docs/` - Documentation: `docs/api-guide`

---

## 🔄 Common Git Operations

### Checking Status
```bash
# See modified files
git status

# See detailed changes
git diff

# See changes in specific file
git diff MyAppName/app/(tabs)/home.tsx
```

### Viewing History
```bash
# View commit history
git log

# Compact view
git log --oneline

# Last 5 commits
git log -5

# With file changes
git log --stat

# Graphical view
git log --graph --oneline --all
```

### Undoing Changes

```bash
# Discard changes in specific file (before staging)
git checkout -- MyAppName/app/(tabs)/home.tsx

# Unstage file (after git add)
git reset HEAD MyAppName/app/(tabs)/home.tsx

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Undo last commit (discard changes) - CAREFUL!
git reset --hard HEAD~1

# Amend last commit message
git commit --amend -m "New commit message"

# Amend last commit (add forgotten files)
git add forgotten-file.tsx
git commit --amend --no-edit
```

### Pulling Updates
```bash
# Pull latest changes from GitHub
git pull origin main

# Pull and rebase (cleaner history)
git pull --rebase origin main
```

### Stashing Changes
```bash
# Save work in progress
git stash

# List stashes
git stash list

# Apply most recent stash
git stash apply

# Apply and remove stash
git stash pop

# Stash with message
git stash save "WIP: working on video call feature"
```

---

## 📦 Specific File Workflows

### After Updating Colors
```bash
git add MyAppName/constants/Colors.ts
git add MyAppName/app/(tabs)/*.tsx
git add MyAppName/app/mentor/(tabs)/*.tsx
git commit -m "ui: complete color migration to ADVIJR branding"
git push origin main
```

### After Backend Changes
```bash
git add mindaro-backend/src/main/java/com/dekhokaun/mindarobackend/
git add mindaro-backend/src/main/resources/application.properties
git commit -m "backend: update database configuration and add new endpoints"
git push origin main
```

### After Adding New Feature
```bash
# Add all related files
git add MyAppName/app/new-feature.tsx
git add MyAppName/services/newFeatureService.ts
git add mindaro-backend/src/main/java/.../NewFeatureController.java

git commit -m "feat: implement new feature with frontend and backend

- Added new screen in frontend
- Created API service
- Implemented backend controller and service
- Updated navigation"

git push origin main
```

---

## 🔍 Checking What to Commit

### See All Changes
```bash
# List all modified files
git status

# See line-by-line changes
git diff

# See changes in staged files
git diff --staged
```

### Selective Staging
```bash
# Add specific files
git add MyAppName/app/(tabs)/home.tsx
git add MyAppName/constants/Colors.ts

# Add all files in directory
git add MyAppName/app/(tabs)/

# Add all TypeScript files
git add *.tsx

# Add all files with pattern
git add MyAppName/**/*.tsx

# Interactive staging (choose what to stage)
git add -p
```

---

## 🚨 Troubleshooting

### Problem: "fatal: not a git repository"
```bash
# Solution: Initialize git
git init
```

### Problem: "remote origin already exists"
```bash
# Solution: Remove and re-add
git remote remove origin
git remote add origin https://github.com/yourusername/advijr.git
```

### Problem: "failed to push some refs"
```bash
# Solution: Pull first, then push
git pull origin main --rebase
git push origin main
```

### Problem: Merge Conflicts
```bash
# 1. Pull latest changes
git pull origin main

# 2. Git will mark conflicts in files
# 3. Open conflicted files and resolve manually
# 4. Look for markers:
#    <<<<<<< HEAD
#    Your changes
#    =======
#    Their changes
#    >>>>>>> branch-name

# 5. After resolving, stage and commit
git add .
git commit -m "fix: resolve merge conflicts"
git push origin main
```

### Problem: Accidentally Committed Sensitive Data
```bash
# Remove file from git but keep locally
git rm --cached sensitive-file.txt
echo "sensitive-file.txt" >> .gitignore
git commit -m "chore: remove sensitive file from git"
git push origin main
```

### Problem: Want to Ignore Already Tracked Files
```bash
# Remove from git tracking
git rm --cached MyAppName/.env
git rm --cached -r mindaro-backend/data/

# Add to .gitignore
echo "MyAppName/.env" >> .gitignore
echo "mindaro-backend/data/" >> .gitignore

# Commit
git commit -m "chore: stop tracking environment and data files"
git push origin main
```

---

## 📊 Project-Specific Workflows

### Complete Feature Development Cycle

```bash
# 1. Create feature branch
git checkout -b feature/mentor-analytics

# 2. Make changes to files
# ... edit files ...

# 3. Test locally
cd MyAppName && npx expo start
cd mindaro-backend && ./gradlew bootRun

# 4. Stage changes
git add .

# 5. Commit
git commit -m "feat: add mentor analytics dashboard

- Created analytics screen with charts
- Added backend API endpoints
- Implemented data aggregation service
- Updated mentor navigation"

# 6. Push feature branch
git push origin feature/mentor-analytics

# 7. Create Pull Request on GitHub (optional)
# Or merge directly:
git checkout main
git merge feature/mentor-analytics

# 8. Push to main
git push origin main

# 9. Delete feature branch
git branch -d feature/mentor-analytics
git push origin --delete feature/mentor-analytics
```

### Hotfix Workflow (Urgent Bug Fix)

```bash
# 1. Create hotfix branch from main
git checkout main
git checkout -b hotfix/critical-login-bug

# 2. Fix the bug
# ... edit files ...

# 3. Test fix
# ... test ...

# 4. Commit
git add .
git commit -m "fix: resolve critical login authentication bug"

# 5. Merge to main
git checkout main
git merge hotfix/critical-login-bug

# 6. Push
git push origin main

# 7. Delete hotfix branch
git branch -d hotfix/critical-login-bug
```

---

## 📁 Files to Always Commit

### ✅ Should Commit
- Source code (`.tsx`, `.ts`, `.java`)
- Configuration files (`app.json`, `package.json`, `build.gradle`)
- Documentation (`.md` files)
- Assets (images, icons - if not too large)
- `.gitignore` file

### ❌ Never Commit
- `node_modules/` folder
- `.env` files with secrets
- Database files (`.db`, `.lock.db`)
- Build outputs (`build/`, `dist/`, `.expo/`)
- IDE settings (`.vscode/`, `.idea/`)
- Log files (`*.log`)
- OS files (`.DS_Store`, `Thumbs.db`)

---

## 🎯 Quick Command Reference

```bash
# Setup
git init                                    # Initialize repository
git remote add origin <url>                 # Add remote
git clone <url>                             # Clone repository

# Daily workflow
git status                                  # Check status
git add .                                   # Stage all changes
git commit -m "message"                     # Commit changes
git push origin main                        # Push to GitHub
git pull origin main                        # Pull from GitHub

# Branching
git branch                                  # List branches
git branch <name>                           # Create branch
git checkout <name>                         # Switch branch
git checkout -b <name>                      # Create and switch
git merge <branch>                          # Merge branch
git branch -d <name>                        # Delete branch

# History
git log                                     # View history
git log --oneline                           # Compact history
git diff                                    # See changes

# Undo
git checkout -- <file>                      # Discard changes
git reset HEAD <file>                       # Unstage file
git reset --soft HEAD~1                     # Undo commit (keep changes)
git stash                                   # Save work in progress
```

---

## 🔐 GitHub Authentication

### Using HTTPS (Recommended for Windows)
```bash
# When pushing, you'll be prompted for credentials
# Use Personal Access Token instead of password

# Generate token:
# 1. GitHub → Settings → Developer settings → Personal access tokens
# 2. Generate new token (classic)
# 3. Select scopes: repo, workflow
# 4. Copy token and use as password
```

### Using SSH (Alternative)
```bash
# Generate SSH key
ssh-keygen -t ed25519 -C "your.email@example.com"

# Add to SSH agent
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519

# Add public key to GitHub
# Copy: cat ~/.ssh/id_ed25519.pub
# GitHub → Settings → SSH and GPG keys → New SSH key

# Use SSH URL
git remote set-url origin git@github.com:yourusername/advijr.git
```

---

## 📚 Additional Resources

- [Git Documentation](https://git-scm.com/doc)
- [GitHub Guides](https://guides.github.com/)
- [Git Cheat Sheet](https://education.github.com/git-cheat-sheet-education.pdf)

---

**Remember**: 
- Commit often with clear messages
- Pull before you push
- Test before you commit
- Never commit sensitive data
- Use branches for features

**Happy Coding! 🚀**
