import { SelectionModel } from '@angular/cdk/collections';
import { HttpClient, HttpParams } from '@angular/common/http';

import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { UserDetails } from '../data.model';
import { DataService } from '../data.service';
import { User } from '../dataClass';

@Component({
  selector: 'app-manage-user',
  templateUrl: './manage-user.component.html',
  styleUrls: ['../app.component.css'],
  encapsulation: ViewEncapsulation.None,
  providers: [],
})
export class ManageUserComponent implements OnInit {
  constructor(public dataService: DataService, private http: HttpClient) {}

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;
  @ViewChild(MatSort)
  matSort!: MatSort;

  dataSource!: MatTableDataSource<any>;
  userDetails: UserDetails[] = [];
  userSelected: UserDetails[] = [];

  user: User = new User();
  message = '';
  team = 'All';
  disableButton = false;
  roleList = this.dataService.roleListForSignUp;
  teamList = this.dataService.teamListForSignUp;
  decisionList: string[] = ['Yes', 'No'];

  userColumns: string[] = [
    'select',
    'firstname',
    'lastname',
    'username',
    'team',
    'role',
    'approved',
    'active',
  ];
  selection = new SelectionModel<UserDetails>(true, []);

  ngOnInit(): void {
    if (localStorage.getItem('Role') != 'Admin') {
      this.disableButton = true;
    }

    this.viewUserData();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  masterToggle() {
    this.isAllSelected()
      ? this.selection.clear()
      : this.dataSource.data.forEach((row) => this.selection.select(row));
  }

  onSave() {
    if (this.selection.selected.length === 0)
      this.dataService.openSnackBarWithDuration(
        'Please select a row',
        'Close',
        this.dataService.snackbarduration
      );

    this.userSelected = this.selection.selected;
    for (let i = 0; i < this.userSelected.length; i++) {
      this.user = new User();
      this.user.username = this.userSelected[i].username;
      this.user.firstname = this.userSelected[i].firstname;
      this.user.lastname = this.userSelected[i].lastname;
      this.user.team = this.userSelected[i].team;
      this.user.approved = this.userSelected[i].approved;
      this.user.active = this.userSelected[i].active;
      this.user.role = this.userSelected[i].role;

      this.http
        .put(this.dataService.apiURL + 'manage/user/save', this.user, {
          responseType: 'text',
        })
        .subscribe({
          next: (response: any) => {
            if (response === 0) {
              this.dataService.openSnackBarWithDuration(
                'User not updated. Please contact administrator',
                'Close',
                this.dataService.snackbarduration
              );
            } else {
              this.dataService.openSnackBarWithDuration(
                'User updated successfully',
                'Close',
                this.dataService.snackbarduration
              );
              this.viewUserData();
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

  OnDelete() {
    if (this.selection.selected.length === 0)
      this.dataService.openSnackBarWithDuration(
        'Please select a row',
        'Close',
        this.dataService.snackbarduration
      );

    this.userSelected = this.selection.selected;
    for (let i = 0; i < this.userSelected.length; i++) {
      let params = new HttpParams();
      params = params.append('username', this.userSelected[i].username + '');
      this.http
        .delete(this.dataService.apiURL + 'manage/user/delete', {
          params: params, responseType: 'text'
        })
        .subscribe({
          next: (response: any) => {
            if (response === 0) {
              this.dataService.openSnackBarWithDuration(
                'User not deleted. Please contact administrator',
                'Close',
                this.dataService.snackbarduration
              );
            } else {
              this.dataService.openSnackBarWithDuration(
                'User deleted successfully',
                'Close',
                this.dataService.snackbarduration
              );
              this.viewUserData();
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

  async viewUserData() {
    this.selection.clear();
    this.dataService.loadUserDetails().subscribe({
      next: (response: any) => {
        if (response != null) {
          this.userDetails = response;
          this.dataSource = new MatTableDataSource(this.userDetails);
          this.dataSource.sort = this.matSort;
          this.dataSource.paginator = this.paginator;
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
