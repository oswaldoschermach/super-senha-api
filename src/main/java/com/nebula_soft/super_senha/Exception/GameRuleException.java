package com.nebula_soft.super_senha.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando uma regra de negócio do jogo é violada.
 * Retorna HTTP 422 (Unprocessable Entity) por padrão.
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class GameRuleException extends RuntimeException {

    public GameRuleException(String message) {
        super(message);
    }

    public static GameRuleException forNotEnoughPlayers(int minPlayers) {
        return new GameRuleException(
                String.format("Mínimo de %d jogadores necessário", minPlayers)
        );
    }
}