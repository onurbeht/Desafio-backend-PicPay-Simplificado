package com.PicPaySimplificado.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.PicPaySimplificado.dtos.authorizeClient.AuthorizeResponseDto;

@FeignClient(name = "authorize-client", url = "https://util.devi.tools/api/v2/authorize")
public interface AuthorizeClient {

    @GetMapping
    public AuthorizeResponseDto authorize();

}
