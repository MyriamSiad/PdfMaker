package fr.pdfmaker.backend.repository;

import fr.pdfmaker.backend.model.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IUtilisateurRepository extends JpaRepository<Utilisateur , Long> {

    /**
     * C'est une methode qui permet de trouver un utilisateur dans la BDD à partir de son id.
     * @param id c'est l'id de l'utilisateur qu'on veut trouver.
     * @return  un objet utilisateur.
     * @throws Exception si il n'existe pas d'utilisateur avec cet id dans la BDD.
     */
    Utilisateur findByIdUser (Long id) throws Exception ;

    /**
     * C'est une methode qui permet de trouver un utilisateur dans la BDD à partir de son email.
     * @param email c'est l'email de l'utilisateur qu'on veut trouver.
     * @return  un objet utilisateur.
     *
     */
    Utilisateur getUtilisateurByEmail(String email);




}
