package fr.pdfmaker.backend.model.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name ="operation")
public class Operation {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    @Column(name = "id_operation")
    private String idOperation;

    @Column(name = "fichier_source", length = 61)
    private String fichierSource;

    @Column(name = "fichier_resultat", length = 50)
    private String fichierResultat;

    @Column(name = "date_heure_operation")
    private LocalDateTime dateHeureOperation;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "id_type_operation", nullable = false)
    private TypeOperation typeOperation;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Utilisateur utilisateur;
}

