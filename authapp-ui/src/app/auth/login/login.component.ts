import { Component, ViewEncapsulation } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/dataClass';
import { Router } from '@angular/router';
import { DataService } from 'src/app/data.service';
import { HeaderComponent } from 'src/app/header/header.component';
import { AuthService } from '../auth.service';
import { Exceptions, ReturnMessages, Role } from 'src/app/data.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../../app.component.css'],
  encapsulation: ViewEncapsulation.None,
  providers: [HeaderComponent],
})
export class LoginComponent {
  constructor(
    private _router: Router,
    private _auth: AuthService,
    public dataService: DataService,
    public headerService: HeaderComponent
  ) {}

  display: string = '10';
  public timerInterval: any;

  ngOnInit(): void {
    localStorage.clear();
    //this.timer();
  }

  timer() {
    let seconds: number = this.dataService.timeout;
    let statSec: number = this.dataService.timeout;

    this.timerInterval = setInterval(() => {
      seconds--;
      if (statSec != 0) statSec--;

      this.display = `${statSec}`;

      if (seconds == 0) {
        clearInterval(this.timerInterval);
        window.location.href = 'http://localhost:8888/#/';
      }
    }, 1000);
  }

  emailPattern = this.dataService.emailPattern;
  password = '';
  user: User = new User();

  loginForm: FormGroup = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  onAuthenticate(loginForm: any) {
    if (loginForm.invalid) {
      return;
    }
    this.authenticate(loginForm.value);
  }

  authenticate(user: User) {
    localStorage.setItem('Username', user.username);
    this._auth.authenticateUser(user).subscribe({
      next: (response: any) => {
        if (localStorage.getItem('Username') === null) {
          localStorage.setItem('Username', user.username);
        }

        if (response.length != 0 && response[0].message.includes('valid')) {
          this.dataService.openSnackBarWithDuration(
            response[0].message,
            'Close',
            this.dataService.snackbarduration
          );
          localStorage.clear();
          return;
        }

        if (
          response.length != 0 &&
          response[0].approved === '1' &&
          response[0].active === '1'
        ) {
          localStorage.setItem('Firstname', response[0].firstName);
          localStorage.setItem('Lastname', response[0].lastName);
          localStorage.setItem('Role', response[0].role);
          localStorage.setItem('Team', response[0].team);
          localStorage.setItem('Authenticated', 'true');
          localStorage.setItem('JWTToken', response[0].message);
          this.dataService.setLoggedInUsername(
            response[0].firstName + ' ' + response[0].lastName
          );

          if (localStorage.getItem('Role') === Role.Admin) {
            this._router.navigate(['/admin']);
          } else if (localStorage.getItem('Role') === Role.Developer) {
            this._router.navigate(['/developer']);
          } else {
            this._router.navigate(['/login']);
          }
        } else if (response.length === 0) {
          this.dataService.openSnackBarWithDuration(
            'Enter valid credentials',
            'Close',
            this.dataService.snackbarduration
          );
          localStorage.clear();
        } else if (response[0].approved === '0') {
          this.dataService.openSnackBarWithDuration(
            'User not approved. Please contact administrator',
            'Close',
            this.dataService.snackbarduration
          );
          localStorage.clear();
        } else if (response[0].active === '0') {
          this.dataService.openSnackBarWithDuration(
            'User not active. Please contact administrator',
            'Close',
            this.dataService.snackbarduration
          );
          localStorage.clear();
        } else {
          this.dataService.openSnackBarWithDuration(
            'Please enter valid credentials',
            'Close',
            this.dataService.snackbarduration
          );
          localStorage.clear();
        }
      },
      error: (err) => {
        localStorage.clear();
        if (err.status === 403) {
          this.dataService.openSnackBarWithDuration(
            'Login error : Invalid credentials',
            'Close',
            this.dataService.snackbarduration
          );
        } else if (err.status === 404 || err.status === 0) {
          this.dataService.openSnackBarWithDuration(
            'Login error : Resource not found',
            'Close',
            this.dataService.snackbarduration
          );
        } else {
          this.dataService.openSnackBarWithDuration(
            'Login error : ' + err.message,
            'Close',
            this.dataService.snackbarduration
          );
        }
      },
    });
  }
}
