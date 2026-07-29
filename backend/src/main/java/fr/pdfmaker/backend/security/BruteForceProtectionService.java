package fr.pdfmaker.backend.security;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class BruteForceProtectionService {

    private static final int MAX_TENTAVIES = 3;

    public boolean isLocked(LocalDateTime lockedUntil) {
        boolean locked = false;
        if(lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now())){
            locked = true;
        }
        return locked;
    }

    public boolean locking (int tentatives) {
        boolean locked = false;
        if(tentatives >= MAX_TENTAVIES){
            locked = true;
        }
        return locked;
    }

    public LocalDateTime lockUntil (int tentatives) {

        return LocalDateTime.now().plusSeconds (getLockedSeconds(tentatives));
    }

    private long getLockedSeconds(int tentatives) {
        if (tentatives < 5) return 30;
        if (tentatives < 8) return 120;
        if (tentatives< 12) return 900;
        return 3600;
    }
}
