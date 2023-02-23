import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { Role } from '../data.model';
import { User } from '../dataClass';
import { Observable, retry, catchError } from 'rxjs';

@Injectable()
export class AuthService {
  constructor(
    private http: HttpClient,
    private _router: Router,
    public dataService: DataService
  ) {}

  authenticateUser(user: User) {
    return this.http.post<any>(
      this.dataService.apiURL + 'manage/user/authenticate/',
      user
    );
  }

  signupUser(user: User) {
    return this.http.post<any>(
      this.dataService.apiURL + 'manage/user/adduser',
      user
    );
  }

  resetUserPassword(user: User) {
    return this.http.post<any>(
      this.dataService.apiURL + 'manage/user/resetpassword',
      user
    );
  }

  getOTP(username: string): Observable<string> {
    let params = new HttpParams();
    const url = this.dataService.apiURL + 'user/otp';
    console.log('username ; ' + url);
    params = params.append('username', username + '');
    console.log('url ; ' + params);

    return this.http
      .get<string>(url, { params: params });
  }


  refreshJWTToken() {
    if (this.getLoggedInUsername() != null) {
      this.http
        .get<any>(
          this.dataService.apiURL +
            'ce/user/refreshjwttoken/' +
            this.getLoggedInUsername() +
            '/' +
            this.getRole()
        )
        .subscribe(
          (res) => {
            if (res.length > 0) {
              localStorage.setItem('JWTToken', res);
              this.dataService.setLoggedInUsername(
                this.getLoggedInUserFullName()
              );
            }
          },
          (err) => {
            if (err.status === 401) {
              this.dataService.showErrorMessage(
                'Session timeout. Please login again'
              );
            } else if (err.status === 403) {
              this.dataService.showErrorMessage('Restricted access');
            } else if (err.status === 404) {
              this.dataService.showErrorMessage('Resource not found');
            } else if (!(err.message.search('Http failure response') == -1)) {
              this.dataService.showErrorMessage('Resource not available');
            } else {
              this.dataService.showErrorMessage(
                'Exception : ' + err.statusText + ' (' + err.status + ')'
              );
            }
          }
        );
    }
  }

  logoutUser() {
    localStorage.clear();
    this._router.navigate(['']);
  }

  getToken() {
    return localStorage.getItem('JWTToken');
  }

  isDeveloper() {
    return localStorage.getItem('Role') === Role.Developer;
  }

  isAdmin() {
    return localStorage.getItem('Role') === Role.Admin;
  }

  getLoggedInUsername() {
    return localStorage.getItem('Username');
  }

  getLoggedInUserFirstname() {
    return localStorage.getItem('Firstname');
  }

  getLoggedInUserLastname() {
    return localStorage.getItem('Lastname');
  }

  getLoggedInUserFullName() {
    return (
      localStorage.getItem('Firstname') + ' ' + localStorage.getItem('Lastname')
    );
  }

  getRole() {
    return localStorage.getItem('Role');
  }

  getUserRole() {
    return localStorage.getItem('UserRole');
  }

  getUserTeam() {
    return localStorage.getItem('Team');
  }

  loggedIn() {
    return localStorage.getItem('JWTToken');
  }
}
