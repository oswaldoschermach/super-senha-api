package com.nebula_soft.super_senha.dto;

import com.nebula_soft.super_senha.Entity.WordEntity;
import com.nebula_soft.super_senha.Enum.WordStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WordDTO {

    private Long id;
    private String word;
    private String difficultyLevel;
    private String status;

    // Construtores, getters e setters omitidos para brevidade

    // Método estático para converter de WordEntity para WordDTO
    public static WordDTO fromEntity(WordEntity wordEntity) {
        WordDTO dto = new WordDTO();
        dto.setId(wordEntity.getId());
        dto.setWord(wordEntity.getWord());
        dto.setDifficultyLevel(wordEntity.getDifficultyLevel().getLevelName());
        dto.setStatus(wordEntity.getStatus().name());
        return dto;
    }
}
