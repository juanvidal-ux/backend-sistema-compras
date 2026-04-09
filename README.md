# 🛒 Sistema de Compras Corporativo (V1.0)

Sistema de gestão de pedidos de compra (PCN) desenvolvido com foco em segurança, auditoria e controle hierárquico de acesso (RBAC).

![Status](https://img.shields.io/badge/STATUS-CONCLUÍDO-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring](https://img.shields.io/badge/Spring%20Boot-3-green)

## 🚀 Funcionalidades Principais

### 🔒 Segurança & Acesso
- **Autenticação Segura:** Login via banco de dados com senhas criptografadas (BCrypt).
- **Controle de Níveis (RBAC):**
  - `ADMIN`: Acesso total (Gestão de Usuários, BI, Auditoria, Migração).
  - `USER`: Acesso operacional (Criar Pedidos, Visualizar Lista, Cadastros básicos).
- **Trilha de Auditoria:** Registro automático de quem criou, editou ou excluiu pedidos e usuários.

### 📋 Gestão de Pedidos
- Emissão de pedidos com múltiplos itens.
- Cálculo automático de totais.
- **Exportação:** Geração de documento oficial em **Word (.docx)** e relatório gerencial em **Excel (.xlsx)**.
- **Busca Inteligente:** Filtragem em tempo real por PCN, Fornecedor ou descrição do item.
- **Paginação:** Performance otimizada exibindo 20 itens por página.

### 📊 Inteligência (BI)
- **Curva ABC (Pareto):** Análise 80/20 dos gastos por insumo.
- **Dashboard Financeiro:** Gráficos de gastos por ano e setor.

---

## 🛠️ Tecnologias Utilizadas

### Backend (API REST)
- **Java 17** & **Spring Boot 3**
- **Spring Security** (Autenticação/Autorização)
- **Spring Data JPA / Hibernate** (Persistência)
- **MySQL** (Banco de Dados)
- **Apache POI** (Geração de Excel/Word)

### Frontend (SPA - Single Page Application)
- **HTML5 & CSS3** (Design Responsivo e Moderno)
- **JavaScript (ES6+)** (Consumo de API via Fetch)
- **Chart.js** (Gráficos)
- **SweetAlert2** (Alertas visuais)

---

## ⚙️ Como Rodar o Projeto

### Pré-requisitos
1. **Java JDK 17+** instalado.
2. **MySQL** rodando na porta `3306`.
3. Criar um banco de dados chamado `sistemacompras`.

### Passo a Passo
1. Configure o acesso ao banco no arquivo:
   `src/main/resources/application.properties`
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/sistemacompras
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   spring.jpa.hibernate.ddl-auto=update

   Entendido\! Manter a interface limpa e funcional ("Clean Design") é muitas vezes a melhor escolha para sistemas corporativos. O importante é que a segurança e a usabilidade (paginação, busca) estão resolvidas.

Então, voltando ao seu pedido sobre a **Documentação**, já temos:

1.  **Engenharia de Requisitos** (RFs e RNFs).
2.  **Diagramas UML** (Classes, Casos de Uso e Sequência).

Para fechar o pacote documental com chave de ouro, vou preparar para você o arquivo **`README.md`**.

Este é o arquivo que fica na capa do GitHub/GitLab ou na raiz da pasta para quem for avaliar seu código técnico. Ele resume tudo o que fizemos.

### 📄 Arquivo `README.md` (Documentação Final)

Crie um arquivo chamado `README.md` na raiz do seu projeto e cole este conteúdo.

````markdown
# 🛒 Sistema de Compras Corporativo (V1.0)

Sistema de gestão de pedidos de compra (PCN) desenvolvido com foco em segurança, auditoria e controle hierárquico de acesso (RBAC).

![Status](https://img.shields.io/badge/STATUS-CONCLUÍDO-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring](https://img.shields.io/badge/Spring%20Boot-3-green)

## 🚀 Funcionalidades Principais

### 🔒 Segurança & Acesso
- **Autenticação Segura:** Login via banco de dados com senhas criptografadas (BCrypt).
- **Controle de Níveis (RBAC):**
  - `ADMIN`: Acesso total (Gestão de Usuários, BI, Auditoria, Migração).
  - `USER`: Acesso operacional (Criar Pedidos, Visualizar Lista, Cadastros básicos).
- **Trilha de Auditoria:** Registro automático de quem criou, editou ou excluiu pedidos e usuários.

### 📋 Gestão de Pedidos
- Emissão de pedidos com múltiplos itens.
- Cálculo automático de totais.
- **Exportação:** Geração de documento oficial em **Word (.docx)** e relatório gerencial em **Excel (.xlsx)**.
- **Busca Inteligente:** Filtragem em tempo real por PCN, Fornecedor ou descrição do item.
- **Paginação:** Performance otimizada exibindo 20 itens por página.

### 📊 Inteligência (BI)
- **Curva ABC (Pareto):** Análise 80/20 dos gastos por insumo.
- **Dashboard Financeiro:** Gráficos de gastos por ano e setor.

---

## 🛠️ Tecnologias Utilizadas

### Backend (API REST)
- **Java 17** & **Spring Boot 3**
- **Spring Security** (Autenticação/Autorização)
- **Spring Data JPA / Hibernate** (Persistência)
- **MySQL** (Banco de Dados)
- **Apache POI** (Geração de Excel/Word)

### Frontend (SPA - Single Page Application)
- **HTML5 & CSS3** (Design Responsivo e Moderno)
- **JavaScript (ES6+)** (Consumo de API via Fetch)
- **Chart.js** (Gráficos)
- **SweetAlert2** (Alertas visuais)

---

## ⚙️ Como Rodar o Projeto

### Pré-requisitos
1. **Java JDK 17+** instalado.
2. **MySQL** rodando na porta `3306`.
3. Criar um banco de dados chamado `sistemacompras`.

### Passo a Passo
1. Configure o acesso ao banco no arquivo:
   `src/main/resources/application.properties`
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/sistemacompras
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   spring.jpa.hibernate.ddl-auto=update
````

2.  Compile e rode o projeto (via IDE ou Terminal):

    ```bash
    ./mvnw spring-boot:run
    ```

3.  Acesse no navegador:
    `http://localhost:8080/login.html`

### 🔑 Credenciais Iniciais

O sistema criará automaticamente o administrador no primeiro boot:

  - **Login:** `admin`
  - **Senha:** `admin123`

-----

## 📚 Modelagem (UML)

O sistema segue o padrão de camadas (**Controller** -\> **Service** -\> **Repository** -\> **Entity**).

### Estrutura de Banco de Dados

  - **usuarios:** (id, login, senha\_hash, papel)
  - **pedidos\_compra:** (id, pcn, fornecedor\_id, total...)
  - **itens\_pedido:** (id, pedido\_id, descricao, valor...)
  - **log\_auditoria:** (id, usuario, acao, data\_hora)

-----

> **Desenvolvido por:** [Juan Vidal]
> **Data:** Novembro/2025

```
Compile e rode o projeto (via IDE ou Terminal):
./mvnw spring-boot:run

---
Acesse no navegador: http://localhost:8080/login.html


### 🏁 Conclusão Final do Projeto
🔑 Credenciais Iniciais
O sistema criará automaticamente o administrador no primeiro boot:

Login: admin

Senha: admin123
Modelagem (UML)
O sistema segue o padrão de camadas (Controller -> Service -> Repository -> Entity).

Estrutura de Banco de Dados
usuarios: (id, login, senha_hash, papel)

pedidos_compra: (id, pcn, fornecedor_id, total...)

itens_pedido: (id, pedido_id, descricao, valor...)

log_auditoria: (id, usuario, acao, data_hora)
Você tem em mãos um projeto de **Portfólio Sênior**.


