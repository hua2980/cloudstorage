package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private UserMapper userMapper;

    public HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    @PostConstruct
    public void postConstruct(){
        System.out.println("Creating UserService");
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.selectByUserName(username) == null;
    }

    public int register(User user){
        // encrypt user password
        // 1. get salt code
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        // 2. hash password with salt
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        // create user object
        User newUser = new User(user.getUsername(), encodedSalt,
                hashedPassword, user.getFirstName(), user.getLastName());
        return userMapper.insert(newUser);
    }

    public User getUser(String username) {
        return userMapper.selectByUserName(username);
    }
}
