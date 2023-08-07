package com.candyshop.controller;

import com.candyshop.dto.UserDto;
import com.candyshop.dto.UserUpdateRequest;
import com.candyshop.entity.User;
import com.candyshop.mappers.UserMapper;
import com.candyshop.mappers.UserRequestMapper;
import com.candyshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public UserDto getUser(@PathVariable final Long userId) {
        User currentUser = userService.getById(userId);
        return userMapper.toDto(currentUser);
    }

    @GetMapping("/")
    @Operation(summary = "Get all users")
    public List<UserDto> getAllUsers() {
        List<User> currentUsers = userService.getAll();
        return currentUsers.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @DeleteMapping("/{userId}")
    public String deleteUserByUserId(@PathVariable final Long userId) {
        userService.deleteUser(userId);
        return "User successfully deleted";
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update User")
    public UserDto update(@PathVariable final Long userId,
                          @Validated @RequestBody final UserUpdateRequest updateRequest) {
        User user = requestMapper.toEntity(updateRequest);
        User updatedUser = userService.update(userId, user);
        return userMapper.toDto(updatedUser);
    }
}
