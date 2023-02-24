export enum Role {
  Admin = 'Admin',
  Developer = 'Developer',
}

export enum ReturnMessages {
  SUCCESS = 'Success',
  FAILED = 'Failed',
  YES = 'Yes',
  No = 'No',
}

export enum Exceptions {
  UserNotFound = 'User details not found',
  JWTUsernameMismatchFound = 'User name mismatch found in token',
  OTPMismatchFound = 'OTP mismatch found',
  UserAlreadyExist = 'User already exist',
  InvalidToken = 'Invalid token found',
  TokenExpired = 'Token already expired',
}

export interface UserDetails {
  checked: boolean;
  firstname: string;
  lastname: string;
  username: string;
  password: string;
  team: string;
  role: string;
  approved: string;
  active: string;
  otp: string;
  message: string;
}
