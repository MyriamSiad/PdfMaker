export class ConversionResponseModel{
  success :  boolean ;
  fichierPdf : string = "" ;
  outputPath : string = ""
   message : string = "";

  constructor(success: boolean, outputPath: string, message: string , fichierPdf : string) {
    this.success = success;
    this.fichierPdf = fichierPdf;
    this.outputPath = outputPath;
    this.message = message;
  }
}
