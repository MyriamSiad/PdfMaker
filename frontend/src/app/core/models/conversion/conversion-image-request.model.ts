export class ConversionImageRequestModel{

 nomFichierSortie : string = "";

 adapterALaPage : boolean;

  fichier : File | null = null;

  constructor(nomFichierSortie: string, adapterALaPage: boolean, fichier : File ) {
    this.nomFichierSortie = nomFichierSortie;
    this.adapterALaPage = adapterALaPage;
    this.fichier = fichier;

  }

}

