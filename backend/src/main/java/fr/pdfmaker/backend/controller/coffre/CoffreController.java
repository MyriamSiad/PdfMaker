package fr.pdfmaker.backend.controller.coffre;


import fr.pdfmaker.backend.service.coffrefort.CoffreFortService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin (origins = "http://localhost:4200")
@RequestMapping("/api/rest/coffre-fort")
public class CoffreController {

    @Qualifier("encryptCoffreService")
    private CoffreFortService coffreFortService;
}
