import {Component, inject} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AuthService} from '@services/auth.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [
    RouterLink

  ],
  templateUrl: './home.component.html'
})
export class HomeComponent {

  authService = inject(AuthService);
  user = this.authService.currentUser;



}
