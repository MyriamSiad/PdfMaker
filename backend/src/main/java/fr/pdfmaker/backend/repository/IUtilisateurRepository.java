package fr.pdfmaker.backend.repository;

import fr.pdfmaker.backend.model.dto.UtilisateurCreationDto;
import fr.pdfmaker.backend.model.dto.UtilisateurDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IUtilisateurRepository extends JpaRepository<Utilisateur , Long> {

    Utilisateur findByIdUser (Long id) throws Exception ;
    Utilisateur getUtilisateurByEmail(String email);




}
