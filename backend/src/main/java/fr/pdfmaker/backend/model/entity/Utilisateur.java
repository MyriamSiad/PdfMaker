package fr.pdfmaker.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column (nullable = false , length = 64)
    private String passwordHash;

    @Column (nullable = true , length = 64)
    private LocalDate dateCreationCompte;

    //@OneToMany( fetch = FetchType.LAZY)



}
