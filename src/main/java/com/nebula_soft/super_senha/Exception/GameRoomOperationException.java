package com.nebula_soft.super_senha.Exception;

public class GameRoomOperationException extends RuntimeException {
    public GameRoomOperationException(String message) {
        super(message);
    }

    public GameRoomOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}