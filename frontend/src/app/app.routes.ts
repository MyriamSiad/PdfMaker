import { Routes } from '@angular/router';
import { LoginComponent } from '@features/login/login.component';
import { AuthGuard } from '@core/guards/auth.guard';

 export const routes: Routes = [
  { path: 'login', component: LoginComponent },

   {
     path: 'home',
     canActivate: [AuthGuard],
     loadComponent: () => import('@features/home/home.component').then(m => m.HomeComponent)
   },
   {
     path: 'register',
     loadComponent: () => import('@features/register/register.component').then(m => m.RegisterComponent)
   },
  // Par défaut → /login
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];

