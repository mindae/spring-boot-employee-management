# Disable GitHub Email Notifications

## Quick Steps to Stop Email Spam

### Method 1: Repository Settings
1. Go to your repository: `https://github.com/mindae/spring-boot-employee-management`
2. Click **Settings** (top right)
3. Go to **Notifications** (left sidebar)
4. Uncheck **"Email"** notifications
5. Click **Save**

### Method 2: GitHub Account Settings
1. Go to GitHub Settings: `https://github.com/settings/notifications`
2. Under **"Email notifications"**
3. Uncheck **"Actions"** and **"Workflows"**
4. Or set to **"Only participating"** instead of **"All activity"**

### Method 3: Repository Watch Settings
1. Go to your repository
2. Click **"Watch"** button (top right)
3. Select **"Ignore"** or **"Not watching"**
4. This will stop all notifications for this repository

## Why This Happens
- GitHub sends emails for every workflow run
- Every push triggers CI/CD workflows
- Demo projects don't need constant email updates

## Result
- ✅ No more email spam
- ✅ Still get GitHub notifications in the web interface
- ✅ Can still monitor workflow status on GitHub
- ✅ Personal email stays clean

---
*This is a demo project for learning purposes*
