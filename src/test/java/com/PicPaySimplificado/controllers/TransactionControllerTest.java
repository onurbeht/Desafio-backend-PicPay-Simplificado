package com.PicPaySimplificado.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.PicPaySimplificado.domain.entities.AccountType;
import com.PicPaySimplificado.domain.entities.User;
import com.PicPaySimplificado.dtos.transfer.TransferRequestDto;
import com.PicPaySimplificado.dtos.transfer.TransferResponseDto;
import com.PicPaySimplificado.services.transaction.TransactionService;
import com.PicPaySimplificado.services.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    UserService userService;

    @MockitoBean
    TransactionService transactionService;

    TransferRequestDto transferRequestDto;
    TransferResponseDto transferResponseDto;

    User receiver;
    User sender;

    @BeforeEach
    void setUp() {
        transferRequestDto = new TransferRequestDto(100.0, 2L, 1L);
        transferResponseDto = new TransferResponseDto(
                "Amount transferred successfully to ID: " + transferRequestDto.receiverId());

        receiver = User.builder()
                .id(1L)
                .firstName("First")
                .lastName("Last")
                .email("email")
                .password("1234")
                .document("CPF")
                .type(AccountType.PF)
                .balance(150.0)
                .build();
        sender = User.builder()
                .id(2L)
                .firstName("First")
                .lastName("Last")
                .email("email")
                .password("1234")
                .document("CPF")
                .type(AccountType.PF)
                .balance(200.0)
                .build();
    }

    // Test for invalid senderId
    @Test
    @DisplayName("Invalid senderId, should return error message.")
    void shouldReturnBadRequestWhenSenderIdIsInvalid() throws Exception {
        // Arrange
        when(userService.findById(anyLong())).thenReturn(Optional.empty()); // Mocking sender not found

        // Act
        ResultActions result = mockMvc.perform(post("/transfer/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transferRequestDto)));

        // Assert
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("SenderId Invalid"));
    }

    // Test for sender not being PF account type
    @Test
    @DisplayName("Non-PF sender should return, badRequest and error message.")
    void shouldReturnBadRequestWhenSenderIsNotPF() throws Exception {
        // Arrange
        sender.setType(AccountType.PJ); // Changing sender account type to PJ
        when(userService.findById(2L)).thenReturn(Optional.of(sender)); // Mocking sender found

        // Act
        ResultActions result = mockMvc.perform(post("/transfer/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transferRequestDto)));

        // Assert
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Only accounts of type PF, can send Transfers"));

    }

    // Test for insufficient funds
    @Test
    @DisplayName("Sender with insufficient funds should return, badRequest and error message.")
    void shouldReturnBadRequestWhenSenderHasInsufficientFunds() throws Exception {
        // Arrange
        sender.setBalance(50.0); // Reducing sender balance to simulate insufficient funds
        when(userService.findById(2L)).thenReturn(Optional.of(sender)); // Mocking sender found

        // Act
        ResultActions result = mockMvc.perform(post("/transfer/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transferRequestDto)));
        // Assert
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Insufficient funds"));
    }

    // Test for invalid receiverId
    @Test
    @DisplayName("Invalid receiverId should return badRequest and error message.")
    void shouldReturnBadRequestWhenReceiverIdIsInvalid() throws Exception {
        // Arrange
        when(userService.findById(2L)).thenReturn(Optional.of(sender));
        when(userService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResultActions result = mockMvc.perform(post("/transfer/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transferRequestDto)));
        // Assert
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("ReceiverId Invalid"));

    }

    // Test for successful transfer
    @Test
    @DisplayName("Successful transfer should return success message.")
    void shouldTransferAmountSuccessfullyWhenValidData() throws Exception {
        // Arrange
        when(userService.findById(2L)).thenReturn(Optional.of(sender));
        when(userService.findById(1L)).thenReturn(Optional.of(receiver));
        doNothing().when(transactionService).executeTransfer(any(User.class), any(User.class), anyDouble());

        // Act
        ResultActions result = mockMvc.perform(post("/transfer/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transferRequestDto)));
        // Assert
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(transferResponseDto.message()));

    }

}
