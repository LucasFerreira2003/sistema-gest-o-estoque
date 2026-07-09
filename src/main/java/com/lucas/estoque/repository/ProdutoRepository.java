package com.lucas.estoque.repository;

import com.lucas.estoque.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID>{
    Optional<Produto> findBySku(String sku);

    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
