package fr.pdfmaker.backend.exception;


/**
 * Exception personnalisée pour indiquer que l'email fourni lors de l'inscription est déjà utilisé par un autre utilisateur.
 * Cette exception peut être levée lors de la création d'un nouvel utilisateur si l'email existe déjà dans la base de données.
 *
 */
public class EmailDejaUtiliserException extends RuntimeException {

    public EmailDejaUtiliserException(String message) {
        super(message);
    }

    public EmailDejaUtiliserException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "L'email fourni est déjà utilisé par un autre utilisateur. Veuillez choisir un email différent.";
    }
}
