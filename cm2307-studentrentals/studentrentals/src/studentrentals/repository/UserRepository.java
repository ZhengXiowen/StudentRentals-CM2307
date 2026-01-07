package studentrentals.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import studentrentals.model.User;

public class UserRepository {
    private final Map<String, User> byEmail = new HashMap<>();

    public boolean emailExists(String email) {
        return byEmail.containsKey(email.toLowerCase());
    }

    public void save(User user) {
        byEmail.put(user.getEmail().toLowerCase(), user);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(byEmail.get(email.toLowerCase()));
    }
}
