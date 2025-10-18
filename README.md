# Wise Buddy API

## Descrição

API para gerenciamento de usuários, contratos, pagamentos, planos, cartões, sessões, recomendações e suitability, desenvolvida em Java 17 com Spring Boot.

---

## Integrantes

- **Caíque Walter Silva** - RM550693
- **Carlos Eduardo Caramante Ribeiro** – RM552159
- **Felipe Heilmann Marques** – RM551026
- **Guilherme Nobre Bernardo** - RM98604
- **Matheus José de Lima Costa** - RM551157

---

## Requisitos

- **Java 17**
- **Docker** (para o banco de dados)

---

## Como subir o banco de dados (MySQL) com Docker

Execute o comando abaixo para criar e iniciar o banco de dados `wisebuddy`:

```bash
docker run --name wisebuddy-mysql -e MYSQL_ROOT_PASSWORD=123 -e MYSQL_DATABASE=wisebuddy -p 3306:3306 -d mysql:8.0
```

---

## Como rodar a API

1. **Clone o repositório**
2. **Configure o application.properties** conforme necessário (usuário, senha, URL do banco etc)
3. **Compile e execute o projeto**:
   ```bash
   ./mvnw spring-boot:run
   ```
   ou
   ```bash
   mvn spring-boot:run
   ```

---

## Documentação Swagger 

Após iniciar a API, acesse a documentação interativa pelo navegador:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Observações

- A porta padrão da API é **8080**.
- A porta padrão do banco de dados MySQL é **3306**.
- O usuário padrão do MySQL é **root** e a senha é **123** (conforme comando acima).
- Certifique-se de que a porta 3306 não esteja em uso por outro serviço MySQL local.

---
