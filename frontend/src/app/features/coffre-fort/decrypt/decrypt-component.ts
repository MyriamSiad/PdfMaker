import {Component, NgZone} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CoffreFortService} from '@services/coffre-fort-service';
import {firstValueFrom} from 'rxjs';
import {jwtDecode} from 'jwt-decode';
import {NgxExtendedPdfViewerModule} from 'ngx-extended-pdf-viewer';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
@Component({
  selector: 'app-encrypt',
  templateUrl: 'decrypt-component.html',
  imports: [
    FormsModule,
    NgxExtendedPdfViewerModule,

  ],
  //styleUrls: ['encrypt.component.scss']
})


export class DecryptComponent{

  fichierSelectionne: File | null = null;
  nomFichierSortie: string = '';
  chargement: boolean = false;
  erreur: string | null = null;
  resultat: Uint8Array | null = null;
  motDePasse: string = '';
  pdfUrl: SafeResourceUrl | null = null;

  constructor(private encryptService: CoffreFortService, private sanitizer: DomSanitizer,  private ngZone: NgZone) {
  }


  getUserId(): number {

    const user = JSON.parse(localStorage.getItem('user')!);

    const decoded: any = jwtDecode(user.accessToken);
    console.log(decoded.userId);
    return decoded.userId;
  }

  async onDecrypt(): Promise<Uint8Array | null> {
    try {

      const secrets = await firstValueFrom(this.encryptService.getUserSecrets(this.getUserId()));

      const buffer = await this.fichierSelectionne?.arrayBuffer();
      const encryptedBytes = new Uint8Array(buffer!);

      const result = await this.encryptService.dechiffrerFichier(
        this.motDePasse,
        secrets.salt,
        secrets.masterKey,
        encryptedBytes
      );

      this.resultat = result;
      return result;
    } catch (error) {
      this.erreur = 'Une erreur est survenue lors du déchiffrement.';
      console.error(error);
      return null;
    } finally {
      this.chargement = false;
    }
  }

  onFichierChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.fichierSelectionne = input.files[0];
      this.erreur = null;
      this.resultat = null;
    }
  }
onSubmit() : void {
  if (!this.fichierSelectionne ) return;

  this.chargement = true;

  this.erreur = null;
  this.resultat = null;

  this.dechiffrer();

}
  afficherPdf(resultBytes: Uint8Array) {
    const blob = new Blob([resultBytes as Uint8Array<ArrayBuffer>], { type: 'application/pdf' });
    const unsafeUrl = URL.createObjectURL(blob);
    this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(unsafeUrl);
  }

  fermerVisionneuse() {
    this.resultat = null;

  }

  telechargerPdf(result: Uint8Array): void {
    const blob = new Blob([result as Uint8Array<ArrayBuffer>], { type: 'application/pdf' });
    const url = URL.createObjectURL(blob);
    const lien = document.createElement('a');
    lien.href = url;
    lien.download = 'fichier.pdf';
    lien.click();
    URL.revokeObjectURL(url);
  }

  dechiffrer(): void {
    this.chargement = true;
    this.onDecrypt()
      .then((resultBytes: Uint8Array | null) => {
        if (resultBytes) {
          this.ngZone.run(() => {
            // Copie propre du buffer
            this.resultat = new Uint8Array(resultBytes);
          });
        }
        this.chargement = false;
      })
      .catch(err => {
        this.erreur = "Erreur inattendue : " + err;
        this.chargement = false;
      });
  }
}
