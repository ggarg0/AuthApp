import { Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { AuthService } from '../auth/auth.service';
import { UserDetails } from '../data.model';
import { DataService } from '../data.service';


@Component({
  selector: 'app-get-user-detail',
  templateUrl: './get-user-detail.component.html',
  styleUrls: ['./get-user-detail.component.css']
})
export class GetUserDetailComponent {
  constructor(
    public dataService: DataService,
    private _authService: AuthService
  ) {}

  dataSource!: MatTableDataSource<any>;
  userDetails: UserDetails[] = [];
  roleList = this.dataService.roleListForSignUp;
  teamList = this.dataService.teamListForSignUp;
  decisionList: string[] = ['Yes', 'No'];

  userColumns: string[] = [
    'firstname',
    'lastname',
    'username',
    'team',
    'role',
    'approved',
    'active',
  ];

  ngOnInit(): void {
    this.viewUserData(this._authService.getLoggedInUsername());
  }

  async viewUserData(username: any) {
    this.dataService.loadUserDetails(username).subscribe({
      next: (response: any) => {
        if (response != null) {
          this.userDetails = response;
          this.dataSource = new MatTableDataSource(this.userDetails);
        }
      },
      error: (err) => {
        if (err.status === 401) {
          this.dataService.showErrorMessage(
            'Session timeout. Please login again'
          );
        } else if (err.status === 404) {
          this.dataService.showErrorMessage('Resource not found');
        } else if (!(err.message.search('Http failure response') == -1)) {
          this.dataService.showErrorMessage('Resource not available');
        } else {
          this.dataService.showErrorMessage(
            'Exception : ' + err.statusText + ' (' + err.status + ')'
          );
        }
      },
    });
  }

}
