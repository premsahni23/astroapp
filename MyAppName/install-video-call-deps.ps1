Write-Host "Installing Video Call Dependencies..." -ForegroundColor Green
Write-Host ""

Write-Host "Installing react-native-webrtc..." -ForegroundColor Yellow
npm install react-native-webrtc

Write-Host "Installing Firebase dependencies..." -ForegroundColor Yellow
npm install @react-native-firebase/app @react-native-firebase/firestore

Write-Host "Installing permissions package..." -ForegroundColor Yellow
npm install react-native-permissions

Write-Host ""
Write-Host "Dependencies installed successfully!" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Configure iOS permissions in ios/Info.plist" -ForegroundColor White
Write-Host "2. Configure Android permissions in android/app/src/main/AndroidManifest.xml" -ForegroundColor White
Write-Host "3. Set up Firebase configuration" -ForegroundColor White
Write-Host "4. Test on physical devices (video calls don't work well in simulators)" -ForegroundColor White
Write-Host ""
Write-Host "See video-call-dependencies.md for detailed setup instructions." -ForegroundColor Cyan
Read-Host "Press Enter to continue"