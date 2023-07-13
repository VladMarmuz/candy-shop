package com.candyshop.service;

import com.candyshop.auth.JwtEntityFactory;
import com.candyshop.entity.Basket;
import com.candyshop.entity.User;
import com.candyshop.entity.enums.Role;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.BasketRepository;
import com.candyshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BasketRepository basketRepository;


    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getById", key = "#userId")
    public User getById(Long userId) {
        log.info("*** Request to get a user by ID ***");
        User currentUser = userRepository.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("*** User successfully found by ID ***");
        return currentUser;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "UserService:getByEmail", key = "#email")
    public User getByEmail(String email) {
        log.info("*** Request to get a user by email ***");
        User currentUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("*** User successfully found by email ***");
        return currentUser;
    }

    @Transactional
    @Caching(cacheable = {
            @Cacheable(value = "UserService::getById", key = "#user.id"),
            @Cacheable(value = "UserService::getByEmail", key = "#user.email")
    })
    public User create(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setEnabled(false);

        Basket createdBasket = createBasket(user);

        user.setBasket(createdBasket);
        userRepository.save(user);
        return user;
    }

    Basket createBasket(User user) {
        Basket basket = new Basket();
        basket.setUser(user);
        return basketRepository.save(basket);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User currentUser = getByEmail(email);
        return JwtEntityFactory.create(currentUser);
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()){
            throw new ResourceNotFoundException("There are doesn't have users in the db");
        }
        return allUsers;
    }

    @Transactional
    @CacheEvict(value = "UserService::getById", key = "#userId")
    public void deleteUser(Long userId) {
        User currentUser = userRepository.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.deleteById(currentUser.getId());
    }

    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#userId"),
            @CachePut(value = "UserService::getByEmail", key = "#user.email"),
    })
    public User update(Long userId, User user) {
        User currentUser = getById(userId);
        currentUser.setName(user.getName());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        currentUser.setRole(user.getRole());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(currentUser);
    }
}
