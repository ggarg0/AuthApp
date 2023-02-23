import { Injectable, Injector } from '@angular/core';
import { HttpInterceptor } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable()
export class TokenInterceptorService implements HttpInterceptor {
  constructor(private injector: Injector) {}
  intercept(req: any, next: any) {
    let user: any;
    let role: any;

    let authService = this.injector.get(AuthService);

    user = authService.getLoggedInUsername();
    console.log(user);

    role = authService.getRole();
    console.log(role);




      let tokenizedReq = req.clone({
        headers: req.headers
          .set('Authorization', 'Bearer ' + authService.getToken())
          .set('Username', user)
          .set('Role', role),
      });
      console.log(tokenizedReq);
      return next.handle(tokenizedReq);

  }
}
