import {ActivatedRoute, Router} from '@angular/router';
import {CoffreFortService} from '@services/coffre-fort-service';
import {Component, inject, NgZone, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {NgxExtendedPdfViewerModule} from 'ngx-extended-pdf-viewer';
import {MatIcon} from '@angular/material/icon';


@Component({
  selector: 'app-viewer',
  standalone: true,
  imports: [CommonModule, FormsModule, NgxExtendedPdfViewerModule, MatIcon],
  templateUrl: 'pdf-viewer.component.html',
  //styleUrls: ['./vault.component.css']
})
export class PdfViewerComponent implements OnInit {
  resultat: Uint8Array | null = null;
  loading = false;
  error: string | null = null;

  private readonly route = inject(ActivatedRoute);
 // private readonly router = inject(Router);

  private readonly ngZone = inject(NgZone);
  private  readonly coffreFortService = inject (CoffreFortService)


  /*ngOnInit(): void {
    const idFichier = Number(this.route.snapshot.paramMap.get('idFichier'));
    this.loading = true;

    this.coffreFortService.decryptFile(idFichier).subscribe({
      next: async (blob) => {
        const buffer = await blob.arrayBuffer();
        this.ngZone.run(() => {
          this.resultat = new Uint8Array(buffer);
          this.loading = false;
        });
      },
      error: () => {
        this.error = 'Erreur lors du déchiffrement.';
        this.loading = false;
      }
    });
  }

  retour(): void {
    window.history.back();
  }*/

  ngOnInit(): void {
    // 1. On récupère l'idFichier depuis l'URL (ex: /viewer/:id)
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam || isNaN(+idParam)) {
      this.error = "Identifiant de fichier invalide ou manquant.";
      this.loading = false;
      return;
    }

    const idFichier: number = +idParam;

    // 2. On lance le décryptage immédiatement
    this.decrypterLeFichier(idFichier);
  }

  private decrypterLeFichier(idFichier: number): void {
    this.loading = true;
    this.error = null;

    this.coffreFortService.decryptFile(idFichier).subscribe({
      next: async (blob) => {
        try {
          const buffer = await blob.arrayBuffer();
          this.ngZone.run(() => {
            this.resultat = new Uint8Array(buffer);
            this.loading = false;
          });
        } catch (err) {
          this.ngZone.run(() => {
            console.error('Erreur conversion buffer viewer', err);
            this.error = 'Impossible de lire le document déchiffré.';
            this.loading = false;
          });
        }
      },
      error: (err) => {
        this.ngZone.run(() => {
          console.error('Erreur décryptage viewer', err);
          this.error = 'Erreur lors du déchiffrement sécurisé du PDF.';
          this.loading = false;
        });
      }
    });
  }
  retour(): void {
    window.history.back();
  }

}
