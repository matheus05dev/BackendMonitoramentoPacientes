# SQL Query Explanations

Este documento fornece explicações detalhadas para as queries SQL utilizadas no projeto InfraMed, divididas em dois grupos principais para facilitar a compreensão e o uso:

- [Queries de Desenvolvimento](#-queries-de-desenvolvimento-queries_desenvolvimento.sql)
- [Queries Específicas para Auditoria](#-queries-específicas-para-auditoria-queries_auditoria.sql)

---

## 🛠️ Queries de Desenvolvimento (queries_desenvolvimento.sql)

Este arquivo contém um conjunto de queries simples e diretas, todas no formato básico:

```sql
SELECT * FROM nome_da_tabela;
```

O objetivo principal dessas queries é permitir que os desenvolvedores visualizem rapidamente o conteúdo das tabelas do banco de dados durante as fases de **desenvolvimento**, **testes** e **depuração**.

### Para que servem essas queries:

-   **Análise de Dados:** Confirmar se os dados estão sendo criados, atualizados e removidos corretamente.
-   **Depuração de Front-end:** Verificar se o front-end está exibindo dados coerentes com a base de dados.
-   **Depuração de Back-end:** Validar se os endpoints e serviços estão manipulando os dados da forma esperada.
-   **Verificação de Integridade:** Confirmar que o banco está em um estado consistente após operações do sistema.

### Características das queries de desenvolvimento:

-   **Simples e diretas:** Apenas retornam todos os dados da tabela, sem filtros complexos.
-   **Alta abrangência:** Incluem todas as tabelas essenciais do banco `monitoramentopacientedb`.
-   **Uso exclusivo para leitura:** Não alteram dados, apenas exibem o estado atual da base.

### Tabelas consultadas:

-   `especialidades`
-   `alergias`
-   `pessoa`
-   `quarto`
-   `telefone`
-   `atendimento`
-   `paciente`
-   `funcionario_saude`
-   `leitura_sensor`
-   `notificacao`
-   `user`
-   `log`

Essas queries são fundamentais para a **inspeção rápida do banco** e **suporte ao desenvolvimento** contínuo do projeto.

---

## 🔍 Queries Específicas para Auditoria (queries_auditoria.sql)

As queries de auditoria têm como objetivo principal analisar as operações do sistema, garantindo aspectos cruciais como:

-   **Rastreabilidade**
-   **Segurança**
-   **Governança**
-   **Conformidade**
-   **Monitoramento Operacional**

Diferentemente das queries de desenvolvimento, elas não servem para visualizar todos os dados de forma bruta, mas sim para **extrair informações analíticas** e **insights** com base nas atividades registradas no sistema.

### Base da Auditoria

A auditoria do projeto é centralizada na tabela:

-   `log`

Essa tabela é crucial e registra informações detalhadas sobre cada evento, incluindo:

-   **Usuário responsável pela ação** (ou `NULL` para ações automáticas — ex.: IoT)
-   **Tipo de evento** (`event_type`)
-   **Mensagem/descrição** do evento
-   **Data e hora** (`timestamp`) da ocorrência
-   **ID do usuário** (quando aplicável)

### Exemplos de eventos registrados:

-   `TENTATIVA_AUTENTICACAO`
-   `SUCESSO_AUTENTICACAO`
-   `LOGOUT`
-   `SALVAR_LEITURA_SENSOR`
-   Eventos automáticos do módulo IoT
-   Ações realizadas por usuários humanos

### Para que servem as queries de auditoria:

-   Identificar **quem fez o quê e quando**.
-   Analisar **padrões de uso** do sistema.
-   Criar **relatórios de segurança**.
-   Detectar **tentativas de acesso indevido**.
-   Verificar **frequência de leituras de sensores** (IoT).
-   Observar períodos com **maior atividade** no sistema.
-   Acompanhar **atividades individuais** de um usuário.
-   Diferenciar **ações humanas de automações**.

### Tipos de análises realizadas:

-   Ações totais por usuário
-   Distribuição de eventos por tipo
-   Comparação Sistema (IoT) vs Usuário
-   Eventos por hora
-   Eventos dos últimos 7 dias
-   Registros da última hora
-   Consultas específicas por usuário
-   Consultas por padrões de evento (`LIKE`)
-   Uso de funções de janela para medir intervalos

### Exemplos de relatórios extraídos:

-   Usuários mais ativos no sistema
-   Tipos de evento mais frequentes
-   Tentativas e sucessos de autenticação
-   Últimas atividades registradas
-   Frequência de leitura de sensores
-   Histórico de eventos agrupado por dia/hora

Essas queries demonstram a presença de um **sistema de auditoria completo**, essencial para aplicações reais que envolvem segurança, integridade de dados e rastreabilidade — especialmente no contexto hospitalar.

---

## 💡 Conclusão

O conjunto de queries documentado neste arquivo demonstra a **maturidade técnica** na modelagem, desenvolvimento e auditoria do sistema InfraMed.

Além de facilitar o entendimento da base de dados, as consultas de auditoria refletem **boas práticas de governança, observabilidade e segurança**, reforçando a **robustez arquitetural** do projeto.
