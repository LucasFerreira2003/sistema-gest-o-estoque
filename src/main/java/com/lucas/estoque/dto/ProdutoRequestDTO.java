package com.lucas.estoque.dto;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoRequestDTO(

        @NotBlank(message = "O SKU não pode ser vazio")
        @Size(max = 50, message = "O SKU deve ter no máximo 50 caracteres")
        String sku,

        @NotBlank(message = "O nome não pode ser vazio")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,


        @NotNull(message = "O SKU não pode ser vazio")
        UUID categoriaId,

        @NotNull(message = "O estoque mínimo é obrigatório")
        @PositiveOrZero(message = "O estoque mínimo não pode ser negativo")
        Integer estoqueMinimo,

        @NotNull(message = "O estoque ideal é obrigatório")
        @PositiveOrZero(message = "O estoque ideal não pode ser negativo")
        Integer estoqueIdeal,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.00", inclusive = false, message = "O preço deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal precoUnitario

) {
}