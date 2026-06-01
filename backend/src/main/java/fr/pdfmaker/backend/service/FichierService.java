package fr.pdfmaker.backend.service;


import fr.pdfmaker.backend.model.dto.fichier.FichierDto;
import fr.pdfmaker.backend.model.entity.Fichier;
import fr.pdfmaker.backend.repository.IFichierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FichierService {

    private final  IFichierRepository fichierRepository;


    public FichierService(IFichierRepository fichierRepository) {

        this.fichierRepository = fichierRepository;

    }

    public boolean validCheminLocal (String cheminLocal) throws Exception {
        try {
            if (cheminLocal == null || cheminLocal.isEmpty()) {
                throw new IllegalArgumentException("Le chemin local ne peut pas être vide.");
            }
            return true;
        } catch (Exception e) {
            throw new Exception("Validation du chemin local échouée : " + e.getMessage());
        }
    }

     public Fichier saveFichier(Fichier fichier) throws Exception {
        try {

            return fichierRepository.save(fichier);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'enregistrement du fichier : " + e.getMessage());
        }
    }

    public Fichier getFichierById(Long idFichier) throws Exception {
        try{
           return  fichierRepository.findByidFichier(idFichier);
        }catch(Exception e){
            throw new Exception("Erreur lors de la récupération du fichier : " + e.getMessage());
        }
    }

    public void deleteFichierById(Long idFichier) throws Exception {
        fichierRepository.deleteById(idFichier);
    }

    @Transactional
    public Fichier updateFichier(Long idFichier, String nouveauNom) throws Exception {
        try {
            Fichier fichier = getFichierById(idFichier);
            fichier.setNomOriginal(nouveauNom.trim());
            return fichierRepository.save(fichier);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkFileName(String fileName){
        if(fichierRepository.findByNomStockage(fileName.trim().toLowerCase()) != null){
            return true;
        };
        return false;
    }

    public Fichier findByNomStockage(String nomStockage){
        try{
            if(fichierRepository.findByNomStockage(nomStockage) != null){
                return fichierRepository.findByNomStockage(nomStockage.trim().toLowerCase());
            }
           return null;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public List<FichierDto> getFichiersByIdDossier(Long idDossier ) {

        List<FichierDto> fichierDtos = new ArrayList<>();

        try {
            List<Fichier> fichiers = fichierRepository.findFichiersByDossier_IdDossier(idDossier);

            for (Fichier fichier : fichiers) {
                FichierDto fichierDto = new FichierDto();
                fichierDto.setCheminLocal(fichier.getCheminLocal());
                fichierDto.setIdFichier(fichier.getIdFichier());
                fichierDto.setDateAjout(fichier.getDateAjout());
                fichierDto.setNomOriginal(fichier.getNomOriginal());
                fichierDto.setDateAjout(fichier.getDateAjout());
                fichierDto.setNomStockage(fichier.getNomStockage());
                fichierDtos.add(fichierDto);
            }

            return fichierDtos;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

}
