import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { AuthService } from '@core/services/auth.service';
import {  RegisterRequest} from '@core/models/auth/register/register-request.model';
import {FormGroup, ReactiveFormsModule, FormBuilder, Validators} from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './register.component.html'
})
export class RegisterComponent {

  registerForm: FormGroup;
  erreur: string = '';
  chargement: boolean = false;

  registerRequest: RegisterRequest = {
    prenom : '',
    nom : '',
    email: '',
    passwordHash : ''
  };




  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder,

  ) {
    this.registerForm = this.fb.group({
      nom: ['', [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(50)
      ]],
      prenom: ['', [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(50)
      ]],
      email: ['', [
        Validators.required,
        Validators.email          // vérifie le format email
      ]],
      passwordHash: ['', [
        Validators.required,
        Validators.minLength(12),
        // Au moins 1 majuscule, 1 chiffre, 1 caractère spécial
        Validators.pattern(/^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{12,}$/)
      ]]
    });
  }

  get nom() { return this.registerForm.get('nom'); }
  get prenom() { return this.registerForm.get('prenom'); }
  get email() { return this.registerForm.get('email'); }
  get passwordHash() { return this.registerForm.get('passwordHash'); }
  onSubmit(): void {

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched(); // affiche toutes les erreurs
      return;
    }

    this.chargement = true;
    this.authService.register(this.registerForm.value).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err: any) => {
        this.chargement = false;
        switch (err.status) {
          case 409:
            this.erreur = 'Cet email est déjà utilisé';
            break;
          case 400:
            this.erreur = 'Les données saisies sont invalides';
            break;
          case 500:
            this.erreur = 'Erreur serveur, réessaie plus tard';
            break;
          default:
            this.erreur = err.error?.message || 'Une erreur est survenue';
        }
      }
    });
  }
}
