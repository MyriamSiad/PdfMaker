import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {EncryptFileRequest} from '@core/models/encrypt/encrypt-request-model';
import {Observable} from 'rxjs';
import {EncryptFileResponse} from '@core/models/encrypt/encrypt-response-model';

import { invoke } from '@tauri-apps/api/core';
import {SecretDetailResponse} from '@core/models/encrypt/secret-detail-response';
@Injectable({
  providedIn: 'root'
})





export class CoffreFortService {
  private readonly API_URL = 'http://localhost:8080/api/rest/coffre-fort';

  constructor(private http: HttpClient) {}
  getUserSecrets(idUser: number): Observable<SecretDetailResponse> {
    return this.http.get<SecretDetailResponse>(`${this.API_URL}/secrets/${idUser}`);
  }

 /* async  chiffrerFichier(password: string, salt: string, masterKey: string, pdfBytes: Uint8Array) {
    return await invoke<Uint8Array>('encrypt_pdf', {
      password,
      salt,
      encryptedMasterKey: masterKey,
      pdfBytes,
    });
  }

*/
/*
  chiffrerFichier(request: EncryptFileRequest): Observable<EncryptFileResponse> {
    const formData = new FormData();
    formData.append('fichierPdf', request.fichierPdf as File);
    formData.append('nomFichierSortie', request.nomFichierSortie);

    return this.http.post<EncryptFileResponse>(`${this.API_URL}/encrypt-file`, formData);
  }
*/
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

  telechargerFichierChiffre(result: Uint8Array): void {
    const blob = new Blob([result as Uint8Array<ArrayBuffer>], { type: 'application/octet-stream' });
    const url = URL.createObjectURL(blob);
    const lien = document.createElement('a');
    lien.href = url;
    lien.download = 'fichier.enc';
    lien.click();
    URL.revokeObjectURL(url);
  }


  /*
  async dechiffrerFichier(password: string, salt: string, encryptedMasterKey: string, encryptedPdf: Uint8Array): Promise<Uint8Array> {
    return await invoke<Uint8Array>('decrypt_pdf', {
      password,
      salt,
      encryptedMasterKey : encryptedMasterKey,
      encryptedPdf,
    });
  }*/
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

  async chiffrerFichier(motDePasse: string, salt: string, masterKey: string, pdfBytes: Uint8Array): Promise<Uint8Array> {
    const resultB64 = await invoke<string>('encrypt_pdf', {
      password: motDePasse,
      salt: salt,
      encryptedMasterKey: masterKey,
      pdfBytesB64: this.toBase64(pdfBytes),
    });
    return this.fromBase64(resultB64);
  }
  async dechiffrerFichier(motDePasse: string, salt: string, masterKey: string, encryptedBytes: Uint8Array): Promise<Uint8Array> {
    const resultB64 = await invoke<string>('decrypt_pdf', {
      password: motDePasse,
      salt: salt,
      encryptedMasterKey: masterKey,
      encryptedPdfB64: this.toBase64(encryptedBytes),
    });
    return this.fromBase64(resultB64);
  }

}
