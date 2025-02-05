package com.PicPaySimplificado.services.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.PicPaySimplificado.domain.entities.AccountType;
import com.PicPaySimplificado.domain.entities.User;
import com.PicPaySimplificado.domain.repositories.UserRepository;
import com.PicPaySimplificado.dtos.user.UserRequestDto;
import com.PicPaySimplificado.dtos.user.UserResponseDto;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByDocument(String document) {
        return userRepository.findByDocument(formatDocument(document));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto data) {

        // Remove all non-numeric characters from Document
        String document = formatDocument(data.document());

        User user = User.builder()
                .firstName(data.firstName())
                .lastName(data.lastName())
                .email(data.email())
                .password(data.password())
                .document(document)
                .balance(generateBalance())
                .type(AccountType.PF)
                .build();

        // Save user in DB > map to DTO > return
        return mapToUserResponseDto(userRepository.save(user));
    }

    private Double generateBalance() {
        return (double) Math.round(Math.random() * 1000);
    }

    private String formatDocument(String document) {
        return document.replaceAll("\\D", "");
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        return new UserResponseDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getDocument(),
                user.getBalance(), user.getType().toString());
    }

}
