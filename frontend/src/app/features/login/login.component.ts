import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { AuthService } from '@core/services/auth.service';
import { LoginRequest } from '@core/models/auth/login/login-request-model';
import {MatIcon, MatIconModule} from '@angular/material/icon';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    NgClass,
    FormsModule,
    MatIcon
  ],
  templateUrl: './login.component.html'
})
export class LoginComponent {


  loginRequest: LoginRequest = {
    email: '',
    motDePasse: ''
  };

  loginForm = new FormGroup({
    email: new FormControl(this.loginRequest.email, [Validators.required, Validators.email]),
    motDePasse: new FormControl(this.loginRequest.motDePasse, [Validators.required])
  });
  erreur: string = '';
  chargement: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {

  }

  ngOnInit() {
    if (this.authService.estConnecte()) {
      this.router.navigate(['/home']);
    }
  }


  onSubmit(): void {
    this.chargement = true;
    this.erreur = '';

    this.authService.login(this.loginRequest).subscribe({
      next: (value) => {
        this.chargement = false;
        // Login réussi → on redirige vers /home
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.chargement = false;
        switch (err.status) {
          case 401:
            this.erreur = 'Email ou mot de passe incorrect';
            break;
          case 500:
            this.erreur = 'Erreur serveur, réessayez plus tard';
            break;
          case 0:
            this.erreur = 'Impossible de contacter le serveur';
            break;
          default:
            this.erreur = 'Une erreur est survenue';
        }
      }
    });
  }
}
