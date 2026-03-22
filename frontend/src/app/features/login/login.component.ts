import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@core/services/auth.service';
import { LoginRequest } from '@core/models/auth/login/login-request-model';

import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule
  ],
  templateUrl: './login.component.html'
})
export class LoginComponent {


  loginRequest: LoginRequest = {
    email: '',
    motDePasse: ''
  };

  erreur: string = '';
  chargement: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.chargement = true;
    this.erreur = '';

    this.authService.login(this.loginRequest).subscribe({
      next: () => {
        // Login réussi → on redirige vers /home
        this.router.navigate(['/home']);
      },
      error: (err: any) => {
        this.chargement = false;
        this.erreur = err.error?.message || 'Email ou mot de passe incorrect';
      }
    });
  }
}
