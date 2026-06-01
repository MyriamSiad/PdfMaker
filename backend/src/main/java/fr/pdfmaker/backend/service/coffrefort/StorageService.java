package fr.pdfmaker.backend.service.coffrefort;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {
    @Value("${coffre.download.folder-name}")
    private String customFolderName;

    private Path rootDownloadPath;

    @PostConstruct
    public void initSecureFolder() {
        try {
            // 1. Récupère dynamiquement le dossier "Home" de l'utilisateur de l'OS (ex: C:\Users\Nom)
            String userHome = System.getProperty("user.home");

            // 2. On cible le sous-dossier "Documents" et on y ajoute votre arborescence personnalisée
            this.rootDownloadPath = Paths.get(userHome, "Documents", customFolderName);
            File folder = this.rootDownloadPath.toFile();

            // 3. Si le dossier n'existe pas, on le crée (ainsi que les dossiers parents si nécessaires)
            if (!folder.exists()) {
                boolean created = folder.mkdirs();
                if (created) {
                    // SÉCURITÉ OS : On restreint les droits d'accès
                    // Seul l'utilisateur propriétaire de la session de l'OS peut lire/écrire dedans
                    folder.setReadable(true, true);  // (readable=true, ownerOnly=true)
                    folder.setWritable(true, true);  // (writable=true, ownerOnly=true)
                    folder.setExecutable(true, true); // Requis pour pouvoir ouvrir/parcourir un dossier sous Linux/Mac

                    System.out.println("[COFFRE-FORT] Dossier sécurisé initialisé à : " + folder.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Impossible d'initialiser le dossier de téléchargement sécurisé", e);
        }
    }

    /**
     * Permet à votre service de déchiffrement de récupérer le chemin exact pour y enregistrer le fichier
     */
    public Path getTargetFilePath(String fileName) {
        return this.rootDownloadPath.resolve(fileName);
    }

}
