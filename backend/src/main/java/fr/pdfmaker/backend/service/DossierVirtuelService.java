package fr.pdfmaker.backend.service;


import fr.pdfmaker.backend.model.entity.DossierVirtuel;
import fr.pdfmaker.backend.model.entity.Fichier;
import fr.pdfmaker.backend.repository.IDossierVirtuelRepository;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


public class DossierVirtuelService {

    private final  IDossierVirtuelRepository dossierVirtuelRepository;

    private final IUtilisateurRepository utilisateurRepository;

    public DossierVirtuelService(IDossierVirtuelRepository dossierVirtuelRepository, IUtilisateurRepository utilisateurRepository) {
        this.dossierVirtuelRepository = dossierVirtuelRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    DossierVirtuel getDossierVirtuelById(Long idDossier) throws Exception {
        try {
            return dossierVirtuelRepository.findByidDossier(idDossier);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération du dossier virtuel : " + e.getMessage());
        }
    }
    public DossierVirtuel creerDossier(String nomDuDossier, Long idUtilisateur, Long idDossierParent){
        DossierVirtuel dossierVirtuel = new DossierVirtuel();
        try{
            dossierVirtuel.setNomDuDossier(nomDuDossier);
            dossierVirtuel.setUtilisateur(utilisateurRepository.findById(idUtilisateur).orElseThrow());

            return  dossierVirtuelRepository.save(dossierVirtuel);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DossierVirtuel renommerDossier(Long idDossier, String nouveauNom){

        try{
            DossierVirtuel dossierVirtuel = getDossierVirtuelById(idDossier);
            dossierVirtuel.setNomDuDossier(nouveauNom);
            return dossierVirtuelRepository.save(dossierVirtuel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void supprimerDossier(Long idDossier){
        try{
            DossierVirtuel dossierVirtuel = getDossierVirtuelById(idDossier);
            dossierVirtuelRepository.delete(dossierVirtuel);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }



    public DossierVirtuel ajouterFichier(Long idDossier, Fichier fichier){
        try{
            DossierVirtuel  dossierVirtuel = getDossierVirtuelById(idDossier);
            dossierVirtuel.getFichiers().add(fichier);
            dossierVirtuelRepository.save(dossierVirtuel);
            return dossierVirtuel;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public DossierVirtuel retirerFichier(Long idDossier, Long idFichier){
        try {
          DossierVirtuel dossierVirtuel = getDossierVirtuelById(idDossier);
          dossierVirtuel.getFichiers().removeIf(fichier -> fichier.getIdFichier().equals(idFichier));
         return  dossierVirtuelRepository.save(dossierVirtuel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Set<Fichier> getFichiersParDossier(Long idDossier){

        try {
            DossierVirtuel dossierVirtuel = getDossierVirtuelById(idDossier);
            return dossierVirtuel.getFichiers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Set<DossierVirtuel> getDossiersParUtilisateur(Long idUtilisateur){
        try{
            DossierVirtuel dossierVirtuel = dossierVirtuelRepository.findByUtilisateur(utilisateurRepository.findById(idUtilisateur).orElseThrow());

            Set<DossierVirtuel> dossierVirtuels = new HashSet<>();
            for(DossierVirtuel sousDossier : dossierVirtuel.getUtilisateur().getDossiers()){
                dossierVirtuels.add(sousDossier);
            }
            return dossierVirtuels;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
