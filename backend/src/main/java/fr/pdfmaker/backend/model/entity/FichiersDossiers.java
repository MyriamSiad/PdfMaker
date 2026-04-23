package fr.pdfmaker.backend.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fichiers_dossiers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichiersDossiers {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "fichiers_dossier_id", nullable = false)
    private Long fichiersDossierId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dossier", nullable = false)
    private DossierVirtuel dossier;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fichier", nullable = false)
    private Fichier fichier;

    @Column(name = "date_ajout", nullable = false)
    private LocalDateTime dateAjout;
}
