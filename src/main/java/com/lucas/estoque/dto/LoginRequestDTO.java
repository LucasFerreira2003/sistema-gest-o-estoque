package com.lucas.estoque.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "O email não pode ser vazio")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "A senha não pode ser vazia")
        String senha
) {
}
