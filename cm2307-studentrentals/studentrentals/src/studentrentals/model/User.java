package studentrentals.model;

public abstract class User {
    private final String name;
    private final String email;
    private final String passwordHash;

    protected User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
}
