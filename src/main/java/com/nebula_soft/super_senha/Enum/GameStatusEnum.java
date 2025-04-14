package com.nebula_soft.super_senha.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameStatusEnum {
    LOBBY("Aguardando jogadores"),
    IN_PROGRESS("Jogo em andamento"),
    COMPLETED("Jogo finalizado"),
    ABANDONED("Jogo abandonado");

    private final String description;

    public boolean canStartGame() {
        return this == LOBBY;
    }

    public boolean isActive() {
        return this == LOBBY || this == IN_PROGRESS;
    }
}