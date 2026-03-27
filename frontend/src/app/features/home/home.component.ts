import {Component, inject} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AuthService} from '@services/auth.service';
import {RouterLink} from '@angular/router';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [
    RouterLink,
    NgClass

  ],
  templateUrl: './home.component.html'
})
export class HomeComponent {

  authService = inject(AuthService);
  user = this.authService.currentUser;

  stats = { conversions: 24, fusions: 8, separations: 3, annotations: 15 };

  activiteRecente = [
    { id: 1, nom: 'rapport-2024.txt', action: 'Conversion', date: 'Aujourd\'hui 14:32' },
    { id: 2, nom: 'contrats.pdf',     action: 'Fusion',     date: 'Hier 09:15' },
    { id: 3, nom: 'facture-mars.pdf', action: 'Annotation', date: '25 mars 2026' },
  ];

  getGreeting(): string {
    const h = new Date().getHours();
    if (h < 12) return 'Bonjour';
    if (h < 18) return 'Bon après-midi';
    return 'Bonsoir';
  }

  getUserFirstName(): string {
    return this.authService.currentUser()?.prenom ?? 'Utilisateur';
  }

  getFormattedDate(): string {
    return new Date().toLocaleDateString('fr-FR', { weekday: 'long', day: 'numeric', month: 'long' });
  }

  getFormattedTime(): string {
    return new Date().toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' });
  }

}
