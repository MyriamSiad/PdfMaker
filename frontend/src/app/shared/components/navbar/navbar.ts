import {Component, computed, inject, Injectable} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '@services/auth.service';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLink
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})


export class Navbar {

  authService = inject(AuthService);
  router = inject(Router);

  logout() :void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
