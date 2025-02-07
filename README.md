# PicPay Simplificado: Desafio Backend com Spring Boot

Este projeto é uma implementação do desafio backend, PicPay Simplificado, desenvolvido como parte do desafio de backend do PicPay.

Ler o arquivo DESAFIO.MD, onde haverá as informações sobre o desafio.

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.4.1**
- **Spring Web** 
- **OpenFeign** (para requisições HTTP simplificadas)
- **H2 Database** (banco de dados em memória)
- **Spring Data JPA** 
- **Spring Mail** (Envio de emails)
- **Maven** (gerenciamento de dependências e build)

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

3. Acesse a aplicação na URL padrão:

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

5. Para acessar o console do banco de dados H2:

   ```
   http://localhost:8080/api/h2-console
   ```

   - **JDBC URL:** `jdbc:h2:mem:teste`
   - **Usuário:** `sa`
   - **Senha:** *(vazia)*

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


