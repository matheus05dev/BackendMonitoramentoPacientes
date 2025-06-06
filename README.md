# Documentação da API Springwalker

## Visão Geral
Esta API fornece endpoints para gerenciamento de pacientes e funcionários de saúde de uma clínica de fisioterapia.

## Base URL
```
http://localhost:8080
```

## Endpoints

### Pacientes

#### Listar Todos os Pacientes
```http
GET /paciente
```
Retorna a lista de todos os pacientes cadastrados.

#### Buscar Paciente por Nome
```http
GET /paciente/buscar?nome={nome}
```
Parâmetros:
- `nome`: Nome do paciente (parcial ou completo)

#### Cadastrar Novo Paciente
```http
POST /paciente/novo
```
Formulário para cadastro de novo paciente.

#### Salvar Paciente
```http
POST /paciente/salvar
```
Salva um novo paciente no sistema.

Dados necessários:
- Nome
- Email (único)
- Telefones (múltiplos)

#### Buscar Paciente por ID
```http
GET /paciente/buscar/{id}
```
Parâmetros:
- `id`: ID do paciente

#### Atualizar Paciente
```http
PATCH /paciente/alterar/{id}
```
Atualiza os dados de um paciente existente.

#### Excluir Paciente
```http
DELETE /paciente/excluir/{id}
```
Remove um paciente do sistema.

#### Gerenciar Telefones
```http
POST /paciente/addTelefone
DELETE /paciente/removeTelefone?removeDynamicRow={index}
```
Endpoints para adicionar e remover telefones de um paciente.

### Funcionários de Saúde

#### Listar Todos os Funcionários
```http
GET /funcionario-saude
```
Retorna a lista de todos os funcionários de saúde cadastrados.

#### Formulário de Inserção
```http
GET /funcionario-saude/form-inserir
```
Formulário para cadastro de novo funcionário.

#### Salvar Funcionário
```http
POST /funcionario-saude/salvar
```
Salva um novo funcionário no sistema.

Dados necessários:
- Nome
- Email
- Telefones (múltiplos)

#### Buscar Funcionário por ID
```http
GET /funcionario-saude/buscar/{id}
```
Parâmetros:
- `id`: ID do funcionário

#### Atualizar Funcionário
```http
PATCH /funcionario-saude/alterar/{id}
```
Atualiza os dados de um funcionário existente.

#### Excluir Funcionário
```http
DELETE /funcionario-saude/excluir/{id}
```
Remove um funcionário do sistema.

#### Gerenciar Telefones
```http
POST /funcionario-saude/addTelefone
DELETE /funcionario-saude/removeTelefone?removeDynamicRow={index}
```
Endpoints para adicionar e remover telefones de um funcionário.

### Página Inicial
```http
GET /
```
Retorna a página inicial do sistema.

## Modelos de Dados

### Paciente
```json
{
    "id": "Long",
    "nome": "String",
    "email": "String",
    "telefones": [
        {
            "id": "Long",
            "numero": "String",
            "tipo": "String"
        }
    ]
}
```

### Funcionário de Saúde
```json
{
    "id": "Long",
    "nome": "String",
    "email": "String",
    "telefones": [
        {
            "id": "Long",
            "numero": "String",
            "tipo": "String"
        }
    ]
}
```

## Observações Importantes

1. Todos os endpoints retornam templates HTML, pois a API foi desenvolvida para servir uma aplicação web.
2. A validação de dados é realizada automaticamente para campos obrigatórios.
3. O email deve ser único para cada paciente.
4. Os telefones são opcionais e podem ser múltiplos.
5. As mensagens de sucesso ou erro são exibidas através de flash attributes.

## Tecnologias Utilizadas

- Java 24
- Spring Boot
- Spring MVC
- Spring Data JPA
- Thymeleaf (Template Engine)
- Hibernate Validator
- Maven 