package fr.pdfmaker.backend.mongo.repository;

import fr.pdfmaker.backend.mongo.document.AppError;

import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppErrorRepository extends MongoRepository <AppError, String> {


}
