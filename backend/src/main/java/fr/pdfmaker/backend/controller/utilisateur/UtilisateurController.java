package fr.pdfmaker.backend.controller.utilisateur;

import fr.pdfmaker.backend.model.dto.utilisateur.AuthResponseDto;
import fr.pdfmaker.backend.model.dto.utilisateur.InscriptionRequestDto;
import fr.pdfmaker.backend.model.dto.utilisateur.LoginDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
import fr.pdfmaker.backend.service.jwt.JwtService;
import fr.pdfmaker.backend.service.utilisateur.IUtilisateurService;
import fr.pdfmaker.backend.utils.DtoUserConverter;
import jakarta.validation.Valid;
import jakarta.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Consumes;

import static fr.pdfmaker.backend.utils.DtoUserConverter.convertUserDtoToUser;
import static fr.pdfmaker.backend.utils.DtoUserConverter.convertUserToUserDto;


@RestController
//@CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/rest/user")
public class UtilisateurController implements IUtilisateurController {

    @Autowired
    private IUtilisateurService userService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final UserDetailsService userDetailsService;


    public UtilisateurController(JwtService jwtService, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public String getInfos() {
        return "";
    }

    @Override
    @PostMapping("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<AuthResponseDto> createUtilisateur( @Valid @RequestBody InscriptionRequestDto user) {

        try{
           Long id =  userService.createUser(user);
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());


            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            AuthResponseDto authResponseDto = new AuthResponseDto();
            authResponseDto.setAccessToken(accessToken);
            authResponseDto.setRefreshToken(refreshToken);
            //authResponseDto.setUtilisateur(userService.getUtilsateur(id));
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDto);

        }catch (Exception e ){
            e.printStackTrace();
            assert HttpStatus.resolve(409) != null;
            return new ResponseEntity<>(HttpStatus.resolve(409));
        }


    }

    @Override
    @PutMapping("/update/{idUser}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<Long> updateUtilisateur(@PathVariable Long idUser,  @RequestBody UtilisateurDto user) {
        try{
          userService.updateUser(user);
          return new ResponseEntity<>(idUser, HttpStatus.ACCEPTED);
        }catch (Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @PostMapping("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<AuthResponseDto> connexionUtilisateur(@RequestBody LoginDto user) {

        try{

            UtilisateurDto userDto =  userService.loginUser(user);
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String accessToken = jwtService.generateToken(userDetails, userDto);
            //String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            AuthResponseDto authResponseDto = new AuthResponseDto();
            authResponseDto.setAccessToken(accessToken);
            authResponseDto.setRefreshToken(refreshToken);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getMotsDePasse()
                    )
            );

            //authResponseDto.setUtilisateur(userDto);
            return ResponseEntity.ok(authResponseDto);

        } catch (BadCredentialsException e) {
            // Email ou mot de passe incorrect
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
