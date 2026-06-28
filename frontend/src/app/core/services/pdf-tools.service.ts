import { Injectable } from '@angular/core';
import { PDFDocument } from 'pdf-lib';
import {logFrontendError} from '@services/error-logger.service';

@Injectable({ providedIn: 'root' })
export class PdfToolsService {

  // ── FUSION ────────────────────────────────────────────────────────
  async mergePdfs(files: File[]): Promise<Uint8Array> {
    try {
      const mergedPdf = await PDFDocument.create();

      for (const file of files) {
        try {
          const bytes = await file.arrayBuffer();
          const pdf = await PDFDocument.load(bytes);
          const pages = await mergedPdf.copyPages(pdf, pdf.getPageIndices());
          pages.forEach(page => mergedPdf.addPage(page));
        } catch (error) {
          await logFrontendError(error as Error, `/pdf/merge/file/${file.name}`);
          throw error; // on remonte l'erreur pour arrêter la fusion
        }
      }

      return mergedPdf.save();

    } catch (error) {
      await logFrontendError(error as Error, '/pdf/merge');
      throw error;
    }
  }


  async splitPdf(
    file: File,
    ranges: { start: number; end: number; nom: string }[]
  ): Promise<{ nom: string; bytes: Uint8Array }[]> {
    try {
      const bytes = await file.arrayBuffer();
      const pdf = await PDFDocument.load(bytes);
      const totalPages = pdf.getPageCount();
      const results: { nom: string; bytes: Uint8Array }[] = [];

      for (const range of ranges) {
        try {
          const newPdf = await PDFDocument.create();

          const start = Math.max(0, range.start - 1);
          const end = Math.min(totalPages - 1, range.end - 1);
          const indices = Array.from({ length: end - start + 1 }, (_, i) => start + i);

          const pages = await newPdf.copyPages(pdf, indices);
          pages.forEach(page => newPdf.addPage(page));

          results.push({
            nom: range.nom,
            bytes: await newPdf.save()
          });
        } catch (error) {
          await logFrontendError(error as Error, `/pdf/split/range/${range.nom}`);
          throw error; // on remonte pour arrêter le split
        }
      }

      return results;

    } catch (error) {
      await logFrontendError(error as Error, '/pdf/split');
      throw error;
    }
  }


  download(bytes: Uint8Array, filename: string): void {

    const blob = new Blob([bytes.buffer as ArrayBuffer], { type: 'application/pdf' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(url);
  }


  async getPageCount(file: File): Promise<number> {
    const bytes = await file.arrayBuffer();
    const pdf = await PDFDocument.load(bytes);
    return pdf.getPageCount();
  }
}
