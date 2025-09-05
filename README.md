# Backend Monitoramento de Pacientes

## Visão Geral do Projeto

O InfraMed é um projeto inovador que nasceu para solucionar um desafio crítico em hospitais: a sobrecarga da equipe e as longas esperas dos pacientes. Este backend é a espinha dorsal do sistema, atuando como uma infraestrutura inteligente que otimiza a gestão e o tempo. Através de uma API RESTful robusta, ele gerencia eficientemente dados cruciais de quartos, pacientes, atendimentos e funcionários de saúde, transformando o ambiente hospitalar em um centro de atendimento ágil e seguro. Essa base tecnológica capacita os profissionais a recuperarem seu tempo e oferece aos pacientes um cuidado otimizado e tranquilo. Em essência, o InfraMed é a infraestrutura de cuidado que devolve o tempo aos profissionais e a tranquilidade aos pacientes.

## Tecnologias Utilizadas
- **Java 24:** Linguagem de programação robusta e amplamente utilizada para aplicações corporativas.
- **Spring Boot 3.4.5:** Framework para desenvolvimento rápido de aplicações Java, facilitando a criação de APIs RESTful e microsserviços.
- **Spring Data JPA:** Simplifica a implementação de camadas de acesso a dados, utilizando o padrão Repository e abstraindo a complexidade do ORM.
- **Hibernate:** Implementação de ORM (Object-Relational Mapping) que facilita a persistência de objetos Java em bancos de dados relacionais.
- **Maven:** Ferramenta de automação de build e gerenciamento de dependências.
- **Lombok:** Biblioteca que reduz a verbosidade do código Java, gerando automaticamente getters, setters, construtores, etc.
- **MySQL Database:** Banco de dados relacional robusto e de código aberto para armazenamento de dados.
- **Validação (Jakarta Validation / Hibernate Validator):** Garante a integridade dos dados de entrada através de validações declarativas.

## Arquitetura
O projeto adota uma arquitetura em camadas bem definida, visando a **separação clara de responsabilidades**, **manutenibilidade** e **escalabilidade**. A estrutura é organizada por **domínios de negócio** (ex: `quarto`, `paciente`, `atendimento`, `funcionario`), onde cada domínio encapsula suas próprias camadas de Controller, Service, Repository e Model. Essa abordagem reforça a **modularidade** e o alinhamento com os princípios de **Domain-Driven Design (DDD)**, facilitando o desenvolvimento, a compreensão e a evolução do sistema.

As principais camadas são:
- **Camada de Apresentação (Controller):** Responsável por receber e validar as requisições HTTP, delegando a lógica de negócio para a camada de serviço e formatando as respostas. Utiliza `@RestController` e `@RequestMapping` para expor a API RESTful.
- **Camada de Serviço (Service):** Contém a **lógica de negócio central** da aplicação. Orquestra as operações, aplica regras de validação complexas e interage com a camada de repositório. Promove a **reutilização de código** e a **consistência das regras de negócio**.
- **Camada de Repositório (Repository):** Abstrai os detalhes de persistência de dados, interagindo diretamente com o banco de dados. Utiliza Spring Data JPA para simplificar as operações de CRUD e consultas personalizadas, garantindo **eficiência no acesso a dados**.
- **Camada de Modelo (Model/Entity):** Define a estrutura dos dados e o mapeamento para o banco de dados, representando as entidades do domínio.
- **DTOs (Data Transfer Objects):** Utilizados para transferir dados de forma otimizada e segura entre as camadas, especialmente entre a camada de apresentação e a de serviço, controlando quais dados são expostos ou recebidos pela API.

## Conceitos e Boas Práticas Implementadas

Este projeto foi desenvolvido com base em sólidos princípios de engenharia de software, garantindo um código de alta qualidade, manutenível e escalável:

