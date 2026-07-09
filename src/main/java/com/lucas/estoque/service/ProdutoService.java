package com.lucas.estoque.service;

import com.lucas.estoque.dto.ProdutoRequestDTO;
import com.lucas.estoque.dto.ProdutoResponseDTO;
import com.lucas.estoque.entity.Categoria;
import com.lucas.estoque.entity.Produto;
import com.lucas.estoque.exception.BusinessException;
import com.lucas.estoque.exception.DuplicateResourceException;
import com.lucas.estoque.exception.ResourceNotFoundException;
import com.lucas.estoque.repository.CategoriaRepository;
import com.lucas.estoque.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        if (produtoRepository.findBySku(dto.sku()).isPresent()) {
            throw new DuplicateResourceException("Já existe um produto com o SKU: " + dto.sku());
        }
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (dto.estoqueIdeal() <= dto.estoqueMinimo()) {
            throw new BusinessException("Estoque ideal deve ser maior que o estoque mínimo");
        }
        Produto produto = Produto.builder()
                .sku(dto.sku())
                .nome(dto.nome())
                .categoria(categoria)
                .estoqueMinimo(dto.estoqueMinimo())
                .estoqueIdeal(dto.estoqueIdeal())
                .precoUnitario(dto.precoUnitario())
                .build();
        Produto salvo = produtoRepository.save(produto);
        return toResponseDTO(salvo);

    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(UUID id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        return toResponseDTO(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public ProdutoResponseDTO atualizar(UUID id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        boolean skuMudou = !produto.getSku().equals(dto.sku());
        boolean skuJaExiste = produtoRepository.findBySku(dto.sku()).isPresent();

        if (skuMudou && skuJaExiste) {
            throw new DuplicateResourceException("Já existe um produto com o SKU: " + dto.sku());
        }
        if (dto.estoqueIdeal() <= dto.estoqueMinimo()) {
            throw new BusinessException("Estoque ideal deve ser maior que o estoque mínimo");
        }
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        produto.setNome(dto.nome());
        produto.setSku(dto.sku());
        produto.setCategoria(categoria);
        produto.setEstoqueMinimo(dto.estoqueMinimo());
        produto.setEstoqueIdeal(dto.estoqueIdeal());
        produto.setPrecoUnitario(dto.precoUnitario());
        Produto salvo = produtoRepository.save(produto);
        return toResponseDTO(salvo);
    }
    @Transactional
    public void deletar(UUID id){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        produtoRepository.deleteById(id);


    }


    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getSku(),
                produto.getNome(),
                produto.getEstoqueAtual(),
                produto.getEstoqueMinimo(),
                produto.getEstoqueIdeal(),
                produto.getPrecoUnitario(),
                produto.getCategoria().getNome()
        );
    }
}
