import {Component, NgModule} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NgClass} from '@angular/common';
import {PdfToolsService} from '@services/pdf-tools.service';
import {FormsModule, NgModel} from '@angular/forms';
import {NgxExtendedPdfViewerModule} from 'ngx-extended-pdf-viewer';
import {MatIcon} from '@angular/material/icon';
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {logFrontendError} from '@services/error-logger.service';

@Component({
  selector: 'app-fusion',
  imports: [
    FormsModule,
    NgxExtendedPdfViewerModule,
    MatIcon,
    CdkDropList,
    CdkDrag

  ],
  templateUrl: './fusion.component.html',
  styleUrl:'./fusion.component.css'
})
export class FusionComponent {

  isSuccess : boolean = false ;
  pdfToolsService : PdfToolsService = new PdfToolsService();
selectedFiles: File[] = [];
  previewIndex: number | null = null;

  togglePreview(i: number): void {
    this.previewIndex = this.previewIndex === i ? null : i;
  }

  mergeFiles: File[] = [];
  mergeOutputName = 'fusion.pdf';
  isDragOver = false;
  merging = false;

  onFilesSelected(event: Event): void {
    const files = Array.from((event.target as HTMLInputElement).files || []);
    this.mergeFiles.push(...files);
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver = true;
  }
  dropCard(event: CdkDragDrop<File[]>): void {
    moveItemInArray(this.mergeFiles, event.previousIndex, event.currentIndex);
    this.previewIndex = null; // Optionnel : ferme l'aperçu pour éviter les décalages visuels
  }
  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver = false;
    const files = Array.from(event.dataTransfer?.files || []);
    this.mergeFiles.push(...files.filter(f => f.type === 'application/pdf'));
  }

  moveUp(i: number): void {
    if (i === 0) return;
    [this.mergeFiles[i - 1], this.mergeFiles[i]] = [this.mergeFiles[i], this.mergeFiles[i - 1]];
  }

  moveDown(i: number): void {
    if (i === this.mergeFiles.length - 1) return;
    [this.mergeFiles[i], this.mergeFiles[i + 1]] = [this.mergeFiles[i + 1], this.mergeFiles[i]];
  }

  removeFile(i: number): void {
    this.mergeFiles.splice(i, 1);
  }

  async onMerge(): Promise<void> {
    this.merging = true;

    try {
      const bytes = await this.pdfToolsService.mergePdfs(this.mergeFiles);
      this.pdfToolsService.download(bytes, this.mergeOutputName || 'fusion.pdf');
      this.isSuccess = true;
      window.scrollTo({ top: 0, behavior: 'smooth' });

    } catch (error) {
      await logFrontendError(error as Error, '/pdf/merge');
      this.isSuccess = false;
      // afficher un message d'erreur à l'utilisateur si besoin
    } finally {
      this.merging = false;
    }
  }

}
