package fr.pdfmaker.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table (name = "utilisateur")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode


public class Utilisateur implements UserDetails  {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_user")
    private Long idUser;

    @Column (nullable = false , length = 48)
    private String nom;

    @Column (nullable = false , length = 48)
    private String prenom;

    @Column (nullable = false , length = 48)
    private String email;

    @Column (nullable = false , length = 250)
    private String passwordHash;

    @Column (nullable = false , length = 250)
    private String masterKey;

    @Column (nullable = true , length = 64)
    @CreationTimestamp
    private Instant dateCreationCompte;

    @Column(name = "salt", length = 255, nullable = false)
    private String salt;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Operation> operations = new HashSet<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private Set<CoffreAccessLog> accessLogs = new HashSet<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DossierVirtuel> dossiers = new HashSet<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Fichier> fichiers = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }
}

