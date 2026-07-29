import {Component, EventEmitter, inject, Output, signal} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {AuthService} from '@services/auth.service';
import {ProfilResponse} from '@core/models/auth/login/profil-response.model';
import {MatIcon} from '@angular/material/icon';
import {JwtPayload} from 'jwt-decode';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLink,
    RouterLinkActive,

  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})


export class Navbar {

  authService = inject(AuthService);

  prenom = this.authService.getPrenom();
  nom = this.authService.getNom();
  email : string | null = this.authService.getEmail();
  router = inject(Router);
  isCollapsed = signal(false);
  @Output() collapsedChange = new EventEmitter<boolean>();

  toggle(): void {
    this.isCollapsed.update(v => !v);
    this.collapsedChange.emit(this.isCollapsed());
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  getUserInitials(): string  | null {
    const user = this.authService.getDecodedToken();
    if(user === null) return null;

   // @ts-ignore
    const  initial_nom : string | undefined = user.nom?.charAt(0).toUpperCase() ?? ''

   // @ts-ignore
    const  initial_prenom : string | undefined =  user.prenom?.charAt(0).toUpperCase() ?? ''

    // @ts-ignore
    return initial_nom + initial_prenom;
  }
  getUserProfil(): JwtPayload | null {

    return this.authService.getProfil();
  }



}
