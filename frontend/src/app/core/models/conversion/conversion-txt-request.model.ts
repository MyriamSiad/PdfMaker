
export class ConversionTxtRequest {

  nomFichierSortie :  string = "";
  cheminFichier: string = "";
  fichier : File | null = null;


  constructor(cheminFichier: string, cheminSortie: string , fichier: File | null = null) {
    this.cheminFichier = cheminFichier;
   this.nomFichierSortie  =  fichier?.name.replace('.txt', '') || cheminSortie;
   this.fichier = fichier;

  }


}
