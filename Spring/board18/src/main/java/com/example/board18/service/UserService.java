package com.example.board18.service;

import com.example.board18.entity.Role;
import com.example.board18.entity.User;
import com.example.board18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnable(true);

        Role role = new Role();

        role.setId(1l);

        user.getRoles().add(role);
        return userRepository.save(user);
    }

}
