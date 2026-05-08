package fr.pdfmaker.backend.service.coffrefort;

import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurSecretDetailDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import fr.pdfmaker.backend.service.commun.FormatVerification;
import fr.pdfmaker.backend.utils.DtoUserConverter;
import jdk.jshell.execution.Util;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HexFormat;

import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;


@Service
@NoArgsConstructor
public class EncryptService {

    @Autowired
    private IUtilisateurRepository iUtilisateurRepository;

    FormatVerification verifyFormat = new FormatVerification();
    public byte [] masterKeyGenerator( ) {

        byte[] masterKeyBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(masterKeyBytes);
        return masterKeyBytes;
    }



    /**
     * Cette méthode permet de chiffrer la masterKey avec le mot de passe de l'utilisateur et le salt, en utilisant l'algorithme PBKDF2WithHmacSHA256 pour dériver une clé secrète à partir du mot de passe et du salt, puis en utilisant cette clé secrète pour chiffrer la masterKey avec l'algorithme AES/GCM/PKCS5Padding. La masterKey chiffrée est ensuite encodée en Base64 pour être stockée dans la base de données.
     * @param masterKey la clée masterKey est une clée de 256 bits (32 bytes) qui sera utilisée
     *
     * @return String de la clée masterKey qui sera stockée en BDD
     */
    public  String chiffrageMasterKey (byte [] masterKey, byte[] secretKey) {


        try {
            System.out.println("=== SECRET KEY CHIFFREMENT chiffrage master key  === " + Arrays.toString(secretKey));

            byte[] iv = generateIv();
            Cipher cipher = Cipher.getInstance("AES/GCM/noPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new GCMParameterSpec(128, iv));
            byte [] encryptedMasterKey =  cipher.doFinal(masterKey);
            byte[] ivPlusCipher = new byte[iv.length + encryptedMasterKey.length];
            System.arraycopy(iv, 0, ivPlusCipher, 0, iv.length);
            System.arraycopy(encryptedMasterKey, 0, ivPlusCipher, iv.length, encryptedMasterKey.length);

            return getEncoder().encodeToString(ivPlusCipher);
        } catch (NoSuchAlgorithmException  e) {
            throw new RuntimeException("Erreur lors du chiffrage de la master key : " + e.getMessage());
        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     *
     * @param password
     * @param salt
     * @return byte [] la clé secrète dérivée du mot de passe et du salt, qui sera utilisée pour chiffrer et déchiffrer la masterKey. Cette clé secrète est générée en utilisant l'algorithme PBKDF2WithHmacSHA256, qui est un algorithme de dérivation de clé sécurisé et largement utilisé. La méthode secretKeyGenerator() prend en entrée le mot de passe de l'utilisateur et le salt, et utilise ces deux éléments pour générer une clé secrète de 256 bits (32 bytes) qui sera utilisée pour chiffrer et déchiffrer la masterKey.
     */
    public byte [] secretKeyGenerator(String password, String salt) {

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);

        try {
            SecretKeyFactory secretKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            System.out.println("=== SECRET KEY CHIFFREMENT generation === " + Arrays.toString(secretKey.generateSecret(spec).getEncoded()));
            return secretKey.generateSecret(spec).getEncoded();

        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte [] generateIv(){
        byte[] iv = new byte[12];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }


    public byte[] dechiffrageMasterKey(String encryptedMasterKey, byte[] secretKey) {
        try {
            System.out.println("=== SECRET KEY CHIFFREMENT  dans dechiffrage === " + Arrays.toString(secretKey));
            byte[] ivPlusCipher = getDecoder().decode(encryptedMasterKey);

            byte[] iv = Arrays.copyOfRange(ivPlusCipher, 0, 12);

            byte[] encrypted = Arrays.copyOfRange(ivPlusCipher, 12, ivPlusCipher.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new GCMParameterSpec(128, iv));

            return cipher.doFinal(encrypted); // ← retournera bien 16 bytes !

        } catch (Exception e) {
            throw new RuntimeException("Erreur déchiffrage master key : " + e.getMessage());
        }
    }


    /**
     * Methode qui permet de générer un hash aléatoire pour le salt de l'utilisateur, pour renforcer la sécurité du mot de passe hashé dans la bdd.
     * @return un hash, pour le salt dans la table utilisateur
     */
    public String saltGenerator(){
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return HexFormat.of().formatHex(saltBytes);
    }


    public UtilisateurSecretDetailDto userSecretDetail (Long idUser) throws Exception{
        try{
            if (!iUtilisateurRepository.existsById(idUser)){
                throw new IllegalArgumentException("Cet utilisateur n'existe pas !  ");
            }
            UtilisateurSecretDetailDto utilisateurSecretDetailDto = new UtilisateurSecretDetailDto();
            Utilisateur user = iUtilisateurRepository.findByIdUser(idUser);
            utilisateurSecretDetailDto.setIdUser(user.getIdUser());
            utilisateurSecretDetailDto.setPasswordHash(user.getPasswordHash());
            utilisateurSecretDetailDto.setSalt(user.getSalt());
            utilisateurSecretDetailDto.setMasterKey(user.getMasterKey());

            return  utilisateurSecretDetailDto;
        }catch (Exception e){
            throw new Exception("Erreur lors de la vérification de l'existence de l'utilisateur : " + e.getMessage());
        }


    }


   public byte [] encryptPdf( Long idUser, byte [] fichierPdf , String motDePasse) throws Exception {

            UtilisateurSecretDetailDto userSecretDetailDto = userSecretDetail(idUser);
            try{
                if (!verifyFormat.verifyFormatPdf(fichierPdf)) {
                    //throw new EncryptionException("Format pdf invalide ");
                }
                    String masterKey= userSecretDetailDto.getMasterKey();
                    byte [] secretKey =  secretKeyGenerator (motDePasse, userSecretDetailDto.getSalt());

                    byte [] iv = generateIv();

                System.out.println("=== SECRET KEY CHIFFREMENT === " + Arrays.toString(secretKey));
                    byte [] masterKeyBrute = dechiffrageMasterKey(masterKey , secretKey);
                    System.out.println("=== MASTER KEY LENGTH === " + masterKeyBrute.length);

                    SecretKey secretKey_ = new SecretKeySpec(masterKeyBrute, "AES");
                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey_,new GCMParameterSpec(128, iv));

                    byte[] encryptedPdf = cipher.doFinal(fichierPdf);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    outputStream.write(iv);           // 12 bytes
                    outputStream.write(encryptedPdf);
                    return outputStream.toByteArray();

            }catch(Exception e){

                throw new Exception ("PDF encryption failed" + e.getMessage());

            }
    }


    public byte[] decryptPdf(Long idUser, byte [] fichierCrypte) throws Exception {


        try{
            UtilisateurSecretDetailDto userSecretDetailDto = userSecretDetail(idUser);

            byte [] iv = generateIv();

            byte [] secretKey = secretKeyGenerator(userSecretDetailDto.getPasswordHash() , userSecretDetailDto.getSalt());
            byte [] masterKeyBrute = dechiffrageMasterKey(userSecretDetailDto.getMasterKey() , secretKey);
            SecretKey secretKey_ = new SecretKeySpec(masterKeyBrute, "AES");

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey_,new GCMParameterSpec(128, iv));

            byte[] decryptedPdf = cipher.doFinal(fichierCrypte);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(iv);           // 12 bytes
            outputStream.write(decryptedPdf);
            return outputStream.toByteArray();

        }catch (Exception e){

            throw new Exception ("Déchiffrage pdf annulé, une erreur est survenue  : " + e.getMessage());

        }


    }
}
