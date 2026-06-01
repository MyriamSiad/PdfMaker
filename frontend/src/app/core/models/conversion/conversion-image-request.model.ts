export class ConversionImageRequestModel{

 nomFichierSortie : string = "";

 adapterALaPage : boolean;

 cheminFichier: string = "";

  fichier : File | null = null;

  constructor(nomFichierSortie: string, adapterALaPage: boolean, fichier : File ) {
    this.cheminFichier = fichier?.webkitRelativePath || "";
    this.nomFichierSortie = nomFichierSortie;
    this.adapterALaPage = adapterALaPage;
    this.fichier = fichier;

  }

}

