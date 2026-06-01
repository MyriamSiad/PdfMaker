package fr.pdfmaker.backend.service.dossier;

import fr.pdfmaker.backend.model.entity.DossierVirtuel;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.repository.IDossierVirtuelRepository;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class DossierStorageService {

    @Autowired
    private  IDossierVirtuelRepository dossierRepository;

    @Autowired
    private IUtilisateurRepository utilisateurRepository;


    @Transactional
    public DossierVirtuel initialiserDossier(Long idUser, String nomDuDossier) throws Exception {

        Utilisateur utilisateur = utilisateurRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));




        try {
            DossierVirtuel  dossier = new DossierVirtuel ();
            dossier.setNomDuDossier(nomDuDossier);
            dossier.setUtilisateur(utilisateur);
            dossier.setIsSystem(true);


           return dossier = dossierRepository.save(dossier);


        } catch (Exception e) {

            throw new Exception("Impossible de créer le dossier physique sur le disque : " + e.getMessage(), e);
        }


    }


}
