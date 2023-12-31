package com.candyshop.service.impl;

import com.candyshop.auth.JwtEntityFactory;
import com.candyshop.entity.Basket;
import com.candyshop.entity.User;
import com.candyshop.entity.enums.Role;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.BasketRepository;
import com.candyshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BasketRepository basketRepository;

    @Transactional(readOnly = true)
    public User getById(final Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getByEmail(final String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public User create(final User user) {
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

    Basket createBasket(final User user) {
        Basket basket = new Basket();
        basket.setUser(user);
        return basketRepository.save(basket);
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        User currentUser = getByEmail(email);
        return JwtEntityFactory.create(currentUser);
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            throw new ResourceNotFoundException("There are doesn't have users in the db");
        }
        return allUsers;
    }

    @Transactional
    public void deleteUser(final Long userId) {
        User currentUser = userRepository.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.deleteById(currentUser.getId());
    }

    @Transactional
    public User update(final Long userId, final User user) {
        User currentUser = getById(userId);
        currentUser.setName(user.getName());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        currentUser.setRole(user.getRole());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(currentUser);
    }

}
