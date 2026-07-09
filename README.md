# Sistema de Gestão de Estoque com Reposição Automática

API REST desenvolvida em Java com Spring Boot para gerenciamento
de estoque com reposição automática.

## Tecnologias

- Java 17 · Spring Boot 3 · PostgreSQL · Docker · Flyway
- Spring Data JPA · Swagger/OpenAPI · JUnit 5 · Mockito

## Funcionalidades

- CRUD completo de Produtos e Categorias
- Registro de movimentações de estoque (ENTRADA/SAIDA/AJUSTE)
- Reposição automática: alerta quando produto atinge estoque mínimo
- Histórico auditável de movimentações
- Tratamento de exceções padronizado
- Documentação com Swagger

## Como rodar

### Pré-requisitos
- Docker instalado
- Java 17+

### 1. Sobe o banco
\`\`\`bash
docker compose up -d
\`\`\`

### 2. Configura as variáveis de ambiente
Copia `.env.example` para `.env` e preenche os valores

### 3. Sobe a aplicação
\`\`\`bash
./mvnw spring-boot:run
\`\`\`

### 4. Acessa o Swagger
http://localhost:8080/swagger-ui/index.html

## Endpoints

| Módulo | Método | Endpoint |
|---|---|---|
| Produto | POST | /produtos |
| Produto | GET | /produtos |
| Produto | GET | /produtos/{id} |
| Produto | PUT | /produtos/{id} |
| Produto | DELETE | /produtos/{id} |
| Categoria | POST | /categorias |
| Categoria | GET | /categorias |
| MovimentoEstoque | POST | /movimentos |
| MovimentoEstoque | GET | /movimentos/produto/{id} |

## Arquitetura

\`\`\`
Controller → Service → Repository → Entity
↓
DTOs + Exceções customizadas
\`\`\`