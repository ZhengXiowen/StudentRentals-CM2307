package studentrentals.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

import studentrentals.model.Homeowner;
import studentrentals.model.Student;
import studentrentals.model.User;
import studentrentals.repository.UserRepository;

public class AuthService {
    private final UserRepository users;
    private User currentUser;

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public User getCurrentUser() { return currentUser; }

    public void logout() { currentUser = null; }

    public boolean registerStudent(String name, String email, String password, String university, String studentId) {
        if (users.emailExists(email)) return false;
        users.save(new Student(name, email, sha256(password), university, studentId));
        return true;
    }

    public boolean registerHomeowner(String name, String email, String password) {
        if (users.emailExists(email)) return false;
        users.save(new Homeowner(name, email, sha256(password)));
        return true;
    }

    public boolean login(String email, String password) {
        Optional<User> userOpt = users.findByEmail(email);
        if (userOpt.isEmpty()) return false;
        User user = userOpt.get();
        if (!user.getPasswordHash().equals(sha256(password))) return false;
        currentUser = user;
        return true;
    }

    private String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}
