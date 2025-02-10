package com.PicPaySimplificado.services.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.PicPaySimplificado.domain.entities.AccountType;
import com.PicPaySimplificado.domain.entities.User;
import com.PicPaySimplificado.domain.repositories.UserRepository;
import com.PicPaySimplificado.dtos.user.UserRequestDto;
import com.PicPaySimplificado.dtos.user.UserResponseDto;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    UserRequestDto userRequestDto;
    User user;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto("First", "Last", "email", "1234", "CPF");
        user = User.builder().id(1L).firstName("First").lastName("Last").email("email").password("1234").document("CPF")
                .type(AccountType.PF).build();
    }

    @Test
    public void testFindByEmailSuccess() {
        String email = "test@email.com";
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> possibleUser = userService.findByEmail(email);

        assertTrue(possibleUser.isPresent(), "User with the provided email should be found.");
        assertEquals(user, possibleUser.get(), "The returned user should be the same as the mocked one.");
    }

    @Test
    public void testFindByEmailNotFound() {
        String email = "nonexistent@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> possibleUser = userService.findByEmail(email);

        assertFalse(possibleUser.isPresent(), "User with the provided email should not be found.");
    }

    @Test
    public void testFindByDocumentSuccess() {
        String document = "123.456.789-00";
        user.setDocument(document);
        when(userRepository.findByDocument("12345678900")).thenReturn(Optional.of(user));

        Optional<User> possibleUser = userService.findByDocument(document);

        assertTrue(possibleUser.isPresent(), "User with the provided document should be found.");
        assertEquals(user, possibleUser.get(), "The returned user should be the same as the mocked one.");
    }

    @Test
    public void testFindByDocumentNotFound() {
        String document = "111.222.333-44";
        when(userRepository.findByDocument("11122233344")).thenReturn(Optional.empty());

        Optional<User> possibleUser = userService.findByDocument(document);

        assertFalse(possibleUser.isPresent(), "User with the provided document should not be found.");
    }

    @Test
    public void testFindByIdSuccess() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> possibleUser = userService.findById(userId);

        assertTrue(possibleUser.isPresent(), "User with the provided ID should be found.");
        assertEquals(user, possibleUser.get(), "The returned user should be the same as the mocked one.");
    }

    @Test
    public void testFindByIdNotFound() {
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> possibleUser = userService.findById(userId);

        assertFalse(possibleUser.isPresent(), "User with the provided ID should not be found.");
    }

    @Test
    public void testCreateUserSuccess() {

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto possibleUser = userService.createUser(userRequestDto, AccountType.PF);

        assertNotNull(possibleUser, "The response should not be null");
        assertEquals(user.getFirstName(), possibleUser.firstName(),
                "The user's first name should match the provided first name.");
        assertEquals(user.getEmail(), possibleUser.email(), "The user's email should match the provided email.");
        assertEquals(user.getType().toString(), possibleUser.type(),
                "The user's account type should match the provided account type.");
    }

}
