import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { DataService } from '../data.service';


@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(public dataService: DataService) {
  }
  canActivate(
    _route: ActivatedRouteSnapshot,
    _state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (localStorage.getItem('Authenticated') && !(_route.data['roles'] && _route.data['roles'].indexOf(localStorage.getItem('Role')) === -1)) {
      return true;
    }
    else {

        this.dataService.openSnackBarWithDuration('Page access not allowed', 'Close', this.dataService.snackbarduration);
      return false;
    }
  }
}

