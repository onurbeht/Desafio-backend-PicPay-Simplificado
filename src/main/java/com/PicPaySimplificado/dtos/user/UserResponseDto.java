package com.PicPaySimplificado.dtos.user;

public record UserResponseDto(
                String firstName,
                String lastName,
                String email,
                String document,
                Double balance,
                String type) {

}
