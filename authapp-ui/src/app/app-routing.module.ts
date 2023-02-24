import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';
import { LoginComponent } from './auth/login/login.component';
import { ResetPasswordComponent } from './auth/reset-password/reset-password.component';
import { SignupComponent } from './auth/signup/signup.component';
import { Role } from './data.model';
import { ManageUserComponent } from './manage-user/manage-user.component';

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'resetpassword', component: ResetPasswordComponent },

  {
    path: 'getuser',
    component: SignupComponent,
    canActivate: [AuthGuard],
    data: { roles: [Role.Admin, Role.Developer] },
  },

  {
    path: 'user',
    component: ManageUserComponent,
    canActivate: [AuthGuard],
    data: { roles: [Role.Admin] },
  },

  { path: '**', redirectTo: 'login' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
  providers: [AuthGuard],
})
export class AppRoutingModule {}
