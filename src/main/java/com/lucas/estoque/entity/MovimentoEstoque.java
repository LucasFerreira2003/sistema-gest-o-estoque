package com.lucas.estoque.entity;

import com.lucas.estoque.entity.enums.TipoMovimento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "movimento_estoque")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MovimentoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimento tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private String motivo;

    @CreationTimestamp
    @Column(name = "data_hora", updatable = false)
    private LocalDateTime dataHora;


}
