package com.nebula_soft.super_senha.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nebula_soft.super_senha.Enum.GameStatusEnum;
import com.nebula_soft.super_senha.Exception.GameRuleException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "game_rooms")
@Getter
@Setter
@NoArgsConstructor
public class GameRoomEntity {

    public static final int MAX_PLAYERS = 8;
    public static final int MIN_PLAYERS = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room name is required")
    @Size(min = 3, max = 50, message = "Room name must be between 3 and 50 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatusEnum status = GameStatusEnum.LOBBY;

    @OneToMany(mappedBy = "gameRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayerEntity> players = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Integer version;

    @ManyToMany(mappedBy = "gameRooms")
    private Set<WordEntity> words = new HashSet<>();

    public void addPlayer(PlayerEntity player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (!isJoinable()) {
            throw new GameRuleException("Room is not joinable");
        }
        players.add(player);
        player.setGameRoom(this);
    }

    public void removePlayer(PlayerEntity player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        players.remove(player);
        player.setGameRoom(null);
    }

    public boolean isJoinable() {
        return status == GameStatusEnum.LOBBY && players.size() < MAX_PLAYERS;
    }

    public void startGame() {
        if (!isJoinable()) {
            throw new GameRuleException("Invalid game status for starting");
        }
        if (players.size() < MIN_PLAYERS) {
            throw new GameRuleException("Not enough players to start the game");
        }
        this.status = GameStatusEnum.IN_PROGRESS;
    }

    public void endGame(boolean completed) {
        this.status = completed ? GameStatusEnum.COMPLETED : GameStatusEnum.ABANDONED;
    }

    public List<PlayerEntity> drawPairs() {
        if (players.size() < 2) {
            throw new GameRuleException("Not enough players to draw pairs");
        }
        Collections.shuffle(players);
        return players.subList(0, 2);
    }

}