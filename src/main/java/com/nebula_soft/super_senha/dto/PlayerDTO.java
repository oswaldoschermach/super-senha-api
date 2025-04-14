package com.nebula_soft.super_senha.dto;

import com.nebula_soft.super_senha.Entity.PlayerEntity;
import jdk.jshell.Snippet;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PlayerDTO {

    private Long id;
    private String playerName;
    private Integer points;
    private boolean ready;
    private String status;

    public static PlayerDTO fromEntity(PlayerEntity player) {
        return new PlayerDTO(
                player.getId(),
                player.getPlayerName(),
                player.getPoints(),
                player.isReady(),
                player.getStatus().name()
        );
    }

}