import { Component } from '@angular/core';
import * as pdfjsLib from 'pdfjs-dist';
import { PdfToolsService } from '@services/pdf-tools.service';
import {FormsModule} from '@angular/forms';
import {MatIcon} from '@angular/material/icon';
import {logFrontendError} from '@services/error-logger.service';
pdfjsLib.GlobalWorkerOptions.workerSrc = './assets/pdf.worker.min.mjs';

interface PageThumbnail {
  pageNumber: number;
  dataUrl: string;
  selected: boolean;
}
pdfjsLib.GlobalWorkerOptions.workerSrc = new URL(
  'pdfjs-dist/build/pdf.worker.min.mjs',
  import.meta.url
).toString();

@Component({
  selector: 'app-split-pdf',
  imports: [
    FormsModule,
    MatIcon
  ],
  templateUrl: './separation.component.html'
})
export class SeparationComponent {
  splitFile: File | null = null;
  isDragOver = false;
  splitting = false;
  pages: PageThumbnail[] = [];
  outputName = 'selection.pdf';
  isSuccess : boolean = false;
  errorMessage: string | null = null;



  constructor(private pdfToolsService: PdfToolsService) {}

  previewPage: PageThumbnail | null = null;
  async onSplitFileSelected(event: Event): Promise<void> {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;

    if (file.type !== 'application/pdf') {
      const error = new Error(`Format invalide : "${file.type || 'inconnu'}" n'est pas un PDF`);
      error.name = 'InvalidFileTypeError';
      await logFrontendError(error, '/pdf/split/file-select');
      this.isSuccess = false;
      this.errorMessage = 'Le fichier sélectionné n\'est pas un PDF.';
      return;
    }

    this.splitFile = file;
    await this.generateThumbnails(file);
  }




  // NOUVEAU : Ouvrir la vue en grand
  openPreview(event: Event, page: PageThumbnail): void {
    event.stopPropagation(); // Évite de déclencher le clic de sélection en même temps
    this.previewPage = page;
  }

  // NOUVEAU : Fermer la vue en grand
  closePreview(): void {
    this.previewPage = null;
  }

  async onDropSplit(event: DragEvent): Promise<void> {
    event.preventDefault();
    this.isDragOver = false;
    const file = event.dataTransfer?.files?.[0];
    if (!file) return;

    if (file.type !== 'application/pdf') {
      const error = new Error(`Format invalide : "${file.type || 'inconnu'}" n'est pas un PDF`);
      error.name = 'InvalidFileTypeError';
      await logFrontendError(error, '/pdf/split/drag-and-drop');
      this.isSuccess = false;
      this.errorMessage = 'Seuls les fichiers PDF sont acceptés.';
      return;
    }

    this.splitFile = file;
    await this.generateThumbnails(file);
  }

  async generateThumbnails(file: File): Promise<void> {
    this.pages = [];
    const arrayBuffer = await file.arrayBuffer();
    const pdf = await pdfjsLib.getDocument({ data: arrayBuffer }).promise;

    for (let i = 1; i <= pdf.numPages; i++) {
      const page = await pdf.getPage(i);
      const viewport = page.getViewport({ scale: 1.5 });

      const canvas = document.createElement('canvas');
      canvas.width = viewport.width;
      canvas.height = viewport.height;

      await page.render({
        canvasContext: canvas.getContext('2d')!,
        viewport,
        canvas: canvas
      }).promise;

      this.pages.push({
        pageNumber: i,
        dataUrl: canvas.toDataURL(),
        selected: false
      });
    }
  }

  togglePage(page: PageThumbnail): void {
    page.selected = !page.selected;
  }

  selectAll(): void {
    this.pages.forEach(p => p.selected = true);
  }

  deselectAll(): void {
    this.pages.forEach(p => p.selected = false);
  }

  get selectedCount(): number {
    return this.pages.filter(p => p.selected).length;
  }

  async onExtract(): Promise<void> {
    if (!this.splitFile) return;
    this.splitting = true;

    try {
      const selectedPages = this.pages
        .filter(p => p.selected)
        .map(p => ({ start: p.pageNumber, end: p.pageNumber, nom: '' }));

      const results = await this.pdfToolsService.splitPdf(this.splitFile, selectedPages);

      const merged = await this.pdfToolsService.mergePdfs(
        results.map(r => new File([r.bytes as BlobPart], r.nom, { type: 'application/pdf' }))
      );

      this.pdfToolsService.download(merged, this.outputName || 'selection.pdf');
      this.isSuccess = true;

    } catch (error) {
      await logFrontendError(error as Error, '/pdf/extract');
      this.isSuccess = false;
      // afficher un message d'erreur à l'utilisateur si besoin
    } finally {
      this.splitting = false;
    }
  }
  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver = true;
  }

  getSelectedPagesList(): string {
    const selected = this.pages.filter(p => p.selected).map(p => p.pageNumber);
    if (selected.length === 0) return 'Aucune';
    return 'Page ' + selected.join(', ');
  }




}
