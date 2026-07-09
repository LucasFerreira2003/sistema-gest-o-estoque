package com.lucas.estoque.service;

import com.lucas.estoque.dto.CategoriaRequestDTO;
import com.lucas.estoque.dto.CategoriaResponseDTO;
import com.lucas.estoque.entity.Categoria;
import com.lucas.estoque.entity.Produto;
import com.lucas.estoque.exception.BusinessException;
import com.lucas.estoque.exception.DuplicateResourceException;
import com.lucas.estoque.exception.ResourceNotFoundException;
import com.lucas.estoque.repository.CategoriaRepository;
import jakarta.persistence.OneToMany;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new DuplicateResourceException("Já existe uma categoria com o nome: " + dto.nome());
        }
        Categoria categoria = Categoria.builder()
                .nome(dto.nome())
                .build();
        Categoria salvo = categoriaRepository.save(categoria);

        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));
        return toResponseDTO(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodos() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public CategoriaResponseDTO atualizar(UUID id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));
        if (!categoria.getNome().equalsIgnoreCase(dto.nome()) &&
                categoriaRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new DuplicateResourceException("Já existe uma categoria com o nome: " + dto.nome());
        }
        categoria.setNome(dto.nome());
        Categoria salvo = categoriaRepository.save(categoria);
        return toResponseDTO(salvo);
    }

    @Transactional
    public void deletar(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));

        if (!categoria.getProdutos().isEmpty()) {
            throw new BusinessException("Não é possível excluir uma categoria que possui produtos vinculados");
        }

        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getCreatedAt()
        );
    }
}
