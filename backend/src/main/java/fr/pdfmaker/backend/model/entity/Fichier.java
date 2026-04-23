package fr.pdfmaker.backend.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
public class Fichier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFichier;

    @Column(name = "nom_original", length = 255, nullable = false)
    private String nomOriginal;

    @Column(name = "date_ajout", nullable = false)
    private LocalDateTime dateAjout;

    @Column(name = "nom_stockage", length = 255, nullable = false, unique = true)
    private String nomStockage;

    @Column(name = "algorithme_chiffrement", length = 50, nullable = false)
    private String algorithmeChiffrement;


    @Column(name = "hash_fichier", length = 64, nullable = false)
    private String hashFichier;


    @Column(name = "iv_chiffrement", length = 500, nullable = false)
    private String ivChiffrement;

    @Column(name = "chemin_local", length = 255)
    private String cheminLocal;


    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Utilisateur utilisateur;


    @OneToMany(mappedBy = "fichier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OperationFichier> operationFichiers = new HashSet<>();


    @OneToMany(mappedBy = "fichier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FichiersDossiers> fichiersDossiers = new HashSet<>();

}

