package org.example.repository;

import org.example.domain_entities.User;

import java.util.List;
import java.util.Optional;

//todo unsure if needed, architecture gets ugly without it
public interface UserRepository {
    Optional<User> get(String username);

    List<User> getAllByPrefix(String usernamePrefix);
    User save(User entity);

    void delete(String username);
}
