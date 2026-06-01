package fr.pdfmaker.backend.utils;

import fr.pdfmaker.backend.model.dto.dossier.DossierResponseDto;
import fr.pdfmaker.backend.model.dto.fichier.FichierDto;
import fr.pdfmaker.backend.model.entity.DossierVirtuel;
import fr.pdfmaker.backend.model.entity.Fichier;

import java.util.HashSet;
import java.util.Set;

public class DossierVirtuelConverter {

    public  static Set<DossierResponseDto> DossierVirtuelToDto (Set<DossierVirtuel> dossiersVirtuel){
      DossierResponseDto dossierResponseDto  = new DossierResponseDto();
      Set<DossierResponseDto> dossierResponseDtos = new HashSet<>();
        Set<FichierDto> setFichierDto = null;
        FichierDto fichierDto = new FichierDto();

        for (DossierVirtuel dossierVirtuel : dossiersVirtuel) {

            dossierResponseDto.setIdDossier(dossierVirtuel.getIdDossier());
            dossierResponseDto.setNomDuDossier(dossierVirtuel.getNomDuDossier());
            for (Fichier fichier : dossierVirtuel.getFichiers()) {
                fichierDto.setIdFichier(fichier.getIdFichier());
                fichierDto.setNomOriginal(fichier.getNomOriginal());
                fichierDto.setDateAjout(fichier.getDateAjout());
                fichierDto.setNomStockage(fichier.getNomStockage());
                fichierDto.setCheminLocal(fichier.getCheminLocal());
                setFichierDto.add(fichierDto);
            }
            dossierResponseDtos.add(dossierResponseDto);
        }
    ;
        return dossierResponseDtos;


    }
}
