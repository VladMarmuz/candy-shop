package com.candyshop.controller;

import com.candyshop.dto.UserDTO;
import com.candyshop.dto.UserUpdateRequest;
import com.candyshop.entity.User;
import com.candyshop.mappers.UserMapper;
import com.candyshop.mappers.UserRequestMapper;
import com.candyshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRequestMapper requestMapper;

    @GetMapping("/{userId}")
    @Operation(summary = "Method for find one user by userId")
    public UserDTO getUser(@PathVariable Long userId) {
        User currentUser = userService.getById(userId);
        return userMapper.toDto(currentUser);
    }

    @GetMapping("/")
    @Operation(summary = "Get all users")
    public List<UserDTO> getAllUsers() {
        List<User> currentUsers = userService.getAll();
        return currentUsers.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @DeleteMapping("/{userId}")
    public void deleteUserByUserId(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update User")
    public UserDTO update(@PathVariable Long userId,
                          @Validated @RequestBody UserUpdateRequest updateRequest) {
        User user = requestMapper.toEntity(updateRequest);
        User updatedUser = userService.update(userId, user);
        return userMapper.toDto(updatedUser);
    }


}
