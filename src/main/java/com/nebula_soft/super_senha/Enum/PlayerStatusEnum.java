package com.nebula_soft.super_senha.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlayerStatusEnum {
    CONNECTED("Conectado"),
    DISCONNECTED("Desconectado"),
    AFK("Ausente");

    private final String description;
}