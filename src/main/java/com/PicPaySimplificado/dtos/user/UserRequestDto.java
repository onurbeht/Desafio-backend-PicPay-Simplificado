package com.PicPaySimplificado.dtos.user;

public record UserRequestDto(
                String firstName,
                String lastName,
                String email,
                String password,
                String document) {

}
