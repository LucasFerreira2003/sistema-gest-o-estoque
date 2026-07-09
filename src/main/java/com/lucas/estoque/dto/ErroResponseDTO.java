package com.lucas.estoque.dto;

import java.util.Map;

public record ErroResponseDTO(
        int status,
        String erro,
        Map<String, String> campos
) {
}
