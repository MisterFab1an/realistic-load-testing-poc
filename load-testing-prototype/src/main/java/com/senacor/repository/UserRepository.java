package com.senacor.repository;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public class UserRepository {
    public Optional<UserDetails> findByUsername(String username){
        var randomBcryptPassword = BCrypt.hashpw(RandomStringUtils.randomAlphanumeric(15), BCrypt.gensalt());
        return Optional.of(new User(username, randomBcryptPassword, new ArrayList<>()));
    }
}
