package fr.pdfmaker.backend.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
/*
id_fichier
 chemin
 taille_fichier
 nombre_page
 date_ajout
 favoris
 id_user
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table (name = "fichier_pdf")
public class FichierPdf {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idFichier;

    @Column (nullable = false , length = 50)
    private String chemin;

    @Column (nullable = false )
    private Long tailleFichier;

    @Column (nullable = false)
    private Long nombrePage;

    @Column(nullable = true)
    private LocalDate dateAjout;

    @Column (nullable = false)
    private Boolean favoris;







}
