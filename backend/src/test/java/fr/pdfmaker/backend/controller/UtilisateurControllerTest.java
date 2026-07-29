package fr.pdfmaker.backend.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pdfmaker.backend.controller.utilisateur.UtilisateurController;
import fr.pdfmaker.backend.model.dto.utilisateur.LoginDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
import fr.pdfmaker.backend.security.SecurityConfig;
import fr.pdfmaker.backend.service.jwt.JwtService;
import fr.pdfmaker.backend.service.utilisateur.IUtilisateurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import fr.pdfmaker.backend.mongo.service.AppErrorService;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
@WebMvcTest(UtilisateurController.class)

public class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Pour convertir les objets en JSON

    @MockitoBean
    private IUtilisateurService userService;

    @MockitoBean
    private AppErrorService appErrorService;
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetailsService userDetailsService;
    @Test
    @WithMockUser
    void connexionUtilisateur_Succes() throws Exception {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("user@example.com");
        loginDto.setMotsDePasse("password");

        UtilisateurDto utilisateurDto = new UtilisateurDto();
        UserDetails mockUserDetails = mock(UserDetails.class);

        when(userService.loginUser(any(LoginDto.class))).thenReturn(new UtilisateurDto());
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));
        // Act & Assert
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token-mock"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-mock"));
    }

    @Test
    void connexionUtilisateur_IdentifiantsInvalides() throws Exception {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("wrong@example.com");
        loginDto.setMotsDePasse("wrongpass");

        // On simule le fait que l'authenticationManager lève une erreur
        when(userService.loginUser(any(LoginDto.class))).thenReturn(new UtilisateurDto());
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized()); // Doit retourner 401
    }
}