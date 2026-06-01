package fr.pdfmaker.backend.model.dto.dossier;

import fr.pdfmaker.backend.model.dto.fichier.FichierDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class DossierResponseDto {

    Long idDossier;
    String nomDuDossier;
  //  Set<FichierDto> fichiers;
}
