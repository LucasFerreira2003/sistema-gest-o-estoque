package com.lucas.estoque.service;

import com.lucas.estoque.dto.MovimentoEstoqueRequestDTO;
import com.lucas.estoque.dto.MovimentoEstoqueResponseDTO;
import com.lucas.estoque.entity.MovimentoEstoque;
import com.lucas.estoque.entity.Produto;
import com.lucas.estoque.exception.BusinessException;
import com.lucas.estoque.exception.ResourceNotFoundException;
import com.lucas.estoque.repository.MovimentoEstoqueRepository;
import com.lucas.estoque.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MovimentoEstoqueService {

    private final MovimentoEstoqueRepository movimentoRepository;
    private final ProdutoRepository produtoRepository;

    public MovimentoEstoqueService(MovimentoEstoqueRepository movimentoRepository, ProdutoRepository produtoRepository) {
        this.movimentoRepository = movimentoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public MovimentoEstoqueResponseDTO registrar(MovimentoEstoqueRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID"));


        switch (dto.tipo()) {
            case ENTRADA -> produto.setEstoqueAtual(
                    produto.getEstoqueAtual() + dto.quantidade()
            );
            case SAIDA -> {
                if (produto.getEstoqueAtual() < dto.quantidade()) {
                    throw new BusinessException("Estoque insuficiente para realizar a saída");
                }
                produto.setEstoqueAtual(produto.getEstoqueAtual() - dto.quantidade());
            }
            case AJUSTE -> produto.setEstoqueAtual(
                    produto.getEstoqueAtual() + dto.quantidade()
            );
        }
        MovimentoEstoque movimento = MovimentoEstoque.builder()
                .produto(produto)
                .tipo(dto.tipo())
                .quantidade(dto.quantidade())
                .motivo(dto.motivo())
                .build();
        produtoRepository.save(produto);
        MovimentoEstoque salvo = movimentoRepository.save(movimento);

        if (produto.getEstoqueAtual() <= produto.getEstoqueMinimo()){
            log.warn("⚠️ Produto {} atingiu o estoque mínimo. Reposição necessária.", produto.getNome());
        }
        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<MovimentoEstoqueResponseDTO>listarPorProduto(UUID produtoId){
        produtoRepository.findById(produtoId)
                .orElseThrow(()-> new ResourceNotFoundException("Produto não encontrado com ID: "+produtoId));
return movimentoRepository.findByProdutoIdOrderByDataHoraDesc(produtoId)
        .stream()
        .map(this::toResponseDTO)
        .toList();
    }
    private MovimentoEstoqueResponseDTO toResponseDTO(MovimentoEstoque movimento) {
        return new MovimentoEstoqueResponseDTO(
                movimento.getId(),
                movimento.getProduto().getId(),
                movimento.getProduto().getNome(),
                movimento.getTipo(),
                movimento.getQuantidade(),
                movimento.getMotivo(),
                movimento.getDataHora()
        );
    }
}
