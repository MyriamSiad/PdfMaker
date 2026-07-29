import {Component, OnInit, inject, HostListener} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {SecretDetailResponse} from '@core/models/encrypt/secret-detail-response';
import { CoffreFortService} from '@core/services/coffre-fort-service';
import {jwtDecode} from 'jwt-decode';
import {NgxExtendedPdfViewerModule} from 'ngx-extended-pdf-viewer';

import { NgZone } from '@angular/core';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import {DossierResponseDto} from '@core/models/dossier/dossier.model';
import {MatIcon} from '@angular/material/icon';


export interface Fichier {
  idFichier: number;
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
@Component({
  selector: 'app-vault',
  standalone: true,
  imports: [CommonModule, FormsModule, NgxExtendedPdfViewerModule, MatIcon],
  templateUrl: 'coffre-fort-component.html',
  //styleUrls: ['./vault.component.css']
})
export class VaultComponent  {
  vaultService = inject(CoffreFortService);
  dossiers: DossierVirtuel[] = [];
  motDePasse = '';
  vaultOpen = false;
  selectedFile: File | null = null;
  userId = this.getUserId();
  error: string | null = null;
  loading = false;
  isDragOver: boolean = false;
  fichiers : Fichier[] = [];
  loadingVault = false;      // pour déverrouiller
  loadingFichiers = false;   // pour charger les fichiers
  loadingDecrypt = false;    // pour déchiffrer
  resultat: Uint8Array | null = null;
  fichiersParDossier: { [idDossier: number]: Fichier[] } = {};
  addingDossier = false;
  newDossierNom = '';
  openMenuId: string | null = null;
  editingDossierId: number | null = null;
  editingFichierId: number | null = null;
  editingNom: string = '';
  fichier :Fichier  | null = null  ;
  dossierOuvertId: number | null = null;
  selectedDossierId : number |null = null;

  isSuccess : boolean = false;
  getUserId(): number {

    const user = JSON.parse(sessionStorage.getItem('user')!);
    const decoded: any = jwtDecode(user.accessToken);
    console.log(decoded.userId);
    return decoded.userId;

  }

  toggleMenu(id: string): void {
    this.openMenuId = this.openMenuId === id ? null : id;
  }

  @HostListener('document:click')
  onDocumentClick(): void {
    this.openMenuId = null;
  }

  onFileDelete(idFile : number) : void {
    console.log("click : " + idFile);

    this.vaultService.deleteFile(idFile).subscribe( {
      next : () => {
        this.fichiers = this.fichiers.filter(f => f.idFichier !== idFile);
      },
      error : err => console.error("Erreur de suppression fichier " , err)

    });
  }

  onDossierDelete(idDossier: number): void {
    this.vaultService.deleteDossier(idDossier).subscribe({
      next: () => {
        this.dossiers = this.dossiers.filter(d => d.idDossier !== idDossier); // ✅ Retire localement
      },
      error: (err) => console.error('Erreur suppression dossier', err)
    });
  }

  addDossier(nomDossier: string): void {
    this.vaultService.addDossier(nomDossier).subscribe({
      next: (dto) => {
        this.dossiers.push({
          idDossier: dto.idDossier,
          nomDuDossier: dto.nomDossier
        });
        this.ngOnInit();
      },
      error: (err) => console.error('Erreur ajout dossier', err)
    });
  }

  onModifyDossier(nomDossier: string, idDossier: number): void {
    this.vaultService.modifyDossier(idDossier, nomDossier).subscribe({
      next: () => {
        const dossier = this.dossiers.find(d => d.idDossier === idDossier);
        if (dossier) dossier.nomDuDossier = nomDossier;
        this.editingDossierId = null;
      },
      error: (err) => console.error('Erreur modification dossier', err)
    });
  }

  onModifyFile(nomFichier: string, idFile: number): void {
    this.vaultService.updateFilename(idFile, nomFichier).subscribe({
      next: () => {
        const fichier = this.fichiers.find(f => f.idFichier === idFile);
        if (fichier) fichier.nomOriginal = nomFichier;
        this.editingFichierId = null;
      },
      error: (err) => {
        console.error('Erreur modification fichier', err);
        this.editingFichierId = null;
      }

    });
  }



  onDossierClick(idDossier: number): void {
    if (this.dossierOuvertId === idDossier) {
      this.dossierOuvertId = null;
      return;
    }

    this.dossierOuvertId = idDossier;
    this.loadingFichiers = true;
    this.fichiers = [];
    if (this.fichiersParDossier[idDossier] !== undefined) return;

    this.loadingFichiers = true;
    this.vaultService.getFichiers(idDossier).subscribe({
      next: (fichiers) => {
        this.fichiersParDossier[idDossier] = fichiers;
        this.loadingFichiers = false;
      },
      error: (err) => {
        console.error(err);
        this.loadingFichiers = false;
      }
    });
  }

  ngOnInit(): void {
    this.loadDossiers();
  }

