package fr.pdfmaker.backend.service.coffrefort;


public class CoffreFortSession {

    private String password;  // en clair, uniquement en RAM
    private long createdAt;

    public CoffreFortSession(String password, long createdAt) {
        this.password = password;
        this.createdAt = createdAt;
    }

    // getters / setters
    public String getPassword() { return password; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
