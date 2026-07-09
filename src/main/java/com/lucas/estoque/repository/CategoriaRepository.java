package com.lucas.estoque.repository;

import com.lucas.estoque.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    boolean existsByNomeIgnoreCase(String nome);

    List<Categoria> findByNomeContainingIgnoreCase(String nome);
}
