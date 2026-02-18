package fr.pdfmaker.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "utilisateur")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode


public class Utilisateur {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idUser;
//idUser, nom, prenom, email, passwordHash,dateCreationCompte
    @Column (nullable = false , length = 48)
    private String nom;

    @Column (nullable = false , length = 48)
    private String prenom;

    @Column (nullable = false , length = 48)
    private String email;

    @Column (nullable = false , length = 250)
    private String passwordHash;

    @Column (nullable = true , length = 64)
    private LocalDate dateCreationCompte;



    @OneToMany(mappedBy = "utilisateur")
    private List<FichierPdf> listFichiersPdf = new ArrayList<>();


    @OneToMany(mappedBy = "utilisateur")
    private List<Operation> operations = new ArrayList<>();


    @OneToMany(mappedBy = "utilisateur")
    private Set<Tag> tags = new HashSet<>();


}
