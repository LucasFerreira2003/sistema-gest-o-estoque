package com.lucas.estoque.controller;

import com.lucas.estoque.dto.MovimentoEstoqueRequestDTO;
import com.lucas.estoque.dto.MovimentoEstoqueResponseDTO;
import com.lucas.estoque.service.MovimentoEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movimentos")
@Tag(
        name = "Movimentação de Estoque",
        description = "Entrada, saída e ajuste de estoque"
)
public class MovimentoEstoqueController {

    private final MovimentoEstoqueService movimentoEstoqueService;

    public MovimentoEstoqueController(MovimentoEstoqueService movimentoEstoqueService) {
        this.movimentoEstoqueService = movimentoEstoqueService;
    }

    @Operation(
            summary = "Registrar movimentação",
            description = "Realiza uma entrada, saída ou ajuste de estoque."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimentação registrada"),
            @ApiResponse(responseCode = "400", description = "Estoque insuficiente"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })

    @PostMapping
    public ResponseEntity<MovimentoEstoqueResponseDTO> criar(@Valid @RequestBody MovimentoEstoqueRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentoEstoqueService.registrar(dto));
    }
    @Operation(
            summary = "Listar movimentações por produto",
            description = "Retorna o histórico completo de movimentações de um produto."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimentações encontradas"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<MovimentoEstoqueResponseDTO>> listarPorProduto(@PathVariable UUID produtoId) {
        return ResponseEntity.ok(movimentoEstoqueService.listarPorProduto(produtoId));
    }

}
