package org.example.repository;

import org.example.domain_entities.Trainee;
import org.example.domain_entities.User;

import java.util.List;
import java.util.Optional;

//todo unsure if needed, architecture gets ugly without it
public interface UserRepository {
    Optional<User> get(String username);

    List<User> getAllByPrefix(String usernamePrefix);
    Trainee save(User entity);
}
