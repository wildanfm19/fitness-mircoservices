package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not found!"));


        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return userResponse;
    }

    public UserResponse register(@Valid RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exist");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        User savedUser = userRepository.save(user);
        UserResponse userResponse = UserResponse.builder()
                .id(savedUser.getId())
                .password(savedUser.getPassword())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();

        return userResponse;
    }

    public Boolean existByUserId(String userId) {
        return userRepository.existsById(userId);
    }
}
