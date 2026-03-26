package fr.pdfmaker.backend.exception;

public class LoginIncorrectException  extends RuntimeException {

    public LoginIncorrectException(String message) {
        super(message);
    }

    public LoginIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "Login ou mot de passe incorrect. Veuillez réessayer.";
    }
}
