package com.nebula_soft.super_senha.Entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nebula_soft.super_senha.Enum.PlayerStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "players", indexes = {
        @Index(name = "idx_player_name", columnList = "player_name")
})
@Getter
@Setter
@NoArgsConstructor
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Player name is required")
    @Size(min = 3, max = 30, message = "Player name must be between 3 and 30 characters")
    @Column(name = "player_name", nullable = false, unique = true)
    private String playerName;

    @Min(value = 0, message = "Points cannot be negative")
    @Column(nullable = false)
    private Integer points = 0;

    @Column(nullable = false)
    private boolean ready = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlayerStatusEnum status = PlayerStatusEnum.CONNECTED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_room_id")
    @JsonBackReference
    private GameRoomEntity gameRoom;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Integer version;
}