-   **RESTful API:** A API foi projetada seguindo os princípios REST, utilizando verbos HTTP (GET, POST, PUT, DELETE) e URLs amigáveis para representar recursos. Isso garante uma comunicação **padronizada e eficiente** com os clientes.
-   **DTOs (Data Transfer Objects):** Implementação estratégica de DTOs para **desacoplar os modelos de domínio** das requisições e respostas da API, proporcionando maior **flexibilidade**, **segurança** (evitando exposição de dados sensíveis) e **otimização** na transferência de dados.
-   **Tratamento de Exceções Centralizado:** Gerenciamento robusto e centralizado de exceções para fornecer **respostas de erro consistentes e informativas** aos clientes da API, utilizando `@ExceptionHandler` e `ResponseEntity`. Isso melhora a **experiência do desenvolvedor** que consome a API.
-   **Validação de Dados:** Utilização de anotações de validação (`@Valid`, `@NotNull`, `@Size`, etc.) para garantir a **integridade e a consistência** dos dados de entrada desde a camada de apresentação, prevenindo erros e vulnerabilidades.
-   **Injeção de Dependência (DI):** Amplo uso do mecanismo de Injeção de Dependência do Spring (`@RequiredArgsConstructor` e `final` fields) para gerenciar as dependências entre os componentes. Isso promove **baixo acoplamento**, **alta coesão**, e facilita a **testabilidade** e a **manutenção** do código.
-   **Padrão Repository:** Abstração da camada de persistência de dados, facilitando a **troca de tecnologias de banco de dados** e a **realização de testes unitários e de integração** sem depender de uma infraestrutura real.
-   **Padrão Service:** Encapsulamento da lógica de negócio em classes de serviço, promovendo a **reutilização**, a **organização** e a **separação de preocupações**, tornando o código mais legível e fácil de dar manutenção.
-   **Domain-Driven Design (DDD):** Foco na modelagem do domínio de negócio, com a criação de entidades, objetos de valor e agregados que refletem a complexidade do problema a ser resolvido. Isso garante que o software esteja **alinhado com as regras de negócio** e seja **compreensível** para especialistas do domínio.
-   **SOLID:** Aplicação rigorosa dos cinco princípios de design de software (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion) para criar um código mais **compreensível, flexível, extensível e manutenível**, reduzindo a complexidade e o custo de futuras modificações.
-   **DRY (Don't Repeat Yourself):** Princípio fundamental aplicado para **evitar a duplicação de código**, promovendo a **reutilização** e a **consistência** em toda a aplicação, o que resulta em menos bugs e maior agilidade no desenvolvimento.
-   **Clean Architecture:** Organização do código em camadas concêntricas, onde as dependências fluem de fora para dentro. Isso garante que a **lógica de negócio seja independente de frameworks e bancos de dados**, facilitando a **testabilidade**, a **portabilidade** e a **manutenção** a longo prazo.
-   **Clean Code:** Prática constante de escrever código **legível, compreensível e fácil de manter**, seguindo convenções de nomenclatura claras, formatação consistente e refatoração contínua. Isso melhora a **colaboração** e a **qualidade geral** do projeto.

## Diferenciais Técnicos

Este projeto se destaca por:

-   **Modularidade e Escalabilidade:** A arquitetura baseada em domínios e a adesão a princípios como DDD e Clean Architecture garantem um sistema altamente modular, fácil de estender e escalar para novas funcionalidades ou demandas.
-   **Qualidade de Código:** A aplicação de SOLID, DRY e Clean Code resulta em um código limpo, testável e de fácil manutenção, o que é crucial para projetos de longo prazo.
-   **Robustez e Confiabilidade:** O tratamento centralizado de exceções e a validação rigorosa de dados contribuem para uma API mais robusta e menos suscetível a erros.
-   **Desenvolvimento Ágil e Eficiente:** O uso de Spring Boot e Lombok acelera o desenvolvimento, permitindo a entrega de valor de forma mais rápida e eficiente.
-   **Foco no Domínio de Negócio:** A forte influência do DDD assegura que o software reflita com precisão a complexidade e as regras do negócio hospitalar, tornando-o mais eficaz na resolução dos problemas reais.

## Endpoints da API

### Endpoints de Quarto
Base URL: `/api/quarto`

| Método HTTP | URL | Descrição | Corpo da Requisição | Respostas |
|---|---|---|---|---|
| `GET` | `/` | Lista todos os quartos. | N/A | `200 OK` (Lista de Quartos) |
| `GET` | `/{id}` | Busca um quarto por ID. | N/A | `200 OK` (Quarto), `404 NOT FOUND` |
| `POST` | `/` | Insere um novo quarto. | `Quarto` (JSON) | `201 CREATED` (Quarto criado) |
| `PUT` | `/{id}` | Altera um quarto existente. | `Quarto` (JSON) | `200 OK` (Quarto alterado), `404 NOT FOUND` |
| `DELETE` | `/{id}` | Apaga um quarto. | N/A | `204 NO CONTENT`, `404 NOT FOUND` |
| `POST` | `/inserir-varios` | Insere múltiplos quartos. | `List<Quarto>` (JSON) | `200 OK` (Lista de Quartos criados) |
| `PUT` | `/{quartoId}/alocar-paciente/{pacienteId}` | Aloca um paciente em um quarto. | N/A | `200 OK` (Quarto atualizado), `400 BAD REQUEST` |
| `PUT` | `/{quartoId}/remover-paciente/{pacienteId}` | Remove um paciente de um quarto. | N/A | `200 OK` (Quarto atualizado), `400 BAD REQUEST` |

### Endpoints de Paciente
Base URL: `/api/pacientes`

| Método HTTP | URL | Descrição | Corpo da Requisição | Respostas |
|---|---|---|---|---|
| `POST` | `/` | Cria um novo paciente. | `PacienteRequestDTO` (JSON) | `201 CREATED` (Paciente criado), `400 BAD REQUEST` (CPF duplicado) |
| `GET` | `/` | Lista todos os pacientes. | N/A | `200 OK` (Lista de Pacientes) |
| `GET` | `/{id}` | Busca um paciente por ID. | N/A | `200 OK` (Paciente), `404 NOT FOUND` |
| `GET` | `/{cpf}` | Busca um paciente por CPF. | N/A | `200 OK` (Paciente), `404 NOT FOUND` |
| `GET` | `?nome={nome}` | Busca pacientes por nome (parcial). | N/A | `200 OK` (Lista de Pacientes) |
| `PUT` | `/{id}` | Altera um paciente existente. | `PacienteRequestDTO` (JSON) | `200 OK` (Paciente alterado), `404 NOT FOUND` |
| `DELETE` | `/{id}` | Deleta um paciente. | N/A | `204 NO CONTENT`, `404 NOT FOUND` |

*Nota sobre `GET /api/pacientes/{cpf}` e `GET /api/pacientes/{id}`:* Embora ambos usem um path variable, o Spring é capaz de distinguir entre eles com base no tipo de dado esperado (Long para ID, String para CPF). No entanto, para maior clareza e evitar ambiguidades em outros contextos, uma abordagem comum seria usar `/api/pacientes/cpf/{cpf}` ou `/api/pacientes/id/{id}`.

### Endpoints de Atendimento
Base URL: `/api/atendimento`

| Método HTTP | URL | Descrição | Corpo da Requisição | Respostas |
|---|---|---|---|---|
| `POST` | `/` | Cria um novo atendimento. | `Atendimento` (JSON) | `201 CREATED` (Atendimento criado), `400 BAD REQUEST`, `404 NOT FOUND`, `409 CONFLICT` |
| `GET` | `/{id}` | Busca um atendimento por ID. | N/A | `200 OK` (Atendimento), `404 NOT FOUND` |
| `GET` | `/` | Busca todos os atendimentos. | N/A | `200 OK` (Lista de Atendimentos) |
| `PUT` | `/{id}` | Altera um atendimento existente. | `Atendimento` (JSON) | `200 OK` (Atendimento alterado), `400 BAD REQUEST`, `404 NOT FOUND` |
| `DELETE` | `/{id}` | Deleta um atendimento. | N/A | `204 NO CONTENT`, `404 NOT FOUND` |

### Endpoints de Funcionário
Base URL: `/api/funcionarios`

| Método HTTP | URL | Descrição | Corpo da Requisição | Respostas |
|---|---|---|---|---|
| `POST` | `/` | Cria um novo funcionário de saúde. | `FuncionarioSaudeRequestDTO` (JSON) | `201 CREATED` (Funcionário criado) |
| `GET` | `/` | Lista todos os funcionários de saúde. | N/A | `200 OK` (Lista de Funcionários) |
| `GET` | `/{id}` | Busca um funcionário de saúde por ID. | N/A | `200 OK` (Funcionário), `404 NOT FOUND` |
| `GET` | `/{cpf}` | Busca um funcionário de saúde por CPF. | N/A | `200 OK` (Funcionário), `404 NOT FOUND` |
| `GET` | `?nome={nome}` | Busca funcionários de saúde por nome (parcial). | N/A | `200 OK` (Lista de Funcionários) |
| `PUT` | `/{id}` | Altera um funcionário de saúde existente. | `FuncionarioSaudeRequestDTO` (JSON) | `200 OK` (Funcionário alterado), `404 NOT FOUND` |
| `DELETE` | `/{id}` | Deleta um funcionário de saúde. | N/A | `204 NO CONTENT`, `404 NOT FOUND` |

*Nota sobre `GET /api/funcionarios/{cpf}` e `GET /api/funcionarios/{id}`:* Similar aos endpoints de Paciente, o Spring é capaz de distinguir. Para maior clareza, considere usar `/api/funcionarios/cpf/{cpf}` ou `/api/funcionarios/id/{id}`.

## Como Rodar o Projeto

1.  **Pré-requisitos:**
    *   Java Development Kit (JDK) 24 ou superior
    *   Maven 3.x

2.  **Clonar o Repositório:**
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    cd BackendMonitoramentoPacientes
    ```

3.  **Compilar e Rodar:**
    Você pode compilar e rodar o projeto usando Maven:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    O aplicativo estará disponível em `http://localhost:8080`.

## Contribuição
Sinta-se à vontade para abrir issues ou pull requests.

## Licença
Este projeto está licenciado sob a Licença MIT.
