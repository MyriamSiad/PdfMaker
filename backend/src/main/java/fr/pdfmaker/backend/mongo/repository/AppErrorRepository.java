package fr.pdfmaker.backend.mongo.repository;

import fr.pdfmaker.backend.mongo.document.LoginHistorique;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppErrorRepository extends MongoRepository <LoginHistorique, String> {


}
