package fr.pdfmaker.backend.mongo.controller;


import fr.pdfmaker.backend.mongo.service.LoginHistoriqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/loginHistorique")
public class LoginHistoriqueController {

    @Autowired
    private  LoginHistoriqueService loginHistoriqueService;



}
