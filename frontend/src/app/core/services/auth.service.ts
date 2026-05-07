import {Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {LoginRequest} from "@core/models/auth/login/login-request-model";
import {ProfilResponse} from '@core/models/auth/login/profil-response.model';
import {RegisterRequest} from '@core/models/auth/register/register-request.model';
import {SIGNAL} from '@angular/core/primitives/signals';
import { jwtDecode } from 'jwt-decode';

interface JwtPayload {
  sub: string;
  iat: number;
  exp: number;
  prenom: string;
  nom: string;
  userId: number;
}
@Injectable({
  providedIn: 'root'
})


export class AuthService {


  private readonly API_URL = 'http://localhost:8080/api/rest/user';


  currentUser = signal<any>(null);
  private profilActif: ProfilResponse | null = null;


  constructor(private http: HttpClient ) {
    this.loadUserFromStorage();
  }

  getDecodedToken(): JwtPayload | null {
    const stored = localStorage.getItem('user');
    if (!stored) return null;

    try {
      const data = JSON.parse(stored);
      return jwtDecode<JwtPayload>(data.accessToken);
    } catch {
      return null;
    }
  }

  getEmail(): string | null {
    return this.getDecodedToken()?.sub ?? null;
  }
  getPrenom(): string | null {
    return this.getDecodedToken()?.prenom ?? null;
  }

  getNom(): string | null {
    return this.getDecodedToken()?.nom ?? null;
  }

 getId() : number |null {
    return this.getDecodedToken()?.userId ?? null;
 }
  login(request: LoginRequest): Observable<ProfilResponse> {
    return this.http.post<ProfilResponse>(`${this.API_URL}/login`, request).pipe(
      tap(profil => {
        this.currentUser.set(profil);
        localStorage.setItem('user', JSON.stringify(profil));
      })
    );
  }

  register(request: RegisterRequest): Observable<ProfilResponse> {
    return this.http.post<ProfilResponse>(`${this.API_URL}/register`, request).pipe(
      tap(profil => {
        //this.profilActif = profil;
      })
    )
  }

  logout(): void {
    this.currentUser.set(null);
    localStorage.removeItem('user');
  }


  loadUserFromStorage() {
    const stored = localStorage.getItem('user');
    if (stored) {
      this.currentUser.set(JSON.parse(stored));
    }
  }

  estConnecte(): boolean {
    return this.currentUser() !== null;

  }

  getProfil(): JwtPayload | null {
    return this.getDecodedToken();
  }
}
