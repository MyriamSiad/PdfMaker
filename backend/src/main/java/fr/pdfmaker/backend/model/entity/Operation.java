package fr.pdfmaker.backend.model.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private UUID idOperation;

    @Column(name = "fichier_source", length = 61)
    private String fichierSource;

    @Column(name = "fichier_resultat", length = 50)
    private String fichierResultat;

    @Column(name = "date_heure_operation", nullable = false)
    private LocalDateTime dateHeureOperation;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "id_type_operation", nullable = false)
    private TypeOperation typeOperation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OperationFichier> operationFichiers = new HashSet<>();
}

