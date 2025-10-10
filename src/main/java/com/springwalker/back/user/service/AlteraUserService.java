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
public class AlteraUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO execute(Long id, UserRequestDTO requestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        userMapper.updateFromDto(requestDTO, existingUser);

        if (requestDTO.password() != null && !requestDTO.password().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(requestDTO.password());
            existingUser.setPassword(encryptedPassword);
        }

        User UserAtualizado = userRepository.save(existingUser);

        return userMapper.toResponseDTO(UserAtualizado);
    }
}
