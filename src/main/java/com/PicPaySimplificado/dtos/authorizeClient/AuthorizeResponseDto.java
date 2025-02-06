package com.PicPaySimplificado.dtos.authorizeClient;

public record AuthorizeResponseDto(
        String status,
        Data data) {

    // {"status" : "success", "data" : { "authorization" : true }}
}

final record Data(
        boolean authorization) {

}