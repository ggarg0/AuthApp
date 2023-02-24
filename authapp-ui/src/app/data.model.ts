export enum Role {
  Admin = 'Admin',
  Manager = 'Manager',
  Developer = 'Developer',
}

export enum ReturnMessages {
  SUCCESS = 'SUCCESS',
  FAILED = 'FAILED',
  YES = 'YES',
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
