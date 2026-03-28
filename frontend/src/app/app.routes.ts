import { Routes } from '@angular/router';
import { LoginComponent } from '@features/login/login.component';
import { AuthGuard } from '@core/guards/auth.guard';
import {TxtToPdfComponent} from '@features/conversion/conversion.txt.component';

import {HomeComponent} from '@features/home/home.component';

 export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' }, // redirection par défaut
   { path: 'login', component: LoginComponent },
   {
     path: '',
     canActivate: [AuthGuard],
     children: [
       { path: 'home', loadComponent: () => import('@features/home/home.component').then(m => m.HomeComponent) },

       { path: 'txt-to-pdf',
    loadComponent: () => import('@features/conversion/conversion.txt.component').then(m => m.TxtToPdfComponent)
},
       { path: 'img-to-pdf',
         loadComponent: () => import('@features/conversion/image-conversion').then(m => m.ImageToPdfComponent)
       },
     ]
   },
   {
     path: 'register',
     loadComponent: () => import('@features/register/register.component').then(m => m.RegisterComponent)
   },

   /*// Par défaut → /login
   { path: '', redirectTo: '/login', pathMatch: 'full' }*/
];