  loadDossiers(): void {
    this.vaultService.getDossier().subscribe({
      next: (dossiers) => {
        this.dossiers = dossiers;

        if (this.dossiers && this.dossiers.length > 0) {
          this.selectedDossierId = this.dossiers[0].idDossier;
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement des dossiers :', err);
      }
    });
  }


  constructor(private router: Router, private readonly ngZone: NgZone) {

  }
  onClickDecryptFile(fichier: Fichier): void {
    // 1. On active le spinner de chargement au début de l'action
    this.loadingDecrypt = true;
    this.error = null; // On réinitialise les anciennes erreurs potentielles

    this.vaultService.decryptFile(fichier.idFichier).subscribe({
      next: async (blob) => {
        try {
          const buffer = await blob.arrayBuffer();

          this.ngZone.run(() => {
            this.resultat = new Uint8Array(buffer);
            this.loadingDecrypt = false; // Désactive le spinner après le traitement réussi


            this.router.navigate(['/viewer', fichier.idFichier]);
          });
        } catch (err) {
          this.ngZone.run(() => {
            console.error('Erreur conversion buffer', err);
            this.error = 'Erreur lors du traitement du fichier décrypté.';
            this.loadingDecrypt = false;
          });
        }
      }, // <-- La virgule de séparation qui manquait ici
      error: (err) => {
        this.ngZone.run(() => {
          console.error('Erreur décryptage', err);
          this.error = 'Erreur lors du décryptage du fichier.';
          this.loadingDecrypt = false; // Désactive le spinner même s'il y a un échec réseau
        });
      }
    });
  }

    onOpenVault(): void {
    this.vaultService.openVault( this.motDePasse).subscribe({
      next: () => {
        this.vaultOpen = true;
        this.motDePasse = ''; //
        this.vaultService.unlock();
        this.loadingVault = true;
        this.loadDossiers();
        this.ngOnInit();
      },
      error: (err) => {
        this.error = "Mot de passe incorrect ou erreur technique.";
        console.error(err);
      }
    });
  }



  onEncrypt(): void {
    if (!this.selectedFile || !this.selectedDossierId) return;

    this.vaultService.encryptFile( this.selectedFile , this.selectedDossierId ).subscribe({
      next: (blob) => {

        this.isSuccess = true;
        console.log('Chiffrement réussi, isSuccess vaut :', this.isSuccess);
        this.loadDossiers();

        this.selectedFile = null;
      },
      error: (err) => {
        this.isSuccess = false;
        if (err.status === 401) {
          this.vaultService.lock();
        } else {
          this.error = "Une erreur est survenue lors du chiffrement.";
        }
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectionnerFichier(input.files[0]);

    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();  // indispensable pour autoriser le drop
    this.isDragOver = true;
  }

  onDragLeave(): void {
    this.isDragOver = false;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver = false;

    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      this.selectionnerFichier(files[0]);
    }
  }

  private selectionnerFichier(fichier: File): void {
    if (!fichier.name.endsWith('.pdf')) {
      this.error = 'Seuls les fichiers .pdf sont acceptés';
      return;
    }
    this.selectedFile = fichier;
    this.error = '';


    //this.nomFichierSortie = fichier.name.replace('.txt', '');
  }
  fermerVisionneuse(): void {
    this.resultat = null;
    this.error = null;
  }


  onLock() {
    this.vaultService.lock();
  }
}
  /*
    loadSecrets() {
      const userId = this.getUserId();
      if (userId === 0) {
        this.error = "Utilisateur non connecté.";
        return;
      }
      this.vaultService.getUserSecrets(userId).subscribe({
        next: (data) => {
          this.secrets = data;
        },
        error: (err) => {
          this.error = "Erreur lors du chargement des paramètres de sécurité.";
          console.error(err);
        }
      });
    }
  async onUnlock() {
    if (!this.motDePasse|| !this.secrets) return;

    //this.chargement = true;
    this.error = null;

    try {
      await this.vaultService.unlock(
        this.motDePasse,
        this.secrets.salt,
        this.secrets.masterKey
      );
      this.motDePasse = ''; // Sécurité : on efface le mot de passe
    } catch (e) {
      this.error = "Mot de passe maître incorrect ou erreur de déchiffrement.";
      console.error(e);
    } finally {
      //this.chargement = false;
    }
  }
  /*async onUnlock(secret: SecretDetailResponse) {
    this.loading = true;
    this.error = null;
    try {
      const secrets = await firstValueFrom(this.vaultService.getUserSecrets(this.getUserId()));
      this.motDePasse = secret.passwordHash;
      console.error("Secrets reçus :", secrets);
    } catch (e) {
      this.error = "Mot de passe incorrect ou erreur technique.";
    } finally {
      this.loading = false;
    }
  }

  async downloadFile(fichierId: number) {
    try {
      const decryptedData = await this.vaultService.decryptFile(fichierId);
      this.vaultService. telechargerFichierChiffre(decryptedData);
    } catch (e) {
      this.error = "Erreur de déchiffrement : le coffre est peut-être verrouillé.";
    }
  }

*/



