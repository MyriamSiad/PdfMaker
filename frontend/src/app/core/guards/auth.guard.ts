import {inject, Injectable} from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '@core/services/auth.service';
import {LoginComponent} from '@features/login/login.component';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

 private authService = inject(AuthService);
 private router = inject(Router);

  canActivate(): boolean {
    if (this.authService.estConnecte()) {
      return true;
    }
    this.router.navigate(['/login']);
    return false;
  }
}
