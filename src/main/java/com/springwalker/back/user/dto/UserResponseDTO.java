package com.springwalker.back.user.dto;

import com.springwalker.back.user.role.Role;
import com.springwalker.back.user.model.User;

public record UserResponseDTO(Long id, String username, Role role) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getUsername(), user.getRole());
    }
}
