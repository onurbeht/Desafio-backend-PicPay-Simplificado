package com.PicPaySimplificado.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.PicPaySimplificado.domain.entities.AccountType;
import com.PicPaySimplificado.domain.entities.User;
import com.PicPaySimplificado.dtos.user.UserRequestDto;
import com.PicPaySimplificado.dtos.user.UserResponseDto;
import com.PicPaySimplificado.services.user.UserService;
import com.PicPaySimplificado.services.validate.ValidateCnpj;
import com.PicPaySimplificado.services.validate.ValidateCpf;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        ObjectMapper mapper;

        @MockitoBean
        UserService userService;

        private MockedStatic<ValidateCpf> validateCpf;
        private MockedStatic<ValidateCnpj> validateCnpj;

        User user;
        UserRequestDto userRequestDto;
        UserResponseDto userResponseDto;

        @BeforeEach
        void setUp() {

                user = User.builder().id(1L).firstName("First").lastName("Last").email("email").password("1234")
                                .document("CPF")
                                .type(AccountType.PF).build();

                userRequestDto = new UserRequestDto("First", "Last", "email", "1234", "CPF");

                userResponseDto = new UserResponseDto(1L, "First", "Last", "email", "CPF", 100.0,
                                AccountType.PF.toString());

                validateCpf = mockStatic(ValidateCpf.class);
                validateCnpj = mockStatic(ValidateCnpj.class);
        }

        @AfterEach
        void tearDown() {
                validateCpf.close();
                validateCnpj.close();
        }

        // Test for invalid CPF
        @Test
        @DisplayName("Should return status BadRequest, when document(cpf) is invalid")
        void shouldReturnBadRequestWhenCpfIsInvalid() throws Exception {
                // Arrange
                validateCpf.when(() -> ValidateCpf.valid(anyString())).thenReturn(false);

                // Act
                MvcResult result = mockMvc.perform(post("/user/pf")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequestDto)))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // Assert
                assertTrue(result.getResponse().getContentAsString().contains("CPF Invalido!!!"),
                                "Invalid CPF should return error message.");

                verify(userService, times(0)).findByEmail(anyString());
        }

        // Test for already registered email
        @Test
        @DisplayName("Should return status BadRequest, when Email already exist")
        void shouldReturnBadRequestWhenEmailIsAlreadyRegisteredPf() throws Exception {
                // Arrange
                validateCpf.when(() -> ValidateCpf.valid(anyString())).thenReturn(true);

                when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));

                // Act
                MvcResult result = mockMvc.perform(post("/user/pf")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequestDto)))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // Assert
                assertTrue(result.getResponse().getContentAsString().contains("Email já cadastrado!!!"),
                                "Email already registered should return error message.");

                verify(userService, times(1)).findByEmail(anyString());
        }

        // Test for already registered CPF
        @Test
        @DisplayName("Should return status BadRequest, when Document(cpf) already exist")
        void shouldReturnBadRequestWhenCpfIsAlreadyRegisteredPf() throws Exception {
                // Arrange
                validateCpf.when(() -> ValidateCpf.valid(anyString())).thenReturn(true);

                when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
                when(userService.findByDocument(anyString())).thenReturn(Optional.of(user));

                // Act
                MvcResult result = mockMvc.perform(post("/user/pf")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequestDto)))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // Assert
                assertTrue(result.getResponse().getContentAsString().contains("CPF já cadastrado!!!"),
                                "CPF already registered should return error message.");

                verify(userService, times(1)).findByEmail(anyString());
                verify(userService, times(1)).findByDocument(anyString());
        }

        // Test for successful user creation
        @Test
        @DisplayName("Should return status Created, and userRequestDto")
        void shouldCreateUserPfSuccessfullyWhenValidData() throws Exception {
                // Arrange
                validateCpf.when(() -> ValidateCpf.valid(anyString())).thenReturn(true);
                when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
                when(userService.findByDocument(anyString())).thenReturn(Optional.empty());
                when(userService.createUser(userRequestDto, AccountType.PF)).thenReturn(userResponseDto);

                // Act
                ResultActions result = mockMvc.perform(post("/user/pf")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequestDto)));

                // Assert
                result
                                .andExpect(header().exists("Location"))
                                .andExpect(header().string("Location", "http://localhost:8080/api/user/1"))
                                .andExpect(jsonPath("$.type").value(user.getType().toString()));
        }

        @Test
        @DisplayName("Should return status BadRequest, when document(cnpj) is invalid")
        void shouldReturnBadRequestWhenCnpjIsInvalid() throws Exception {
                // Arrange
                validateCnpj.when(() -> ValidateCnpj.valid(anyString())).thenReturn(false);

                // Act
                MvcResult result = mockMvc.perform(post("/user/pj")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequestDto)))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // Assert
                assertTrue(result.getResponse().getContentAsString().contains("CNPJ Invalido!!!"),
                                "Invalid CNPJ should return error message.");

                verify(userService, times(0)).findByEmail(anyString());
        }

        // Test for already registered email
        @Test
        @DisplayName("Should return status BadRequest, when Email already exist")
        void shouldReturnBadRequestWhenEmailIsAlreadyRegisteredPj() throws Exception {
                // Arrange
                validateCnpj.when(() -> ValidateCnpj.valid(anyString())).thenReturn(true);

                when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));

                // Act
                MvcResult result = mockMvc.perform(post("/user/pj")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequestDto)))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // Assert
                assertTrue(result.getResponse().getContentAsString().contains("Email já cadastrado!!!"),
                                "Email already registered should return error message.");

                verify(userService, times(1)).findByEmail(anyString());
        }

        // Test for already registered CPF
        @Test
        @DisplayName("Should return status BadRequest, when Document(cnpj) already exist")
        void shouldReturnBadRequestWhenCnpjIsAlreadyRegisteredPj() throws Exception {
                // Arrange
                validateCnpj.when(() -> ValidateCnpj.valid(anyString())).thenReturn(true);

                when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
                when(userService.findByDocument(anyString())).thenReturn(Optional.of(user));

                // Act
                MvcResult result = mockMvc.perform(post("/user/pj")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequestDto)))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // Assert
                assertTrue(result.getResponse().getContentAsString().contains("CNPJ já cadastrado!!!"),
                                "CNPJ already registered should return error message.");

                verify(userService, times(1)).findByEmail(anyString());
                verify(userService, times(1)).findByDocument(anyString());
        }

        // Test for successful user creation
        @Test
        @DisplayName("Should return status Created, and userRequestDto")
        void shouldCreateUserPjSuccessfullyWhenValidData() throws Exception {
                // Arrange
                user.setType(AccountType.PJ);
                userResponseDto = new UserResponseDto(1L, "First", "Last", "email", "CPF", 100.0,
                                AccountType.PJ.toString());

                validateCnpj.when(() -> ValidateCnpj.valid(anyString())).thenReturn(true);
                when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
                when(userService.findByDocument(anyString())).thenReturn(Optional.empty());
                when(userService.createUser(userRequestDto, AccountType.PJ)).thenReturn(userResponseDto);

                // Act
                ResultActions result = mockMvc.perform(post("/user/pj")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequestDto)));

                // Assert
                result
                                .andExpect(status().isCreated())
                                .andExpect(header().exists("Location"))
                                .andExpect(header().string("Location", "http://localhost:8080/api/user/1"))
                                .andExpect(jsonPath("$.type").value(user.getType().toString()));
        }

}
