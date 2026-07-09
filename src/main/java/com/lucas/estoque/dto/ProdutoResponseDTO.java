package com.lucas.estoque.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoResponseDTO(
        UUID id,
        String sku,
        String nome,
        Integer estoqueAtual,
        Integer estoqueMinimo,
        Integer estoqueIdeal,
        BigDecimal precoUnitario,
        String categoriaNome

) {
}
