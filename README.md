# PicPay Simplificado: Desafio Backend com Spring Boot

Este projeto é uma implementação do desafio backend, PicPay Simplificado, desenvolvido como parte do desafio de backend do PicPay.

Ler o arquivo DESAFIO.MD, onde haverá as informações sobre o desafio.

## 🚀 Tecnologias Utilizadas

- **[Java 21](https://www.oracle.com/br/java/technologies/downloads/)**
- **[Spring Boot 3.4.1](https://spring.io/projects/spring-boot)**
- **[Spring Web](https://docs.spring.io/spring-boot/reference/web/index.html)**
- **[OpenFeign](https://spring.io/projects/spring-cloud-openfeign)** (para requisições HTTP simplificadas)
- **[H2 Database](https://www.h2database.com/html/main.html)** (banco de dados em memória)
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)**
- **[Spring Mail](https://docs.spring.io/spring-framework/reference/integration/email.html)** (Envio de emails)
- **[Maven](maven.apache.org)** (gerenciamento de dependências e build)
- **[Swagger](https://springdoc.org/)** (Documentação da API)

## 🛠 Configuração e Execução

### Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:

- [JDK 21](https://www.oracle.com/br/java/technologies/downloads/#java21)
- [Maven](https://maven.apache.org/)

### Instalação e Execução

1. Clone o repositório:

   ```bash
   git clone https://github.com/onurbeht/Desafio-backend-PicPay-Simplificado.git
   cd Desafio-backend-PicPay-Simplificado
   ```

2. Instale as dependências, compile e execute a aplicação:

   ```bash
   mvn install
   ```

   ```bash
   mvn spring-boot:run
   ```

3. Para testar, acesse a aplicação, você pode usar um client API(Ex: Postman, Insomnia) na URL padrão http://localhost:8080/api **ou pelo Swagger - http://localhost:8080/api/swagger-ui/index.html** :

   Criação de usuarios.
   Envie o JSON no body da requisição

   ```
   POST - http://localhost:8080/api/users/pf
   { "firstName": "nome",
     "lastName": "sobrenome",
     "email": "email@email.com",
     "password": "1234",
     "document": "cpf"
   }
   ```

   ```
   POST - http://localhost:8080/api/users/pj
   { "firstName": "nome",
     "lastName": "sobrenome",
     "email": "email@email.com",
     "password": "1234",
     "document": "cnpj"
   }
   ```

   Envio de transferencias.
   Envie o JSON no body da requisição - POST

   ```
   http://localhost:8080/api/transfer/send
    { "amount": 10.0,
      "senderId": senderId,
      "receiverId": receiverId
    }

   ```

4. Para acessar o console do banco de dados H2:

   ```
   http://localhost:8080/api/h2
   ```

   - **JDBC URL:** `jdbc:h2:mem:teste`
   - **Usuário:** `sa`
   - **Senha:** _(vazia)_

## 📬 Configuração de E-mail (Mailtrap)

O projeto utiliza o Mailtrap para envio de e-mails de notificação. Para configurar:

1. Crie uma conta no [Mailtrap](https://mailtrap.io/).
2. Obtenha as credenciais(username e password) SMTP e substitua no `./src/main/resources/application.yml`:
   ```yaml
   spring:
    ...
     mail:
       host: sandbox.smtp.mailtrap.io
       port: 587
       username: SEU_USERNAME
       password: SUA_PASSWORD
    ...
   ```

## 📜 Licença

Este projeto é desenvolvido apenas para fins educacionais e desafios de backend.

---
