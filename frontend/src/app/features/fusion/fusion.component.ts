import {Component, NgModule} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NgClass} from '@angular/common';
import {PdfToolsService} from '@services/pdf-tools.service';
import {FormsModule, NgModel} from '@angular/forms';
import {NgxExtendedPdfViewerModule} from 'ngx-extended-pdf-viewer';

@Component({
  selector: 'app-fusion',
  imports: [
    FormsModule,
    NgxExtendedPdfViewerModule

  ],
  templateUrl: './fusion.component.html'
})
export class FusionComponent {
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
    const bytes = await this.pdfToolsService.mergePdfs(this.mergeFiles);
    this.pdfToolsService.download(bytes, this.mergeOutputName || 'fusion.pdf');
    this.merging = false;
  }

}
