package fr.pdfmaker.backend.service.coffrefort;

import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import fr.pdfmaker.backend.security.BruteForceProtectionService;
import fr.pdfmaker.backend.service.utilisateur.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class CoffreFortSessionService {


    @Autowired
    private IUtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BruteForceProtectionService bruteForce;

    private final ConcurrentHashMap<Long, CoffreFortSession> sessions = new ConcurrentHashMap<>();

    private static final long SESSION_TTL_MS = 15 * 60 * 1000L; // 15 min
    @Autowired
    private UtilisateurService utilisateurService;


    public void openVault(Long idUser, String password) throws Exception {

        try {
            if (utilisateurRepository.findByIdUser(idUser) == null) {
                throw new RuntimeException("Utilisateur non trouvé !!");
            }
            Utilisateur user = utilisateurRepository.findByIdUser(idUser);
            if(user.getLockedUntil() == null) {


                if (!(passwordEncoder.matches(password, user.getPasswordHash()))) {
                    user.setFailedAttempts(user.getFailedAttempts() + 1);
                    if (bruteForce.locking(user.getFailedAttempts())) {
                        user.setLockedUntil(bruteForce.lockUntil(user.getFailedAttempts()));
                    }
                    throw new RuntimeException("Mots de passe  incorrecte !!");
                }
            }
            if (bruteForce.isLocked(user.getLockedUntil())) {
                throw new AccountLockedException("Coffre fort verrouillé temporairement");
            }
            user.setFailedAttempts(0);
            user.setLockedUntil(null);
            utilisateurRepository.save(user);
            sessions.put(idUser, new CoffreFortSession(password, System.currentTimeMillis()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public String getPassword(Long idUser) {
        CoffreFortSession session = sessions.get(idUser);
        if (session == null) {
            throw new RuntimeException("Coffre verrouillé, veuillez saisir votre mot de passe.");

        }
        if (System.currentTimeMillis() - session.getCreatedAt() > SESSION_TTL_MS) {
            sessions.remove(idUser);
            throw new RuntimeException("Session Expiré  !! ");

        }

        session.setCreatedAt(System.currentTimeMillis());
        return session.getPassword();
    }

    public void closeVault(Long idUser) {
        sessions.remove(idUser);
    }

    public boolean isVaultOpen(Long idUser) {
        try {
            getPassword(idUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Scheduled(fixedDelay = 5 * 60 * 1000L)
    public void cleanExpiredSessions() {
        long now = System.currentTimeMillis();
        sessions.entrySet().removeIf(e ->
                now - e.getValue().getCreatedAt() > SESSION_TTL_MS
        );
    }
}
