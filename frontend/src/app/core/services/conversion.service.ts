import {HttpClient} from '@angular/common/http';
import {ConversionTxtResponse} from '@core/models/conversion/conversion-txt-response.model';
import {ConversionTxtRequest} from '@core/models/conversion/conversion-txt-request.model';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class  ConversionService {

  private readonly API_URL = 'http://localhost:8080/api/rest/pdf/conversion';
  constructor(private http: HttpClient) {}


  convertirTxtEnPdf(request : ConversionTxtRequest) : Observable<ConversionTxtResponse>  {

    const formData = new FormData();
    formData.append('fichier', request.nomFichierSortie);
    formData.append('nomFichierSortie', request.nomFichierSortie);
    formData.append('fichier', request.fichier as File );

    return this.http.post<ConversionTxtResponse>(`${this.API_URL}/txt-to-pdf`, formData);
  }


  telechargerPdf(resultat: ConversionTxtResponse): void {

    const byteCharacters = atob(resultat.fichierPdf);

    const byteNumbers = Array.from(byteCharacters)
      .map(char => char.charCodeAt(0));
    const byteArray = new Uint8Array(byteNumbers);

    const blob = new Blob([byteArray], { type: 'application/pdf' });


    const url = URL.createObjectURL(blob);
    const lien = document.createElement('a');
    lien.href = url;
    lien.download = resultat.outputPath;
    lien.click();


    URL.revokeObjectURL(url);
  }


}
