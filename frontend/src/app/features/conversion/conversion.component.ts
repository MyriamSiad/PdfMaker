
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';
import { ConversionService } from '@services/conversion.service';
import { ConversionImageRequestModel } from '@core/models/conversion/conversion-image-request.model';
import { ConversionResponseModel } from '@core/models/conversion/conversion-response.model';
import { ConversionTxtRequest } from '@core/models/conversion/conversion-txt-request.model';
import { ConversionTxtResponse } from '@core/models/conversion/conversion-txt-response.model';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-conversion',
  standalone: true,
  imports: [FormsModule, NgClass, MatIcon],
  templateUrl: './conversion.component.html' // Remplace par le nom de ton unique fichier HTML global
})
export class ConversionComponent {
  // Gestion globale du mode de l'interface
  mode: 'txt' | 'image' = 'txt';

  // Variables partagées par le formulaire unique
  fichier: File | null = null;
  nomFichierSortie: string = '';
  erreur: string = '';
  success: boolean = false;
  chargement: boolean = false;
  isDragOver: boolean = false;

  // Option spécifique aux images
  adapterALaPage: boolean = true;

  constructor(private conversionService: ConversionService) {}

  // Réinitialise l'état du formulaire quand l'utilisateur change d'onglet
  changerMode(nouveauMode: 'txt' | 'image'): void {
    this.mode = nouveauMode;
    this.fichier = null;
    this.nomFichierSortie = '';
    this.erreur = '';
    this.success = false;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.traiterFichier(input.files[0]);
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
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
      this.traiterFichier(files[0]);
    }
  }

  private traiterFichier(file: File): void {
    const nameLower = file.name.toLowerCase();
    this.erreur = '';
    this.success = false;

    if (this.mode === 'txt') {
      if (!nameLower.endsWith('.txt')) {
        this.erreur = 'Seuls les fichiers .txt sont acceptés';
        this.fichier = null;
        return;
      }
      this.fichier = file;
      this.nomFichierSortie = file.name.replace(/\.txt$/i, '').trim();
    }

    else if (this.mode === 'image') {
      if (!nameLower.endsWith('.jpg') && !nameLower.endsWith('.jpeg') && !nameLower.endsWith('.png') && !nameLower.endsWith('.webp')) {
        this.erreur = 'Seuls les fichiers .jpeg, .jpg, .webp, .png sont acceptés';
        this.fichier = null;
        return;
      }
      this.fichier = file;
      this.nomFichierSortie = file.name.replace(/\.(jpeg|jpg|png|webp)$/i, '').trim();
    }
  }

  onSubmit(): void {
    if (!this.fichier || !this.nomFichierSortie) return;

    this.chargement = true;
    this.erreur = '';
    this.success = false;

    if (this.mode === 'txt') {
      const request = new ConversionTxtRequest(
        this.fichier.webkitRelativePath,
        this.nomFichierSortie,
        this.fichier
      );

      this.conversionService.convertirTxtEnPdf(request).subscribe({
        next: (res: ConversionTxtResponse) => this.handleSuccess(res),
        error: (err: any) => this.handleError(err)
      });
    }

    else if (this.mode === 'image') {
      const request = new ConversionImageRequestModel(
        this.nomFichierSortie,
        this.adapterALaPage,
        this.fichier
      );

      this.conversionService.convertirImageEnPdf(request).subscribe({
        next: (res: ConversionResponseModel) => this.handleSuccess(res),
        error: (err: any) => this.handleError(err)
      });
    }
  }

  private handleSuccess(resultat: any): void {
    this.chargement = false;
    this.success = true;
    this.conversionService.telechargerPdf(resultat);
  }

  private handleError(err: any): void {
    this.chargement = false;
    this.erreur = err.error?.message || 'Échec de la conversion !';
  }
}
