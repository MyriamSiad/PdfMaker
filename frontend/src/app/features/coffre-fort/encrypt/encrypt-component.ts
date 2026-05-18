import { Component } from '@angular/core';
import { CoffreFortService } from '@core/services/coffre-fort-service';
import { EncryptFileRequest } from '@core/models/encrypt/encrypt-request-model';
import {EncryptFileResponse} from '@core/models/encrypt/encrypt-response-model';
import {FormsModule} from '@angular/forms';

import {firstValueFrom} from 'rxjs';
import {jwtDecode} from 'jwt-decode';

@Component({
  selector: 'app-encrypt',
  templateUrl: 'encrypt-component.html',
  imports: [
    FormsModule,

  ],
  //styleUrls: ['encrypt.component.scss']
})
export class EncryptComponent {

  fichierSelectionne: File | null = null;
  nomFichierSortie: string = '';
  chargement: boolean = false;
  erreur: string | null = null;
  resultat: Uint8Array | null = null;
  motDePasse: string = '';

  constructor(private encryptService: CoffreFortService) {
  }


  onFichierChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.fichierSelectionne = input.files[0];
      this.erreur = null;
      this.resultat = null;
    }
  }

  getUserId(): number {

    const user = JSON.parse(localStorage.getItem('user')!);

    const decoded: any = jwtDecode(user.accessToken);
    console.log(decoded.userId);
    return decoded.userId;
  }

  async onEncrypt(): Promise<Uint8Array | null> {
    try {
      const secrets = await firstValueFrom(this.encryptService.getUserSecrets(this.getUserId()));

      const buffer = await this.fichierSelectionne?.arrayBuffer();
      const pdfBytes = new Uint8Array(buffer!);

      const result = await this.encryptService.chiffrerFichier(
        this.motDePasse,
        secrets.salt,
        secrets.masterKey,
        pdfBytes
      );
      this.resultat = result; // ← stocker le résultat pour afficher le bouton

      return result;
    } catch (error) {
      this.erreur = 'Une erreur est survenue lors du chiffrement.';
      console.error(error);
      return null; // ← et ça aussi
    } finally {
      this.chargement = false;
    }
  }

  onSubmit(): void {
    if (!this.fichierSelectionne || !this.nomFichierSortie) return;

    this.chargement = true;
    this.erreur = null;
    this.resultat = null;

    /*   const request: EncryptFileRequest = {
        fichierPdf: this.fichierSelectionne,
        nomFichierSortie: this.nomFichierSortie
      }



      /*

      this.encryptService.chiffrerFichier(request).subscribe({
        next: (response: EncryptFileResponse) => {
          this.resultat = response;
          this.chargement = false;
        },
        error: (err) => {
          this.erreur = 'Une erreur est survenue lors du chiffrement.';
          this.chargement = false;
          console.error(err);
        }
      });*/


  }
  telecharger(): void {
    this.chargement = true;
    this.onEncrypt()
      .then((resultBytes: Uint8Array | null) => {
        if (resultBytes) {
          this.encryptService.telechargerFichierChiffre(resultBytes);
        }
        this.chargement = false;
      })
      .catch(err => {
        this.erreur = "Erreur inattendue : " + err;
        this.chargement = false;
      });
  }
}
