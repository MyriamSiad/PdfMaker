package fr.pdfmaker.backend.model.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.Name;

import java.time.LocalDate;
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
public class FichierPdf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idFichier;

    @Column(nullable = false, length = 50)
    private String chemin;

    @Column(nullable = false)
    private Long tailleFichier;

    @Column(nullable = false)
    private Long nombrePage;

    @Column(nullable = true)
    private LocalDate dateAjout;

    @Column(nullable = false)
    private Boolean favoris;


    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Utilisateur utilisateur;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "fichier_pdf_tag", joinColumns = @JoinColumn(name = "id_fichier"),
            inverseJoinColumns = @JoinColumn(name = "id_tag"))
    private Set<Tag> tags = new HashSet<>();


}
