CREATE TABLE categorias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE produtos
ADD COLUMN categoria_id UUID NOT NULL,
ADD CONSTRAINT fk_produto_categoria
FOREIGN KEY (categoria_id) REFERENCES categorias(id);