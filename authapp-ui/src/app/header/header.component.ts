import { Component } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { DataService } from '../data.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['../app.component.css'],
})
export class HeaderComponent {
  lastBatchExecutionTime: string = '';
  constructor(
    public _authService: AuthService,
    public _dataService: DataService
  ) {}

  ngOnInit(): void {}

  logout() {
    if (this._authService.loggedIn()) {
      this._authService.logoutUser();
    }
  }

  async showTime() {
    const url = this._dataService.apiURL + 'loaddata/last';
    const response = await fetch(url);

    if (response.ok) {
      let json = await response.json();
      for (var data in json) {
        this.lastBatchExecutionTime = json[data];
        this._dataService.openSnackBarWithDuration(
          this.lastBatchExecutionTime,
          'Close',
          this._dataService.snackbarduration
        );
      }
    } else {
      let errorMessage = 'Unknown error!';
      if (response.status == 401) {
        errorMessage = 'Session timeout';
      } else if (response.status == 403) {
        errorMessage = 'Restricted access';
      } else if (response.status == 404) {
        errorMessage = 'Resource not found';
      } else {
        if (!(response.statusText.search('Http failure response') == -1)) {
          errorMessage = 'Resource not available';
        } else {
          errorMessage =
            'Exception : ' + response.statusText + ' (' + response.status + ')';
        }
      }
      this._dataService.openSnackBarWithDuration(
        'Exception : ' + errorMessage,
        'Close',
        this._dataService.snackbarduration
      );
    }
  }
}
