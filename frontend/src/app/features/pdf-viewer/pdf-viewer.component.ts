import {ActivatedRoute} from '@angular/router';
import {CoffreFortService} from '@services/coffre-fort-service';
import {Component, NgZone, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {NgxExtendedPdfViewerModule} from 'ngx-extended-pdf-viewer';

class VaultService {
}
@Component({
  selector: 'app-vault',
  standalone: true,
  imports: [CommonModule, FormsModule, NgxExtendedPdfViewerModule],
  templateUrl: 'pdf-viewer.component.html',
  //styleUrls: ['./vault.component.css']
})
export class PdfViewerComponent implements OnInit {
  resultat: Uint8Array | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private coffreFortService: CoffreFortService,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
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
  }
}
