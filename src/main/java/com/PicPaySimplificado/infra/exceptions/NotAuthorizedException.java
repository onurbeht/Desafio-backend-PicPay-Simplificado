package com.PicPaySimplificado.infra.exceptions;

public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException(String message) {
        super(message);
    }

}
