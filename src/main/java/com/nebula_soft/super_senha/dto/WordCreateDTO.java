package com.nebula_soft.super_senha.dto;

import com.nebula_soft.super_senha.Enum.DifficultyEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordCreateDTO {

    @NotBlank(message = "A palavra não pode estar em branco.")
    private String value;

    @NotNull(message = "A dificuldade deve ser informada.")
    private DifficultyEnum difficulty;
}