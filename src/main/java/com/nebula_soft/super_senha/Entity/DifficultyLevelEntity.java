package com.nebula_soft.super_senha.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "difficulty_levels")
@Getter
@Setter
@NoArgsConstructor
public class DifficultyLevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level_name", nullable = false, unique = true)
    private String levelName; // Exemplo: "Fácil", "Médio", "Difícil"

    @OneToMany(mappedBy = "difficultyLevel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WordEntity> words = new ArrayList<>();
}