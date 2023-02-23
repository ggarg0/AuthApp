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

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['../../app.component.css'],
})
export class ResetPasswordComponent {
  constructor(
    private _router: Router,
    private _auth: AuthService,
    public fb: FormBuilder,
    public dataService: DataService
  ) {}

  userForm: FormGroup = new FormGroup({
    username: new FormControl('', Validators.required),
    otp: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  newUser: User = new User();
  submitted = false;
  emailPattern = this.dataService.emailPattern;

  ngOnInit(): void {}

  resetForm(resetForm: any) {
    if (
      resetForm.value.username.length === 0 &&
      resetForm.value.otp.length === 0 &&
      resetForm.value.password.length === 0
    ) {
      return;
    }

    if (resetForm.value.otp.includes(' ')) {
      this.dataService.openSnackBarWithDuration(
        'Please remove extra space from OTP',
        'Close',
        this.dataService.snackbarduration
      );
      return;
    }

    resetForm.value.password = bcrypt.hashSync(resetForm.value.password, 10);
    this.resetUserPassword(resetForm.value);
  }

  GetOTP(resetForm: any) {
    this._auth.getOTP(resetForm.value.username).subscribe({
      next: (response) => {
        if (response === '') {
          this.dataService.openSnackBarWithDuration(
            'OTP not generated. Please contact administrator',
            'Close',
            this.dataService.snackbarduration
          );
        } else {
          this.dataService.openSnackBarWithDuration(
            'OTP generated successfully. Please check your mailbox',
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


  GetOTP1(resetForm: any) {
    const res = [];
    this._auth.getOTP(resetForm.value.username).subscribe((res)=>{
      console.log(res);
  });
  }

  resetUserPassword(resetUser: User) {
    this._auth.resetUserPassword(resetUser).subscribe(
      (res) => {
        if (res === 2) {
          this.dataService.openSnackBarWithDuration(
            'Please enter correct OTP',
            'Close',
            this.dataService.snackbarduration
          );
        } else if (res === 3) {
          this.dataService.openSnackBarWithDuration(
            'User not found. Please use signup to create account',
            'Close',
            this.dataService.snackbarduration
          );
        } else if (res === 1) {
          this.dataService.openSnackBarWithDuration(
            'User password reset successfully',
            'Close',
            this.dataService.snackbarduration
          );
          this._router.navigate(['/login']);
        } else {
          this.dataService.openSnackBarWithDuration(
            'User password not reset. Please contact administrator',
            'Close',
            this.dataService.snackbarduration
          );
        }
      },
      (err) => {
        this.dataService.openSnackBarWithDuration(
          'Password reset error : ' + err.errorMessage,
          'Close',
          this.dataService.snackbarduration
        );
      }
    );
  }
}
