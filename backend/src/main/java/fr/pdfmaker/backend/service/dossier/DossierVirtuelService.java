package fr.pdfmaker.backend.service.dossier;

import fr.pdfmaker.backend.model.dto.dossier.DossierResponseDto;
import fr.pdfmaker.backend.model.entity.DossierVirtuel;
import fr.pdfmaker.backend.repository.IDossierVirtuelRepository;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static fr.pdfmaker.backend.utils.DossierVirtuelConverter.DossierVirtuelToDto;


@Getter
@Setter
@Service
public class DossierVirtuelService {

    @Autowired
    IDossierVirtuelRepository dossierVirtuelRepository;


    @Autowired
    IUtilisateurRepository utilisateurRepository;

    @Transactional(readOnly = true)
    public Set<DossierResponseDto> getAllDossier(Long idUser){
        Set<DossierResponseDto> dossierResponseDtos = new HashSet<>();

        try{
            if (idUser == null) {
                return null;
            }
            Set<DossierVirtuel> dossiersEntites = dossierVirtuelRepository.findByUtilisateurIdUser(idUser);
            for(DossierVirtuel dossier : dossiersEntites){
                DossierResponseDto dossierResponseDto = new DossierResponseDto();
                dossierResponseDto.setIdDossier(dossier.getIdDossier());
                dossierResponseDto.setNomDuDossier(dossier.getNomDuDossier());
                dossierResponseDtos.add(dossierResponseDto);
            }




            return dossierResponseDtos;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public DossierResponseDto creerDossier(String nomDuDossier, Long idUtilisateur){
        DossierVirtuel dossierVirtuel = new DossierVirtuel();
        DossierResponseDto dossierResponseDto = new DossierResponseDto();
        try{
            dossierVirtuel.setNomDuDossier(nomDuDossier);
            dossierVirtuel.setUtilisateur(utilisateurRepository.findById(idUtilisateur).orElseThrow());
            dossierVirtuelRepository.save(dossierVirtuel);
            dossierResponseDto.setNomDuDossier(dossierVirtuel.getNomDuDossier());
            dossierResponseDto.setIdDossier(dossierVirtuel.getIdDossier());
            return  dossierResponseDto;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DossierResponseDto modifierNomDossier(String nomDuDossier,  Long idDossier){
        if(nomDuDossier == null || nomDuDossier.isEmpty()){
            throw new IllegalArgumentException();
        }

        try{
            DossierVirtuel dossierVirtuel = dossierVirtuel = dossierVirtuelRepository.findByidDossier(idDossier);
            dossierVirtuel.setNomDuDossier(nomDuDossier);
            dossierVirtuelRepository.save(dossierVirtuel);
            DossierResponseDto dossierResponseDto = new DossierResponseDto();
            dossierResponseDto.setIdDossier(dossierVirtuel.getIdDossier());
            dossierResponseDto.setNomDuDossier(nomDuDossier);
            return dossierResponseDto;
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    public void supprimerDossier (Long idDossier){
        try{

            dossierVirtuelRepository.deleteById(idDossier);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
