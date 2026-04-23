package fr.pdfmaker.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "dossier_virtuel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DossierVirtuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dossier")
    private Long idDossier;

    @Column(name = "id_dossier_parent")
    private Long idDossierParent;

    @Column(name = "nom_du_dossier", length = 255, nullable = false)
    private String nomDuDossier;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private Utilisateur utilisateur;


    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FichiersDossiers> fichiersDossiers = new HashSet<>();


    @OneToMany
    @JoinColumn(name = "id_dossier_parent")
    private Set<DossierVirtuel> sousDossiers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dossier_parent", insertable = false, updatable = false)
    private DossierVirtuel dossierParent;
}