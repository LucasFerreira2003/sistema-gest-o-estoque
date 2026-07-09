CREATE TABLE movimento_estoque(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
produto_id UUID NOT NULL,
tipo VARCHAR(10) NOT NULL,
quantidade INTEGER NOT NULL,
motivo VARCHAR(255) NOT NULL,
data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_movimento_produto
    FOREIGN KEY (produto_id)
    REFERENCES produtos (id)
    ON DELETE RESTRICT
);
