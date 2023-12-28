package org.example.repository.impl;

import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.domain_entities.User;
import org.example.exceptions.IllegalStateException;
import org.example.repository.UserRepository;
import org.example.service.IdentityProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final TreeMap<String, User> userMap = new TreeMap<>();

    @Setter(onMethod_={@Autowired})
    private IdentityProviderService idProvider;

    @Override
    public Optional<User> get(String username) {

        User oldUser = userMap.get(username);
        if (oldUser == null)
            return Optional.empty();
        return Optional.of(oldUser);

    }

    @Override
    public List<User> getAllByPrefix(String usernamePrefix) {
        return userMap.subMap(usernamePrefix, usernamePrefix + Character.MAX_VALUE).values().stream()
                .filter(u-> !u.isRemoved())
                .collect(Collectors.toList());

        /*return userMap.entrySet().stream() //full check implementation
                .filter(e -> e.getKey().startsWith(usernamePrefix))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());*/

    }

    @Override
    public User save(User entity) {
        User oldUser = userMap.get(entity.getUserName());
        if (oldUser == null) {
            //new user case
            entity.setId(idProvider.provideIdentity(User.class));
            userMap.put(entity.getUserName(), entity);
            return entity;
        }
        if (oldUser == entity) {
            //do nothing because entity updates are automatically synced to repository
            return entity;
        }
        // illegal state, entity and oldUser have same username but different objects
        throw new IllegalStateException("error - users with duplicate usernames");

    }

}
