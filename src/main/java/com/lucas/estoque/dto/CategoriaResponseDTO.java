package com.lucas.estoque.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoriaResponseDTO(
         UUID id,
         String nome,
         LocalDateTime createdAt
) {
}
