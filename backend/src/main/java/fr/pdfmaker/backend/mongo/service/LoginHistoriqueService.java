package fr.pdfmaker.backend.mongo.service;

import fr.pdfmaker.backend.mongo.repository.ILoginHistoriqueRepository;

import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;



@Service

public class LoginHistoriqueService {

    private final IUtilisateurRepository utilisateurRepository;


    private  final ILoginHistoriqueRepository loginHistoriqueRepository1;

    public LoginHistoriqueService(IUtilisateurRepository utilisateurRepository, ILoginHistoriqueRepository loginHistoriqueRepository1) {
        this.utilisateurRepository = utilisateurRepository;
        this.loginHistoriqueRepository1 = loginHistoriqueRepository1;
    }


}
