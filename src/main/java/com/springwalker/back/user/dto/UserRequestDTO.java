package com.springwalker.back.user.dto;

import com.springwalker.back.user.role.Role;

public record UserRequestDTO(String username, String password, Role role) {
}
