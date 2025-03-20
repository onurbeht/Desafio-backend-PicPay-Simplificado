package com.PicPaySimplificado.controllers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

    @BeforeEach
    void setUp() {
        transferRequestDto = new TransferRequestDto(100.0, 2L, 1L);
        transferResponseDto = new TransferResponseDto(
                "Amount transferred successfully to ID: " + transferRequestDto.receiverId());

    }

    // Test for successful transfer
    @Test
    @DisplayName("Successful transfer should return success message.")
    void shouldTransferAmountSuccessfullyWhenValidData() throws Exception {
        // Arrange
        doNothing().when(transactionService).executeTransfer(transferRequestDto.senderId(),
                transferRequestDto.receiverId(), transferRequestDto.amount());

        // Act
        ResultActions result = mockMvc.perform(post("/transfer/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transferRequestDto)));
        // Assert
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(transferResponseDto.message()));

    }

    // Test for error
    @Test
    @DisplayName("Error transfer when senderID == receiverID, should return error message and status badRequest.")
    void shouldNotTransferAmountWhenDataIsInvalid() throws Exception {
        // Arrange
        transferRequestDto = new TransferRequestDto(10.0, 1L, 1L);

        // Act
        ResultActions result = mockMvc.perform(post("/transfer/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transferRequestDto)));
        // Assert
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("SenderID cannot be equals to ReceiverID"));

    }

}
