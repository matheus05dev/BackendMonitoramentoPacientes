package com.springwalker.back.user.mapper;

import com.springwalker.back.user.dto.UserRequestDTO;
import com.springwalker.back.user.dto.UserResponseDTO;
import com.springwalker.back.user.model.User;
import com.springwalker.back.user.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setup() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    @DisplayName("Mapear UserRequestDTO para User")
    void toEntity() {
        UserRequestDTO dto = new UserRequestDTO("test.user", "password", Role.ADMIN);

        User user = userMapper.toEntity(dto);

        assertEquals("test.user", user.getUsername());
        assertNull(user.getPassword());
    }

    @Test
    @DisplayName("Mapear User para UserResponseDTO")
    void toResponseDTO() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test.user");

        UserResponseDTO dto = userMapper.toResponseDTO(user);

        assertEquals(1L, dto.id());
        assertEquals("test.user", dto.username());
    }

    @Test
    @DisplayName("Atualizar User a partir de UserRequestDTO")
    void updateFromDto() {
        UserRequestDTO dto = new UserRequestDTO("updated.user", "newPassword", Role.ADMIN);

        User user = new User();
        user.setUsername("original.user");
        user.setPassword("originalPassword");

        userMapper.updateFromDto(dto, user);

        assertEquals("updated.user", user.getUsername());
        assertEquals("originalPassword", user.getPassword());
    }
}
