package com.lucas.estoque.repository;

import com.lucas.estoque.entity.MovimentoEstoque;
import com.lucas.estoque.entity.enums.TipoMovimento;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, UUID> {

    // Histórico de um produto
    List<MovimentoEstoque> findByProdutoIdOrderByDataHoraDesc(UUID produtoId);

    List<MovimentoEstoque> findByTipo(TipoMovimento tipo);

    // Filtrar por período
    List<MovimentoEstoque> findByDataHoraBetween(
            LocalDateTime inicio,
            LocalDateTime fim
    );
}