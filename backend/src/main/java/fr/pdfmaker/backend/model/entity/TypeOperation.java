package fr.pdfmaker.backend.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        @Column(name = "id_type_operation")
        private Long idTypeOperation;

        @Column (name = "libelle_operation" , nullable = false , length = 50)
        private String libelleOperation;

        @Column (nullable = false , length = 120)
        private String description;

        @OneToMany(mappedBy = "typeOperation", cascade = CascadeType.ALL)
        private Set<Operation> operations = new HashSet<>();

    }

