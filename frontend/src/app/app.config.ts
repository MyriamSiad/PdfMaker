import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptors, withInterceptorsFromDi} from '@angular/common/http';
import { provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideZoneChangeDetection } from '@angular/core';
import { routes } from './app.routes';
import {AuthInterceptor} from '@core/guards/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),

    // Configuration pour les intercepteurs de type "Classe"
    provideHttpClient(
      withInterceptorsFromDi()
    ),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },

    provideBrowserGlobalErrorListeners(),
  ]
};
