package com.springwalker.back.user.service;

import com.springwalker.back.user.dto.UserRequestDTO;
import com.springwalker.back.user.dto.UserResponseDTO;
import com.springwalker.back.user.mapper.UserMapper;
import com.springwalker.back.user.model.User;
import com.springwalker.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CriarUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO execute(UserRequestDTO requestDTO) {
        if (userRepository.findByUsername(requestDTO.username()).isPresent()) {
            throw new IllegalArgumentException("Username já cadastrado.");
        }

        String encryptedPassword = passwordEncoder.encode(requestDTO.password());
        User newUser = new User(requestDTO.username(), encryptedPassword, requestDTO.role());

        User UserSalvo = userRepository.save(newUser);

        return userMapper.toResponseDTO(UserSalvo);
    }
}
