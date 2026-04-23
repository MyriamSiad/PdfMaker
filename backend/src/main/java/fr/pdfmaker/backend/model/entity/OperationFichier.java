package fr.pdfmaker.backend.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "operation_fichier")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OperationFichier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation", nullable = false)
    private Operation operation;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fichier", nullable = false)
    private Fichier fichier;

    @Column(name = "role", length = 50)
    private String role;
}
