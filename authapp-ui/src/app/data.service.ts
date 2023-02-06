import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { throwError } from "rxjs";
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class DataService {
  setLoggedInUsername(arg0: string) {
    throw new Error('Method not implemented.');
  }

  constructor(private http: HttpClient, private _snackBar: MatSnackBar,
    private _router: Router) {}

  apiURL = 'http://localhost:8888/api/';
  emailPattern = "^[a-zA-Z0-9._%+-]+@gmail.com";
  snackbarduration = 5000;

  openSnackBarWithDuration(message: string, action: string, time: number) {
    this._snackBar.open(message, action, {
      duration: time,
    });
  }

  handleError(error: HttpErrorResponse) {
    let errorMessage = 'Unknown error!';
    if (error.status == 401) {
      errorMessage = 'Session timeout';
    } else if (error.status == 403) {
      errorMessage = 'Restricted access';
    } else if (error.status == 404) {
      errorMessage = 'Resource not found';
    } else {
      if (!(error.message.search("Http failure response") == -1)) {
        errorMessage = 'Resource not available';
      } else {
        errorMessage = 'Exception : ' + error.statusText + ' (' + error.status + ')';
      }
    }
    return throwError(errorMessage);
  }

  showErrorMessage(err: string) {
    if (!(err.search("Session timeout") == -1)) {
      localStorage.clear();
      this._router.navigate(['/login']);
      this.openSnackBarWithDuration('Session timeout. Please login again.', 'Close', this.snackbarduration);
    } else if (!(err.search("Restricted access") == -1)) {
      localStorage.clear();
      this._router.navigate(['/login']);
      this.openSnackBarWithDuration('Restricted access', 'Close', this.snackbarduration);
    } else if (!(err.search("Resource not found") == -1)) {
      this.openSnackBarWithDuration('Resource not found', 'Close', this.snackbarduration);
    } else {
      this.openSnackBarWithDuration(err, 'Close', this.snackbarduration);
    }
  }

}