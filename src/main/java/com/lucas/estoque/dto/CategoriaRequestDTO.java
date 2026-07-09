package com.lucas.estoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(


        @NotBlank(message = "O nome não pode ser vazio")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome

) {
}
