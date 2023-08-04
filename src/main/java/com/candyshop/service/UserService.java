package com.candyshop.service;

import com.candyshop.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    User getById(Long userId);

    User getByEmail(String email);

    User create(User user);

    UserDetails loadUserByUsername(String email);

    List<User> getAll();

    void deleteUser(Long userId);

    User update(Long userId, User user);
}
