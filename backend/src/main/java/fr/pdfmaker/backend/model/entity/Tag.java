package fr.pdfmaker.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "tag")
public class Tag {

    @Id
    @Column(name = "id_tag")
    private Integer id;

    @Column(name = "libelle", length = 50)
    private String libelle;

    @Column(name = "couleur", length = 50)
    private String couleur;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utilisateur utilisateur;

    @ManyToMany(mappedBy = "tags")
    private Set<FichierPdf> fichiers = new HashSet<>();
}
