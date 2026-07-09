package com.lucas.estoque.dto;

import com.lucas.estoque.entity.enums.TipoMovimento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MovimentoEstoqueRequestDTO(

        @Schema(
                description = "Identificador único do produto",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @NotNull(message = "O produto é obrigatório")
        @NotNull(message = "O produto é obrigatório")
        UUID produtoId,

        @Schema(
                description = "Tipo da movimentação",
                example = "ENTRADA",
                allowableValues = {"ENTRADA", "SAIDA", "AJUSTE"}
        )
        @NotNull(message = "O tipo da movimentação é obrigatório")
        TipoMovimento tipo,

        @Schema(
                description = "Quantidade movimentada",
                example = "15",
                minimum = "1"
        )
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantidade,
        @Schema(
                description = "Motivo da movimentação",
                example = "Reposição de estoque após compra do fornecedor"
        )
        @NotBlank(message = "O motivo é obrigatório")
        @Size(max = 255, message = "O motivo deve ter no máximo 255 caracteres")
        String motivo
) {
}
