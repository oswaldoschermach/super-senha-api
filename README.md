# Super Senha API - Jogo de Adivinhação de Palavras 🎮🔠

[![Java](https://img.shields.io/badge/Java-17-red?logo=java)](https://java.com)  

> Backend para um jogo multiplayer de adivinhação de palavras com autenticação JWT e sistema de rodadas.

---

## ✨ Funcionalidades

- 🔐 Cadastro e autenticação de jogadores com JWT  
- 🏠 Criação e gerenciamento de salas de jogo  
- 🔄 Sistema de rodadas com sorteio de duplas e palavras  
- 📊 Palavras classificadas por dificuldade (Fácil, Médio, Difícil)  
- 🏆 Pontuação e ranking de jogadores  
- 📝 CRUD de palavras (administração)  

---

## 🛠 Tecnologias

| Tecnologia                                                                 | Uso Principal          |
|---------------------------------------------------------------------------|------------------------|
| ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)             | Linguagem principal     |
| ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)          | Framework backend       |
| ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white) | Banco de dados          |
| ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)   | Autenticação            |
| ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white)       | Documentação da API     |

---

## 🚀 Instalação

### Pré-requisitos

- Java 17  
- Maven 3.9+  
- PostgreSQL  
- Git

### Passos

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/super-senha-api.git
cd super-senha-api
```

- Crie um banco de dados PostgreSQL chamado `super_senha`
- Configure as credenciais no arquivo `application.yml` ou `application.properties`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/super_senha
    username: seu_usuario
    password: sua_senha
```

```bash
# Execute a aplicação
mvn spring-boot:run
```

---

## 🔗 Acesse a documentação

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 📌 Endpoints Principais

| Método | Endpoint             | Descrição                     |
|--------|----------------------|-------------------------------|
| POST   | `/api/auth/register` | Registrar jogador             |
| POST   | `/api/auth/login`    | Autenticar e receber JWT      |
| GET    | `/api/player/all`    | Listar todos os jogadores     |
| POST   | `/api/gameroom`      | Criar nova sala de jogo       |
| POST   | `/api/round/start`   | Iniciar nova rodada           |
| POST   | `/api/round/guess`   | Enviar palpite                |
| POST   | `/api/round/complete`| Finalizar rodada              |
| GET    | `/api/ranking`       | Obter ranking geral           |

---

## 🔒 Segurança

Todos os endpoints (exceto os de `/auth`) requerem um token JWT no cabeçalho:

```http
Authorization: Bearer seu_token_aqui
```

---

## 🗃 Exemplo de Dados

```json
{
  "id": 12,
  "value": "astronauta",
  "status": "ACTIVE",
  "difficulty": "HARD",
  "gameRoomId": 3
}
```

---

## 📜 Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## 👤 Autor

**Oswaldo Schermach**  
Desenvolvedor backend apaixonado por jogos e sistemas multiplayer.
