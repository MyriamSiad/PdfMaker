import {FormsModule} from '@angular/forms';
import {NgClass} from '@angular/common';
import {ConversionService} from '@services/conversion.service';
import {Component} from '@angular/core';
import {ConversionImageRequestModel} from '@core/models/conversion/conversion-image-request.model';
import {ConversionResponseModel} from '@core/models/conversion/conversion-response.model';

@Component({
  selector : 'app-image-to-pdf',
  imports : [FormsModule, NgClass],
  templateUrl : './image-conversion.html'

})

export class ImageToPdfComponent {

  fichier : File  | null = null;
  nomFichierSortie : string = "";
  erreur: string = '';
  success: boolean = false;
  chargement: boolean = false;
  isDragOver: boolean = false;
  adapterALaPage: boolean = false;


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
    if (!fichier.name.endsWith('.jpg') && !fichier.name.endsWith('.jpeg') && !fichier.name.endsWith('.png')) {
      this.erreur = 'Seuls les fichiers .txt sont acceptés';
      return;
    }
    this.fichier = fichier;
    this.erreur = '';


    this.nomFichierSortie = fichier.name.replace('.jpeg', '');
    this.nomFichierSortie = fichier.name.replace('.png', '');
  }

  onSubmit(): void {
    if (!this.fichier || !this.nomFichierSortie) return;

    this.chargement = true;
    this.erreur = '';
    this.success = false;


    const  conversionImageRequest  : ConversionImageRequestModel = new ConversionImageRequestModel(
      this.nomFichierSortie, this.adapterALaPage,  this.fichier)

    this.conversionService.convertirImageEnPdf(  conversionImageRequest).subscribe({
      next: (resultat: ConversionResponseModel) => {
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
