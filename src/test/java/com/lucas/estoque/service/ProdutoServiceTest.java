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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    ProdutoRepository produtoRepository;
    @Mock
    CategoriaRepository categoriaRepository;
    @InjectMocks
    ProdutoService produtoService;

    @Test
    void deveLancarResourceNotFoundQuandoProdutoNaoExistir() {
        UUID id = UUID.randomUUID();

        when(produtoRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> produtoService.buscarPorId(id)
        );

        verify(produtoRepository).findById(id);
    }


@Test
void deveLancarExcecaoQuandoSkuJaDuplicado() {
    ProdutoRequestDTO dto = new ProdutoRequestDTO(
            "SKU-002",
            "Martelo",
            UUID.randomUUID(),
            5,
            20,
            new BigDecimal("15.00")
    );
//  Simula: SKU não existe
    when(produtoRepository.findBySku("SKU-002"))
            .thenReturn(Optional.of(new Produto()));

    assertThrows(
            DuplicateResourceException.class,
            () -> produtoService.criar(dto)
    );

    verify(produtoRepository).findBySku("SKU-002");
}

@Test
void deveLancarExcecaoQuandoCategoriaInexistente() {
    // Arrange
    UUID categoriaId = UUID.randomUUID();

    ProdutoRequestDTO dto = new ProdutoRequestDTO(
            "SKU-003",
            "Chave de Fenda",
            categoriaId,
            5,
            20,
            new BigDecimal("8.00")
    );

    // SKU não existe — passa essa validação
    when(produtoRepository.findBySku("SKU-003"))
            .thenReturn(Optional.empty());

    // Categoria não existe — lança exceção aqui
    when(categoriaRepository.findById(categoriaId))
            .thenReturn(Optional.empty());

    // Act + Assert
    assertThrows(
            ResourceNotFoundException.class,
            () -> produtoService.criar(dto)
    );
}

    @Test
    void deveLancarBusinessExceptionQuandoEstoqueIdealMenorOuIgualAoMinimo() {
        UUID categoriaId = UUID.randomUUID();
        Categoria categoria = new Categoria();

        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "SKU-004",
                "Martelo",
                categoriaId,
                10, // estoqueMinimo
                5,  // estoqueIdeal — inválido, menor que mínimo
                new BigDecimal("25.00")
        );

        when(produtoRepository.findBySku("SKU-004"))
                .thenReturn(Optional.empty());

        when(categoriaRepository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        assertThrows(
                BusinessException.class,
                () -> produtoService.criar(dto)
        );

        verify(produtoRepository, never()).save(any());
    }


}
