package fr.pdfmaker.backend.service.jwt;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpiration;

    // ─────────────────────────────────────────────
    //  Extraction des claims
    // ─────────────────────────────────────────────

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    public String generateToken(UserDetails details, UtilisateurDto utilisateur) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("prenom", utilisateur.getPrenom());
        extraClaims.put("nom", utilisateur.getNom());
        extraClaims.put("userId", utilisateur.getIdUser());
        return buildToken(extraClaims, details, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        // ✅ Jwts.builder() — API 0.12.x, signWith(SecretKey) sans préciser l'algo (auto-détecté)
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // ─────────────────────────────────────────────
    //  Validation
    // ─────────────────────────────────────────────

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ─────────────────────────────────────────────
    //  Clé de signature
    // ─────────────────────────────────────────────

    private SecretKey getSigningKey() {
        // ✅ Keys.hmacShaKeyFor() — méthode moderne, remplace les anciennes approches manuelles
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}