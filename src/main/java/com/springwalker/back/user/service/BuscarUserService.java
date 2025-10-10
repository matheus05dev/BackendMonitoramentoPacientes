package com.springwalker.back.user.service;

import com.springwalker.back.user.dto.UserResponseDTO;
import com.springwalker.back.user.mapper.UserMapper;
import com.springwalker.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscarUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponseDTO> buscarTodos() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> buscarPorId(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDTO);
    }

    public Optional<UserResponseDTO> buscarPorUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toResponseDTO);
    }
}
