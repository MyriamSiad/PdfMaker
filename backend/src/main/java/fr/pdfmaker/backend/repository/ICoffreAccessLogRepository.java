package fr.pdfmaker.backend.repository;

import fr.pdfmaker.backend.model.entity.CoffreAccessLog;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Repository
public interface ICoffreAccessLogRepository extends JpaRepository<CoffreAccessLog, Long> {

    CoffreAccessLog findByIdLog(Long id);

    CoffreAccessLog findByAction (String action);

    CoffreAccessLog findByDateHeure(LocalDateTime dateHeure);

    CoffreAccessLog getCoffreAccessLogByUtilisateur (Utilisateur utilisateur);
}
