package com.PicPaySimplificado.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.PicPaySimplificado.domain.entities.AccountType;
import com.PicPaySimplificado.dtos.user.UserRequestDto;
import com.PicPaySimplificado.dtos.user.UserResponseDto;
import com.PicPaySimplificado.services.user.UserService;
import com.PicPaySimplificado.services.validate.ValidateCnpj;
import com.PicPaySimplificado.services.validate.ValidateCpf;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
        // this.validateCpf = validateCpf;
    }

    @PostMapping("/pf")
    public ResponseEntity<?> registerUserPf(@RequestBody UserRequestDto data, UriComponentsBuilder uriBuilder) {

        // Check if cpf is valid
        if (!ValidateCpf.valid(data.document())) {
            return ResponseEntity.badRequest().body("CPF Invalido!!!");
        }

        // Check if email already is registered
        if (userService.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado!!!");
        }

        // Check if cpf already is registered
        if (userService.findByDocument(data.document()).isPresent()) {
            return ResponseEntity.badRequest().body("CPF já cadastrado!!!");
        }

        UserResponseDto user = userService.createUser(data, AccountType.PF);

        var uri = uriBuilder.port(8080).path("/api/user/{id}").buildAndExpand(user.id()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @PostMapping("/pj")
    public ResponseEntity<?> registerUserPj(@RequestBody UserRequestDto data, UriComponentsBuilder uriBuilder) {

        // Check if cnpj is valid
        if (!ValidateCnpj.valid(data.document())) {
            return ResponseEntity.badRequest().body("CNPJ Invalido!!!");
        }

        // Check if email already is registered
        if (userService.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado!!!");
        }

        // Check if cnpj already is registered
        if (userService.findByDocument(data.document()).isPresent()) {
            return ResponseEntity.badRequest().body("CNPJ já cadastrado!!!");
        }

        UserResponseDto user = userService.createUser(data, AccountType.PJ);

        var uri = uriBuilder.port(8080).path("/api/user/{id}").buildAndExpand(user.id()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

}
