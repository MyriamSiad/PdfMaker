package fr.pdfmaker.backend.service.coffrefort;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HexFormat;

import static java.util.Base64.getEncoder;


@Service
@NoArgsConstructor
public class EncryptService {


    public byte [] masterKeyGenerator( ) {

        byte[] masterKeyBytes = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(masterKeyBytes);
        return masterKeyBytes;
    }

    /**
     * Cette méthode permet de chiffrer la masterKey avec le mot de passe de l'utilisateur et le salt, en utilisant l'algorithme PBKDF2WithHmacSHA256 pour dériver une clé secrète à partir du mot de passe et du salt, puis en utilisant cette clé secrète pour chiffrer la masterKey avec l'algorithme AES/GCM/PKCS5Padding. La masterKey chiffrée est ensuite encodée en Base64 pour être stockée dans la base de données.
     * @param masterKey la clée masterKey est une clée de 256 bits (32 bytes) qui sera utilisée pour chiffrer et déchiffrer les fichiers PDF. Elle doit être générée de manière sécurisée et aléatoire, et doit être protégée contre tout accès non autorisé. La méthode masterKeyGenerator() génère une masterKey aléatoire de 256 bits en utilisant la classe SecureRandom de Java.
     * @param password le password de l'utilisateur
     * @param salt le salt est une valeur aléatoire qui est utilisée pour renforcer la sécurité du mot de passe hashé dans la base de données. Il est généralement généré de manière aléatoire pour chaque utilisateur et stocké dans la base de données avec le mot de passe hashé. La méthode saltGenerator() génère un salt aléatoire en utilisant la classe SecureRandom de Java, et le convertit en une chaîne hexadécimale pour le stockage dans la base de données.
     * @return String de la clée masterKey qui sera stockée en BDD
     */
    public  String chiffrageMasterKey (byte [] masterKey, byte[] secretKey) {

        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/noPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte [] encryptedMasterKey =  cipher.doFinal(masterKey);
            return getEncoder().encodeToString(encryptedMasterKey);

        } catch (NoSuchAlgorithmException  e) {
            throw new RuntimeException("Erreur lors du chiffrage de la master key : " + e.getMessage());
        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |InvalidKeyException e) {
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
        Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm algorithmSha256 = Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256;
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);

        try {
            byte[] secretKey = SecretKeyFactory.getInstance(algorithmSha256.toString()).generateSecret(spec).getEncoded();
            return secretKey;
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte [] dechiffrageMasterKey(String encryptedMasterKey, byte [] secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/noPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte [] decryptedMasterKey =  cipher.doFinal(getEncoder().encodeToString(encryptedMasterKey.getBytes()).getBytes());
            return decryptedMasterKey;

        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |InvalidKeyException  | NoSuchAlgorithmException  e) {
            throw new RuntimeException(e);
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





   /* public byte [] encryptPdf(byte [] fichierPdf, String masterKey ) {
        if(verifyFormatPdf(fichierPdf)){
            try{



            }catch(Exception e){

            }
            //TODO : implémenter la logique d'encryption du PDF
        }


        return null;

    }*/

    public byte[] decryptPdf(byte [] fichierPdf) {
        //TODO : Implémenter la logique de décryption du PDF
        return null;
    }
}
