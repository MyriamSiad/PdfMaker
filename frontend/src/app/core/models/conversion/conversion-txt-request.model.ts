import {createJitResourceTransformer} from '@angular/build/private';

export class ConversionTxtRequest {

  nomFichierSortie :  string = "";
  cheminFichier: string = "";
  fichier : File | null = null;

  constructor(cheminFichier: string, cheminSortie: string , fichier: File | null = null) {
    this.cheminFichier = fichier?.webkitRelativePath || cheminFichier;
   this.nomFichierSortie  =  fichier?.name.replace('.txt', '') || cheminSortie;
   this.fichier = fichier;

  }


}
