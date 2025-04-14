package com.nebula_soft.super_senha.Exception;

public class PlayerOperationException extends RuntimeException {
    public PlayerOperationException(String message) {
        super(message);
    }

    public PlayerOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}