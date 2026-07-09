package com.lucas.estoque.dto;

import com.lucas.estoque.entity.enums.TipoMovimento;

import java.time.LocalDateTime;
import java.util.UUID;

public record MovimentoEstoqueResponseDTO(
        UUID id,

        UUID produtoId,

        String produtoNome,

        TipoMovimento tipo,

        Integer quantidade,

        String motivo,

        LocalDateTime dataHora
) {
}
