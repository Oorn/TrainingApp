package org.example.repository.impl;

import org.example.domain_entities.Trainee;
import org.example.domain_entities.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Override
    public Optional<User> get(String username) {
        return Optional.empty(); //todo
    }

    @Override
    public List<User> getAllByPrefix(String usernamePrefix) {
        return new ArrayList<>(); //todo
    }

    @Override
    public Trainee save(User entity) {
        return null; //todo
    }
}
