package fr.pdfmaker.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "dossier_virtuel")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DossierVirtuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dossier")
    private Long idDossier;


    @Column(name = "nom_du_dossier", length = 255, nullable = false)
    private String nomDuDossier;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    @JsonIgnore
    private Utilisateur utilisateur;

    @Column(name = "is_system")
    private Boolean isSystem;

    @JsonIgnore
    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Fichier> fichiers = new HashSet<>();


}