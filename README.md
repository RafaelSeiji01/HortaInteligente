# 🌿 Sistema de Monitoramento de Canteiros — Horta Inteligente

API REST desenvolvida em **Java** com o ecossistema **Spring Boot**, orientada a boas práticas de engenharia de software, separação estrita de responsabilidades por camadas e persistência relacional com **Spring Data JPA** e banco em memória **H2**.

O sistema registra canteiros de cultivo, calcula dinamicamente a área total de plantio e avalia a necessidade imediata de irrigação de acordo com o nível de umidade do solo e o tipo de cultura vegetal.

---

## 📁 Estrutura de Diretórios

A estrutura do código-fonte segue a convenção padrão do ecossistema Spring:

```text
src/main/java/com/fiap/hortainteligente/
├── config/
│   └── CorsConfig.java
├── controller/
│   └── CanteiroController.java
├── domain/
│   ├── Canteiro.java
│   └── TipoCultura.java
├── dto/
│   ├── CanteiroRequestDTO.java
│   └── CanteiroResponseDTO.java
├── mapper/
│   └── CanteiroMapper.java
├── repository/
│   └── CanteiroRepository.java
├── service/
│   └── CanteiroService.java
└── HortaInteligenteApplication.java

src/main/resources/
├── application.properties
└── static/
    └── index.html
```

---

## 🏛️ Detalhamento de Cada Camada

### 1. `domain`
* **Propósito:** Representa o núcleo do negócio e os dados estruturados do sistema.
* **Conteúdo:**
  * **Entidades JPA (`Canteiro.java`):** Classes anotadas com `@Entity` e `@Table`. Mapeiam fielmente as tabelas e colunas da base relacional, utilizando identificadores únicos (`@Id`, `@GeneratedValue`) e anotações do Lombok (`@Getter`, `@Setter`) para redução de código repetitivo.
  * **Enumerações (`TipoCultura.java`):** Modelam constantes do domínio (`HORTALICA`, `TUBERCULO`, `GRAO`), evitando valores arbitrários ou inconsistentes no banco.
* **Responsabilidade:** Garantir que as entidades existam de maneira independente de protocolos de transporte (HTTP) ou interfaces gráficas.

---

### 2. `repository`
* **Propósito:** Camada de abstração e acesso a dados persistentes.
* **Conteúdo:** Interfaces Java que estendem `JpaRepository<Canteiro, Long>`, decoradas com `@Repository`.
* **Responsabilidade:** Delega ao Spring Data JPA a geração em tempo de execução de operações CRUD completas (`save()`, `findById()`, `findAll()`, `deleteById()`), sem a necessidade de escrever queries SQL manuais ou gerenciar conexões JDBC diretamente.

---

### 3. `dto` (Data Transfer Object)
* **Propósito:** Objetos imutáveis destinados exclusivamente ao transporte de dados entre a aplicação e clientes externos (navegador, mobile, APIs terceiras).
* **Conteúdo:**
  * **`CanteiroRequestDTO` (`record`):** Delimita os campos aceitos no cadastro e aplica regras declarativas do Bean Validation (`@NotBlank`, `@Positive`, `@Min`, `@Max`, `@NotNull`).
  * **`CanteiroResponseDTO` (`record`):** Estrutura o JSON devolvido pela API, contendo tanto dados salvos no banco quanto métricas calculadas em tempo real (`areaTotal`, `precisaIrrigar`).
* **Responsabilidade:** Proteger as entidades contra exposição ou manipulação indevida (*Over-Posting/Mass Assignment*) e manter o contrato da API desacoplado do modelo do banco de dados.

---

### 4. `mapper`
* **Propósito:** Utilitário de conversão e transformação entre modelos.
* **Conteúdo:** Métodos utilitários estáticos (como `CanteiroMapper.java`).
* **Responsabilidade:**
  * Converter `CanteiroRequestDTO` em uma entidade `Canteiro` para persistência.
  * Converter a entidade `Canteiro` juntamente com os cálculos do serviço em um `CanteiroResponseDTO`.
  * Evitar a duplicação de lógicas de preenchimento de campos manuais nas outras camadas.

---

### 5. `service`
* **Propósito:** Centralização das regras de negócio e orquestração do fluxo de dados.
* **Conteúdo:** Classes de serviço decoradas com `@Service` (como `CanteiroService.java`).
* **Responsabilidade:**
  * Realizar cálculos de engenharia agronômica, como a área útil ($\text{largura} \times \text{comprimento}$).
  * Processar regras condicionais de irrigação baseadas no tipo de cultura e umidade do solo:
    * `HORTALICA`: irrigar quando umidade $< 60\%$
    * `TUBERCULO`: irrigar quando umidade $< 40\%$
    * `GRAO`: irrigar quando umidade $< 50\%$
  * Coordenar chamadas aos repositórios e mappers de maneira atômica e segura.

---

### 6. `controller`
* **Propósito:** Camada de exposição e ponto de entrada das requisições HTTP REST.
* **Conteúdo:** Classes anotadas com `@RestController` e `@RequestMapping` (como `CanteiroController.java`).
* **Responsabilidade:**
  * Mapear rotas e métodos HTTP (`POST /canteiros`, `GET /canteiros`, `GET /canteiros/{id}`).
  * Disparar o mecanismo de validação por meio da anotação `@Valid`.
  * Retornar respostas semânticas com códigos de status HTTP padronizados (`201 Created`, `200 OK`, `400 Bad Request`).

---

### 7. `config`
* **Propósito:** Parametrizações técnicas e infraestrutura do Spring.
* **Conteúdo:** Classes anotadas com `@Configuration`.
* **Responsabilidade:** Definir políticas globais de CORS (*Cross-Origin Resource Sharing*), autenticação/segurança com Spring Security ou documentação Swagger/OpenAPI.

---

### 8. `src/main/resources`
* **`application.properties`:** Centraliza parâmetros de execução, definindo o banco em memória H2 (`jdbc:h2:mem:hortadb`), credenciais de acesso, configuração de geração automática de schema (`hibernate.ddl-auto=update`) e console visual do H2.
* **`static/`:** Pasta raiz servida pelo servidor embutido do Spring (Tomcat), contendo os arquivos de interface de usuário (HTML/CSS/JavaScript como `index.html`).

---

## 🔄 Fluxo de Uma Requisição (POST /canteiros)

```text
[Frontend / Cliente HTTP]
         │
         │  1. JSON via POST
         ▼
[CanteiroController]  ───▶ 2. Valida payload com @Valid (Bean Validation)
         │
         │  3. CanteiroRequestDTO
         ▼
  [CanteiroService]   ───▶ 4. Aciona CanteiroMapper para gerar Entidade
         │
         │  5. Entidade Canteiro
         ▼
[CanteiroRepository]  ───▶ 6. Executa INSERT no Banco de Dados (H2)
         │
         │  7. Entidade salva com ID gerado
         ▼
  [CanteiroService]   ───▶ 8. Calcula área e avalia status de irrigação
         │
         │  9. Aciona CanteiroMapper para montar CanteiroResponseDTO
         ▼
[CanteiroController]  ───▶ 10. Devolve HTTP 201 Created com JSON completo
         │
         ▼
[Frontend / Cliente HTTP]
```
