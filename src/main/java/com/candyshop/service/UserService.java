package com.candyshop.service;

import com.candyshop.auth.JwtEntityFactory;
import com.candyshop.entity.User;
import com.candyshop.entity.enums.Role;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public User getById(Long userId) {
        log.info("*** Request to get a user by ID ***");
        User currentUser = userRepository.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("*** User successfully found by ID ***");
        return currentUser;
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        log.info("*** Request to get a user by email ***");
        User currentUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("*** User successfully found by email ***");
        return currentUser;
    }

    public User create(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setEnabled(false);
        userRepository.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User currentUser = getByEmail(email);
        return JwtEntityFactory.create(currentUser);
    }

}
