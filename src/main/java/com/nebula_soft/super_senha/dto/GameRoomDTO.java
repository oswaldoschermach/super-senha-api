package com.nebula_soft.super_senha.dto;

import com.nebula_soft.super_senha.Entity.GameRoomEntity;
import com.nebula_soft.super_senha.Enum.GameStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class GameRoomDTO {

    private Long id;
    private String name;
    private GameStatusEnum status;
    private List<PlayerDTO> players;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static GameRoomDTO fromEntity(GameRoomEntity gameRoom) {
        return new GameRoomDTO(
                gameRoom.getId(),
                gameRoom.getName(),
                gameRoom.getStatus(),
                gameRoom.getPlayers().stream()
                        .map(PlayerDTO::fromEntity)
                        .collect(Collectors.toList()),
                gameRoom.getCreatedAt(),
                gameRoom.getUpdatedAt()
        );
    }
}