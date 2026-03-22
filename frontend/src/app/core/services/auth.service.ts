import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginRequest } from "@core/models/auth/login/login-request-model";
import { ProfilResponse } from '@core/models/auth/login/profil-response.model';
import {RegisterRequest} from '@core/models/auth/register/register-request.model';


@Injectable({
  providedIn: 'root'  // Angular fournit ce service automatiquement partout
})
export class AuthService {


  private readonly API_URL = 'http://localhost:8080/api/rest/user';


  private profilActif: ProfilResponse | null = null;

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<ProfilResponse> {
    return this.http.post<ProfilResponse>(`${this.API_URL}/login`, request).pipe(
      tap(profil => {
        this.profilActif = profil;
      })
    );
  }

  register(request: RegisterRequest): Observable<ProfilResponse> {
    return this.http.post<ProfilResponse>(`${this.API_URL}/register`, request).pipe(
      tap(profil => {
        this.profilActif = profil;
      })
    )
  }

  logout(): void {
    this.profilActif = null;
  }

  estConnecte(): boolean {
    return this.profilActif !== null;
  }

  getProfil(): ProfilResponse | null {
    return this.profilActif;
  }
}
