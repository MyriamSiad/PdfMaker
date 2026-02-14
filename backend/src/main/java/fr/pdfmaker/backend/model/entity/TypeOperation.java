package fr.pdfmaker.backend.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
/*id_type_operation
libelle_operation
couleur
icone
description
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table (name = "type_operation")
public class TypeOperation {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idTypeOperation;

    @Column (nullable = false , length = 50)
    private String libelleOperation;

    @Column (nullable = false , length = 50)
    private String couleur;

    @Column (nullable = false , length = 50)
    private String icone;

    @Column (nullable = false , length = 120)
    private String description;



}
