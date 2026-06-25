import {Component, computed, inject, Inject, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';

import {AuthService} from '@services/auth.service';
import {Navbar} from '@shared/components/navbar/navbar';
import {FooterComponent} from '@shared/components/footer/footer';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar, FooterComponent],
  templateUrl: './app.html',
  //styleUrl: './app.css'
})
export class App {


  authService = inject(AuthService);
  isLoggedIn = computed(() => this.authService.estConnecte());
  protected readonly title = signal('frontend');


  collapsed = false;

  onNavbarToggle(isCollapsed: boolean): void {
    this.collapsed = isCollapsed;
  }
}
