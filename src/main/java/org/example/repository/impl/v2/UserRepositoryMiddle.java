package org.example.repository.impl.v2;

import lombok.Setter;
import org.example.domain_entities.User;
import org.example.repository.UserRepository;
import org.example.repository.impl.v2.hibernate.TrainingTypeHibernateRepository;
import org.example.repository.impl.v2.hibernate.UserHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@Deprecated
public class UserRepositoryMiddle implements UserRepository {
    @Setter(onMethod_={@Autowired})
    private UserHibernateRepository hibernateUser;
    @Override
    public Optional<User> get(String username) {
        return hibernateUser.findUserByUserName(username);
    }

    @Override
    public List<User> getAllByPrefix(String usernamePrefix) {
        return hibernateUser.findUsersByUserNameIsStartingWith(usernamePrefix);
    }

    @Override
    public User save(User entity) {
        return hibernateUser.save(entity);
    }
}
