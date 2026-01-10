// Mock SignalingService until react-native-webrtc is installed
// This prevents import errors while the dependencies are being set up

export type StreamStateCallback = (stream: any) => void;
export type IceConnectionStateCallback = (state: any) => void;
export type ConnectionStateCallback = (state: any) => void;

export interface MediaStatus {
  isCameraOn?: boolean;
  isMicOn?: boolean;
  updatedAt: any;
  userType: string;
}

export class SignalingService {
  public onAddRemoteStream: StreamStateCallback | null = null;
  public onAddIceConnectionStream: IceConnectionStateCallback | null = null;
  public onAddConnectionStream: ConnectionStateCallback | null = null;

  async createRoom(
    roomId: string,
    localStream: any,
    remoteStream: any,
    isVideo: boolean
  ): Promise<string> {
    console.log('Mock: Creating room', roomId);
    return roomId;
  }

  async joinRoom(
    roomId: string,
    localStream: any,
    remoteStream: any,
    isVideo: boolean
  ): Promise<void> {
    console.log('Mock: Joining room', roomId);
  }

  async hangUp(
    roomId: string,
    localStream: any,
    remoteStream: any,
    isVideo: boolean,
    endedAt?: any,
    duration?: string
  ): Promise<void> {
    console.log('Mock: Hanging up', roomId);
  }

  async createCallEntry(
    roomId: string,
    callerId: string,
    calleeId: string,
    callType: string
  ): Promise<string> {
    console.log('Mock: Creating call entry', roomId);
    return roomId;
  }

  async updateMediaStatusInCall(
    roomId: string,
    userType: string,
    isCameraOn?: boolean,
    isMicOn?: boolean,
    callerId?: string,
    calleeId?: string
  ): Promise<void> {
    console.log('Mock: Updating media status', roomId);
  }

  listenToStatus(
    roomId: string,
    userType: string,
    callerId?: string,
    calleeId?: string
  ): Promise<() => void> {
    console.log('Mock: Listening to status', roomId);
    return Promise.resolve(() => {});
  }

  async getUserMedia(isVideo: boolean = true): Promise<any> {
    console.log('Mock: Getting user media', isVideo);
    return null;
  }

  async switchCamera(): Promise<void> {
    console.log('Mock: Switching camera');
  }

  toggleAudio(enabled: boolean): void {
    console.log('Mock: Toggle audio', enabled);
  }

  toggleVideo(enabled: boolean): void {
    console.log('Mock: Toggle video', enabled);
  }

  dispose(): void {
    console.log('Mock: Disposing signaling service');
    this.onAddRemoteStream = null;
    this.onAddIceConnectionStream = null;
    this.onAddConnectionStream = null;
  }
}

// Export singleton instance
export const signalingService = new SignalingService();