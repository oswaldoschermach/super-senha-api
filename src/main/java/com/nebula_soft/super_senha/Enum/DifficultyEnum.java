package com.nebula_soft.super_senha.Enum;

import lombok.Getter;

@Getter
public enum DifficultyEnum {

    EASY(1, "Fácil"),
    MEDIUM(2, "Médio"),
    HARD(3, "Difícil");

    private final int id;
    private final String difficulty;

    DifficultyEnum(int id, String difficulty) {
        this.id = id;
        this.difficulty = difficulty;
    }

}
