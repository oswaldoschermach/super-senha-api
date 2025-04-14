package com.nebula_soft.super_senha.dto;

import com.nebula_soft.super_senha.Enum.WordStatusEnum;
import lombok.Data;

@Data
public class WordUpdateDTO {
    private String word;
    private Long difficultyLevelId;
    private Long gameRoomId;
    private WordStatusEnum status;
}
