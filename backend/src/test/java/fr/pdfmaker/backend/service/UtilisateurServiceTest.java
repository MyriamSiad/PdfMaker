package fr.pdfmaker.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.pdfmaker.backend.exception.LoginIncorrectException;
import fr.pdfmaker.backend.model.dto.utilisateur.LoginDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import fr.pdfmaker.backend.service.utilisateur.UtilisateurService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UtilisateurServiceTest {

    @Mock
    private IUtilisateurRepository utilisateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UtilisateurService utilisateurService; // La classe à tester

    private LoginDto loginDto;
    private Utilisateur utilisateurBdd;

    @BeforeEach
    void setUp() {
        loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setMotsDePasse("Password123");

        utilisateurBdd = new Utilisateur();
        utilisateurBdd.setIdUser(1L);
        utilisateurBdd.setEmail("test@example.com");
        utilisateurBdd.setPasswordHash("hashed_password_in_db");
    }

    @Test
    void loginUser_Succes() throws Exception {
        // Arrange
        when(utilisateurRepository.getUtilisateurByEmail("test@example.com")).thenReturn(utilisateurBdd);
        when(passwordEncoder.matches("Password123", "hashed_password_in_db")).thenReturn(true);

        // Act
        UtilisateurDto result = utilisateurService.loginUser(loginDto);
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(utilisateurRepository, times(1)).getUtilisateurByEmail(anyString());
    }

    @Test
    void loginUser_Echec_MauvaisMotDePasse() {
        // Arrange
        when(utilisateurRepository.getUtilisateurByEmail("test@example.com")).thenReturn(utilisateurBdd);
        when(passwordEncoder.matches("Password123", "hashed_password_in_db")).thenReturn(false);

        // Act & Assert
        assertThrows(LoginIncorrectException.class, () -> {
            utilisateurService.loginUser(loginDto);
        });
    }
    
}
