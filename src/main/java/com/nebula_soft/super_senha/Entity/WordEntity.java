package com.nebula_soft.super_senha.Entity;

import com.nebula_soft.super_senha.Enum.WordStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "words")
@Getter
@Setter
@NoArgsConstructor
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word", nullable = false, unique = true)
    private String word;

    @ManyToMany
    @JoinTable(
            name = "word_game_room",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "game_room_id")
    )
    private Set<GameRoomEntity> gameRooms = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private WordStatusEnum status = WordStatusEnum.AVAILABLE;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficulty_level_id", nullable = false)
    private DifficultyLevelEntity difficultyLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_room_id")
    private GameRoomEntity gameRoom;
}