import { Injectable, Injector } from '@angular/core';
import { HttpInterceptor } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable()
export class TokenInterceptorService implements HttpInterceptor {
  constructor(private injector: Injector) {}
  intercept(req: any, next: any) {
    let user;
    let role;

    let authService = this.injector.get(AuthService);

    if (!(authService.getLoggedInUsername() === null))
      user = authService.getLoggedInUsername();

    if (!(authService.getRole() === null)) role = authService.getRole();

    let tokenizedReq = req.clone({
      headers: req.headers
        .set('Authorization', 'Bearer ' + authService.getToken())
        .set('Username', user)
        .set('Role', role),
    });
    return next.handle(tokenizedReq);
  }
}
