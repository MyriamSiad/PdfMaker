import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ConversionService } from '@core/services/conversion.service';
import {ConversionTxtResponse} from '@core/models/conversion/conversion-txt-response.model';
import {ConversionTxtRequest} from '@core/models/conversion/conversion-txt-request.model';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-txt-to-pdf',
  imports: [FormsModule, NgClass],
  templateUrl: './conversion.txt.component.html',
})
export class TxtToPdfComponent {

  fichier: File | null = null;
  nomFichierSortie: string = '';
  erreur: string = '';
  success: boolean = false;
  chargement: boolean = false;
  isDragOver: boolean = false;

  constructor(private conversionService: ConversionService) {}


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
    if (!fichier.name.endsWith('.txt')) {
      this.erreur = 'Seuls les fichiers .txt sont acceptés';
      return;
    }
    this.fichier = fichier;
    this.erreur = '';


    this.nomFichierSortie = fichier.name.replace('.txt', '');
  }

  onSubmit(): void {
    if (!this.fichier || !this.nomFichierSortie) return;

    this.chargement = true;
    this.erreur = '';
    this.success = false;


      const conversionTxtRequest  : ConversionTxtRequest = new ConversionTxtRequest(
        this.fichier.webkitRelativePath , this.nomFichierSortie  , this.fichier)

    this.conversionService.convertirTxtEnPdf( conversionTxtRequest).subscribe({
      next: (resultat: ConversionTxtResponse) => {
        this.chargement = false;
        this.success = true;
        this.conversionService.telechargerPdf(resultat);
      },
      error: (err: any) => {
        this.chargement = false;
        this.erreur = err.error?.message || 'Échec de la conversion !';
      }
    });
  }
}
