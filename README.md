# Sistema de Gestão de Projetos e Tarefas

## Visão Geral

Sistema de gestão de projetos e tarefas desenvolvido como solução completa para o desafio técnico do Canal SIS Innov & Tech. A aplicação implementa todos os requisitos obrigatórios e diferenciais solicitados, fornecendo uma API RESTful robusta para gerenciamento de projetos e suas respectivas tarefas, com controle de status, prioridades, prazos, validações, testes automatizados e documentação completa.

## Arquitetura

### Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **PostgreSQL 16**
- **MapStruct 1.5.5** (Diferencial)
- **Flyway** (Diferencial)
- **Docker & Docker Compose** (Diferencial)
- **Swagger/OpenAPI** (Diferencial)
- **JWT Authentication** (Diferencial)
- **RestAssured** (Diferencial)

### Estrutura do Projeto

```
src/main/java/dev/matheuslf/desafio/inscritos/
├── controller/          # Controllers REST
├── dto/                 # Data Transfer Objects
├── exception/           # Tratamento de exceções
├── mapper/              # Mapeadores MapStruct
├── model/               # Entidades JPA
│   └── enums/          # Enums (Status, Priority)
├── repository/          # Repositórios JPA
├── service/            # Lógica de negócio
└── utils/              # Utilitários
```

## Modelo de Dados

### Entidade Project
- `id`: Long (Chave primária)
- `name`: String (3-100 caracteres, obrigatório)
- `description`: String (opcional)
- `startDate`: Date (opcional)
- `endDate`: Date (opcional)

### Entidade Task
- `id`: Long (Chave primária)
- `title`: String (5-150 caracteres, obrigatório)
- `description`: String (opcional)
- `status`: Enum (TODO, DOING, DONE)
- `priority`: Enum (LOW, MEDIUM, HIGH)
- `dueDate`: Date (opcional)
- `project`: Project (Chave estrangeira, obrigatório)

## API Endpoints

### Projetos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/projects` | Criar novo projeto |
| GET | `/projects` | Listar todos os projetos |

### Tarefas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/tasks` | Criar nova tarefa |
| GET | `/tasks` | Listar tarefas com filtros opcionais |
| PUT | `/tasks/{id}/status` | Atualizar status da tarefa |
| DELETE | `/tasks/{id}` | Excluir tarefa |

### Filtros Disponíveis

- `status`: TODO, DOING, DONE
- `priority`: LOW, MEDIUM, HIGH
- `projectId`: ID do projeto

## Instalação e Execução

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Docker e Docker Compose

### Configuração do Ambiente

1. **Clone o repositório**
```bash
git clone <url-do-repositorio>
cd dev.matheuslf.desafio.inscritos
```

2. **Inicie o banco de dados**
```bash
docker-compose up -d
```

3. **Execute a aplicação**
```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## Configuração do Banco de Dados

### Docker Compose

O arquivo `docker-compose.yml` configura um PostgreSQL com:
- **Porta**: 5433
- **Database**: desafio
- **Usuário**: admin
- **Senha**: 12345

### Migrações

O projeto utiliza Flyway para controle de versão do banco de dados. As migrações estão localizadas em `src/main/resources/db/migration/` e são executadas automaticamente na inicialização da aplicação.

## Exemplos de Uso

### Criar Projeto

```bash
curl -X POST http://localhost:8080/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Projeto Exemplo",
    "description": "Descrição do projeto",
    "startDate": "2024-01-01",
    "endDate": "2024-12-31"
  }'
```

### Criar Tarefa

```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Implementar autenticação",
    "description": "Criar sistema de login",
    "priority": "HIGH",
    "dueDate": "2024-02-15",
    "projectId": 1
  }'
```

### Listar Tarefas com Filtros

```bash
curl "http://localhost:8080/tasks?status=TODO&priority=HIGH&projectId=1"
```

### Atualizar Status da Tarefa

```bash
curl -X PUT http://localhost:8080/tasks/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "DOING"
  }'
```

## Validações

### Projeto
- `name`: Obrigatório, entre 3 e 100 caracteres
- `description`: Opcional
- `startDate`: Opcional, formato de data
- `endDate`: Opcional, formato de data

### Tarefa
- `title`: Obrigatório, entre 5 e 150 caracteres
- `description`: Opcional
- `status`: Enum (TODO, DOING, DONE)
- `priority`: Enum (LOW, MEDIUM, HIGH)
- `dueDate`: Opcional, formato de data
- `projectId`: Obrigatório, deve referenciar projeto existente

## Tratamento de Erros

A aplicação implementa tratamento centralizado de exceções através de `@ControllerAdvice`, retornando respostas padronizadas para:

- **400 Bad Request**: Dados de entrada inválidos
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erros internos do servidor

## Mapeamento de DTOs

O projeto utiliza MapStruct para mapeamento automático entre entidades JPA e DTOs, garantindo:
- Performance otimizada
- Código limpo e maintível
- Redução de boilerplate

## Testes

A aplicação implementa cobertura completa de testes automatizados conforme requisitos do desafio:

### Testes Unitários
- **Services**: Testados com mocks para isolamento
- **Mappers**: Validação de conversões MapStruct
- **Validators**: Testes de validação de dados

### Testes de Integração
- **Controllers**: Testes com MockMvc
- **Repositories**: Testes com Testcontainers
- **API Endpoints**: Testes com RestAssured

### Executar Testes

```bash
# Todos os testes
./mvnw test

# Apenas testes unitários
./mvnw test -Dtest="*UnitTest"

# Apenas testes de integração
./mvnw test -Dtest="*IntegrationTest"

# Relatório de cobertura
./mvnw test jacoco:report
```

### Cobertura de Testes
- **Cobertura mínima**: 80%
- **Relatório**: `target/site/jacoco/index.html`

## Documentação da API

### Swagger/OpenAPI

A aplicação inclui documentação automática da API através do Swagger/OpenAPI:

- **URL**: `http://localhost:8080/swagger-ui.html`
- **JSON**: `http://localhost:8080/v3/api-docs`
- **Especificação**: OpenAPI 3.0

### Autenticação

A aplicação implementa autenticação JWT:

```bash
# Obter token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin"}'

# Usar token nas requisições
curl -H "Authorization: Bearer <token>" http://localhost:8080/projects
```

## Configurações

### application.properties

```properties
# Banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5433/desafio
spring.datasource.username=admin
spring.datasource.password=12345

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# JWT
jwt.secret=mySecretKey
jwt.expiration=86400000
```

## Docker

### Comandos Úteis

```bash
# Iniciar serviços
docker-compose up -d

# Parar serviços
docker-compose down

# Remover volumes (apaga dados)
docker-compose down -v
```

## Requisitos Implementados

### ✅ Requisitos Obrigatórios
- **Java 17+** e **Spring Boot 3+** ✅
- **Spring Data JPA** ✅
- **Banco Relacional (PostgreSQL)** ✅
- **Bean Validation** ✅
- **Testes Automatizados** ✅
  - Unitários (Services mockados) ✅
  - Integração (Controllers com MockMvc) ✅
- **Tratamento de erros com @ControllerAdvice** ✅
- **Uso de DTOs** ✅
- **README explicando como rodar o projeto** ✅

### 🏅 Diferenciais Implementados
- **Documentação Swagger/OpenAPI** ✅
- **Autenticação JWT** ✅
- **Configuração Docker/docker-compose** ✅
- **Uso de MapStruct** ✅
- **Testes de API com RestAssured** ✅

## Licença

Este projeto foi desenvolvido exclusivamente para o processo seletivo SIS Innov & Tech e não deve ser utilizado para fins comerciais.