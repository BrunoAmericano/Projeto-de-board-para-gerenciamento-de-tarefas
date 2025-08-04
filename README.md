# Projeto de board para gerenciamento de tarefas 🚀

Bem-vindo ao **Projeto de board para gerenciamento de tarefas** — seu futuro sistema de gerenciamento de tarefas que respeita o que sempre funcionou, com o toque certo de inovação para não te deixar na mão.

---

## 💡 O que é?

Um projeto robusto e minimalista para organizar tarefas em boards, colunas e cards, inspirado na clássica metodologia Kanban. Aqui o foco é produtividade real, nada de frescura.

---

## 🧱 Funcionalidades Principais

- CRUD completo para **Boards**, **Colunas** e **Cards**.
- Movimentação de cards entre colunas com histórico detalhado.
- Bloqueio e desbloqueio de cards com motivos registrados.
- Persistência segura com banco de dados MySQL.
- Arquitetura limpa para facilitar manutenção e evolução.

---

## ⚙️ Tecnologias

- Java 24 (clássico, confiável e estável)
- JDBC para conexão com MySQL
- MySQL 8.0+
- Design orientado a objetos para máxima clareza e organização

---

## 🚦 Como rodar

1. Clone este repositório
2. Configure seu banco MySQL e crie o schema `board_db`
3. Ajuste a conexão no arquivo `Conexao.java`
4. Compile o projeto:
   ```bash
   javac -d bin -cp "lib/*" $(find src -name "*.java")
 Execute:
   ```bash
   java -cp "bin;lib/*" Main