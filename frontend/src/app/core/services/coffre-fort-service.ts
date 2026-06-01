import {Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {EncryptFileRequest} from '@core/models/encrypt/encrypt-request-model';
import {Observable} from 'rxjs';
import {EncryptFileResponse} from '@core/models/encrypt/encrypt-response-model';
import { fromEvent, merge, Subscription, timer } from 'rxjs';
import { switchMap, take } from 'rxjs/operators';
import { invoke } from '@tauri-apps/api/core';
import {SecretDetailResponse} from '@core/models/encrypt/secret-detail-response';
import {DossierResponseDto} from '@core/models/dossier/dossier.model';



export interface Fichier {
  idFichier: number;
  nomOriginal: string;
  nomStockage: string;
  dateAjout : Date;
  cheminLocal : string;

}

export interface FichierEncryptRequest{
  nomOriginal: string;
  nomStockage: string;
  dateAjout : Date;
  cheminLocal : string;

}
export interface DossierVirtuel {
  idDossier: number;
  nomDuDossier: string;
  //fichiers: Fichier[]; // Ton Set<Fichier> arrive ici sous forme de tableau
}
@Injectable({
  providedIn: 'root'
})
export class CoffreFortService {
  private readonly API_URL = 'http://localhost:8080/api/rest/coffre-fort';

  constructor(private http: HttpClient) {}

  addDossier(nomDossier: string): Observable<DossierResponseDto> {
    return this.http.post<DossierResponseDto>(
      `${this.API_URL}/add-dossier`,
      { nomDossier }
    );
  }

  modifyDossier(idDossier: number, nomDossier: string): Observable<DossierResponseDto> {
    return this.http.put<DossierResponseDto>(
      `${this.API_URL}/modify-dossier/${idDossier}`,
      { nomDossier }
    );
  }

  deleteDossier(idDossier: number): Observable<void> {
    return this.http.delete<void>(
      `${this.API_URL}/delete-dossier/${idDossier}`,
      {}
    );
  }



  updateFilename(idFile: number, nomFichier: string): Observable<void> {
    return this.http.put<void>(
      `${this.API_URL}/update-filename/${idFile}`,
      { nomFichier }
    );
  }

  deleteFile(idFile: number): Observable<void> {
    return this.http.delete<void>(
      `${this.API_URL}/delete-file/${idFile}`);
  }
  getUserSecrets(): Observable<SecretDetailResponse> {
    return this.http.get<SecretDetailResponse>(`${this.API_URL}/secrets`);
  }

  openVault( password: string): Observable<any> {
    return this.http.post(`${this.API_URL}/open`, { password });
  }

  encryptFile( file: File , idDossier : number): Observable<Blob> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('idDossier', idDossier.toString());
    return this.http.post(`${this.API_URL}/encrypt`, formData, {
      responseType: 'blob'  // on reçoit un fichier binaire
    });
  }

  decryptFile(idFichier: number): Observable<Blob> {
    return this.http.post(`${this.API_URL}/decrypt/${idFichier}`, {}, {
      responseType: 'blob'
    });
  }

  getDossier():Observable<DossierVirtuel[]>{
    return this.http.get<DossierVirtuel[]>(`${this.API_URL}/dossiers`);
  }

  getFichiers(idDossiers: number): Observable<Fichier[]>{
    return this.http.get<Fichier[]>(`${this.API_URL}/dossiers/${idDossiers}/fichiers`);
  }



  isUnlocked = signal<boolean>(false);

  private idleSubscription?: Subscription;

  private readonly INACTIVITY_TIME = 5 * 60 * 1000;

  chiffrerFichier(request: EncryptFileRequest): Observable<EncryptFileResponse> {
    const formData = new FormData();
    formData.append('fichierPdf', request.fichierPdf as File);
    formData.append('nomFichierEntree', request.nomFichierEntree);

    return this.http.post<EncryptFileResponse>(`${this.API_URL}/encrypt-file`, formData);
  }

  telechargerFichierChiffre(result: Uint8Array): void {
    const blob = new Blob([result as Uint8Array<ArrayBuffer>], { type: 'application/octet-stream' });
    const url = URL.createObjectURL(blob);
    const lien = document.createElement('a');
    lien.href = url;
    lien.download = 'fichier.enc';
    lien.click();
    URL.revokeObjectURL(url);
  }

  private toBase64(bytes: Uint8Array): string {
    let binary = '';
    const chunkSize = 8192;
    for (let i = 0; i < bytes.length; i += chunkSize) {
      const chunk = bytes.subarray(i, i + chunkSize);
      binary += String.fromCharCode(...chunk);
    }
    return btoa(binary);
  }

  private fromBase64(b64: string): Uint8Array {
    return new Uint8Array([...atob(b64)].map(c => c.charCodeAt(0)));
  }

 /*
  chiffrerFichier(request: EncryptFileRequest): Observable<EncryptFileResponse> {
    const formData = new FormData();
    formData.append('fichierPdf', request.fichierPdf as File);
    formData.append('nomFichierSortie', request.nomFichierSortie);

    return this.http.post<EncryptFileResponse>(`${this.API_URL}/encrypt-file`, formData);
  }*/

  async dechiffrerFichier(motDePasse: string, salt: string, masterKey: string, encryptedBytes: Uint8Array): Promise<Uint8Array> {
    const resultB64 = await invoke<string>('decrypt_pdf', {
      password: motDePasse,
      salt: salt,
      encryptedMasterKey: masterKey,
      encryptedPdfB64: this.toBase64(encryptedBytes),
    });
    return this.fromBase64(resultB64);
  }






  async unlock(): Promise<void> {

    this.isUnlocked.set(true);
    this.startInactivityTimer();
  }

  startInactivityTimer(): void {
    this.stopInactivityTimer();

    const activity  = merge(
      fromEvent(window, 'mousemove'),
      fromEvent(window, 'keydown'),
      fromEvent(window, 'click'),
      fromEvent(window, 'scroll')
    );

    this.idleSubscription = activity.pipe(
      switchMap(() => timer(this.INACTIVITY_TIME)),take(1)
    )
      .subscribe(() => {
        this.lock();
      });

  }

  stopInactivityTimer(): void {
    if (this.idleSubscription) {
      this.idleSubscription.unsubscribe();
    }
  }

  async lock(): Promise<void> {

    this.isUnlocked.set(false);
    this.stopInactivityTimer();
  }



}



/*telechargerFichierChiffre(resultat: EncryptFileResponse): void {
  // Décodage base64 → bytes (identique à telechargerPdf)
  const byteCharacters = atob(resultat.fichierChiffre);
  const byteNumbers = Array.from(byteCharacters)
    .map(char => char.charCodeAt(0));
  const byteArray = new Uint8Array(byteNumbers);

  // Type MIME générique pour un fichier chiffré
  const blob = new Blob([byteArray], {type: 'application/octet-stream'});

  const url = URL.createObjectURL(blob);
  const lien = document.createElement('a');
  lien.href = url;
  lien.download = resultat.outputPath; // ex: "fichier_chiffre.enc"
  lien.click();

  URL.revokeObjectURL(url);
}*/
/*
async dechiffrerFichier(password: string, salt: string, encryptedMasterKey: string, encryptedPdf: Uint8Array): Promise<Uint8Array> {
  return await invoke<Uint8Array>('decrypt_pdf', {
    password,
    salt,
    encryptedMasterKey : encryptedMasterKey,
    encryptedPdf,
  });
}*/
