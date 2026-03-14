package fr.pdfmaker.backend.model.pdf;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ModelAttribute;




public class TxtConversionModel {



        private final  String pageFormat   = "A4";
        private final String  orientation  =  "portait";
        private final String Font = "COURIER";
        private final double   marginTop    = 20.0;
        private final   double  marginBottom = 20.0;
        private final  double marginLeft   = 20.0;
        private  final  double marginRight  = 20.0;
        private final   double  lineSpacing  = 1.5;
        private final  boolean  showPageNumbers = true;
        private  final String encoding     = "UTF-8";

}
