import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { DataService } from 'src/app/data.service';
import { User } from 'src/app/dataClass';
import { AuthService } from '../auth.service';
import * as bcrypt from 'bcryptjs';
import { Exceptions, ReturnMessages } from 'src/app/data.model';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent {
  constructor(
    public fb: FormBuilder,
    private _router: Router,
    private _auth: AuthService,
    public dataService: DataService
  ) {}

  teamNameList = this.dataService.teamListForSignUp;
  teamNameFromDropDown = this.teamNameList[0];

  roleList = this.dataService.roleListForSignUp;
  roleFromDropDown = this.roleList[0];

  userForm: FormGroup = new FormGroup({
    firstname: new FormControl('', Validators.required),
    lastname: new FormControl(''),
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    team: new FormControl('', Validators.required),
    role: new FormControl('', Validators.required),
    otp: new FormControl('', Validators.required),
  });

  newUser: User = new User();
  submitted = false;
  message = '';
  emailPattern = this.dataService.emailPattern;

  ngOnInit(): void {}

  onSignup(signupForm: FormGroup) {
    if (signupForm.invalid) {
      return;
    }

    if (signupForm.value.otp.includes(' ')) {
      this.dataService.openSnackBarWithDuration(
        'Please remove extra space from OTP',
        'Close',
        this.dataService.snackbarduration
      );
      return;
    }
    signupForm.value.password = bcrypt.hashSync(signupForm.value.password, 10);
    this.signupUser(signupForm.value);
  }

  signupUser(newUser: User) {
    this._auth.signupUser(newUser).subscribe({
      next: (response: string) => {
        if (response === Exceptions.OTPMismatchFound) {
          this.dataService.openSnackBarWithDuration(
            'Please enter correct OTP',
            'Close',
            this.dataService.snackbarduration
          );
        } else if (response === Exceptions.UserAlreadyExist) {
          this.dataService.openSnackBarWithDuration(
            'User ' + newUser.username + ' already exists',
            'Close',
            this.dataService.snackbarduration
          );
        } else if (response === ReturnMessages.SUCCESS) {
          this.dataService.openSnackBarWithDuration(
            'User added successfully. Please check your mailbox',
            'Close',
            this.dataService.snackbarduration
          );
          this._router.navigate(['/login']);
        } else {
          this.dataService.openSnackBarWithDuration(
            'User not added. Please contact administrator',
            'Close',
            this.dataService.snackbarduration
          );
        }
      },
      error: (err) => {
        this.dataService.openSnackBarWithDuration(
          'Signup error : ' + err,
          'Close',
          this.dataService.snackbarduration
        );
      },
    });
  }

  GetOTP(signupForm: FormGroup) {
    if (signupForm.value.username.length === 0) {
      this.dataService.openSnackBarWithDuration(
        'Enter valid username for OTP',
        'Close',
        this.dataService.snackbarduration
      );
      return;
    }

    this._auth.getOTP(signupForm.value.username).subscribe({
      next: (response: any) => {
        if (response === 0) {
          this.dataService.openSnackBarWithDuration(
            'OTP not generated. Please contact administrator',
            'Close',
            this.dataService.snackbarduration
          );
        } else {
          this.dataService.openSnackBarWithDuration(
            'OTP generated successfully. Please check your mailbox : ' +
              response,
            'Close',
            this.dataService.snackbarduration
          );
        }
      },
      error: (err) => {
        if (err.status === 404) {
          this.dataService.openSnackBarWithDuration(
            'Invalid username',
            'Close',
            this.dataService.snackbarduration
          );
        } else {
          this.dataService.openSnackBarWithDuration(
            'Error : ' + err.errorMessage,
            'Close',
            this.dataService.snackbarduration
          );
        }
      },
    });
  }
}
