@echo off
echo Installing Video Call Dependencies...
echo.

echo Installing react-native-webrtc...
npm install react-native-webrtc

echo Installing Firebase dependencies...
npm install @react-native-firebase/app @react-native-firebase/firestore

echo Installing permissions package...
npm install react-native-permissions

echo.
echo Dependencies installed successfully!
echo.
echo Next steps:
echo 1. Configure iOS permissions in ios/Info.plist
echo 2. Configure Android permissions in android/app/src/main/AndroidManifest.xml
echo 3. Set up Firebase configuration
echo 4. Test on physical devices (video calls don't work well in simulators)
echo.
echo See video-call-dependencies.md for detailed setup instructions.
pause