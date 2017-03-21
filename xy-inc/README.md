# xy-inc

Este projeto conta com uma aplicação REST com serviços de CRUD para uma entidade chamada **Product**.

## 1. Requisitos e Configurações

Para executar o projeto é necessária a instalação das seguintes ferramentas:

    1. JDK 1.8
    2. Kotlin 1.1 (opcional)(recomendado)
    3. Gradle 2.14.1 (pode ser usado em outras versões)

O projeto utiliza um banco embarcado (H2) para desenvolvimento e para produção está configurado inicialmente o MySQL, apesar de que outros bancos podem ser configurados facilmente.
 
## 2. Executando o Projeto
 
Após baixar o projeto, para executá-lo é necessário rodar os seguintes comandos dentro da pasta raiz.

```sh
$ gradle clean build
$ java -jar build/libs/xy-inc-0.0.1-SNAPSHOT.jar
```

A aplicação conta com um servidor de aplicação embarcado. Contudo, este mesmo projeto (xy-inc-0.0.1-SNAPSHOT.war) pode ser executado no JBoss EAP 7.0 ou Wildfly 10.

## 3. Testando os serviços

Uma vez com a aplicação rodando é necessário apenas realizar a chamada dos serviços. A seguir são mostradas as chamadas e os respectivos resultados.

GET /products - Lista todos os produtos

```sh
$ curl http://localhost:8080/xy-inc/api/product
[{
  "id" : 1,
  "name" : "Samsung 40",
  "description" : "UN40K5100 40-Inch 1080p LED TV",
  "price" : 277.990,
  "category" : "Electronics"
}, {
  "id" : 2,
  "name" : "iPhone 7",
  "description" : "Apple iPhone 7 Unlocked Phone 128 GB",
  "price" : 724.890,
  "category" : "Cell Phones"
}]
```

GET /products/{id} - Busca um produto por id

```sh
$ curl http://localhost:8080/xy-inc/api/product/1
{
  "id" : 1,
  "name" : "Samsung 40",
  "description" : "UN40K5100 40-Inch 1080p LED TV",
  "price" : 277.990,
  "category" : "Electronics"
}
```

POST /products - Cria um novo produto

```sh
$ curl -H "Content-Type: application/json" -X POST -d '{"name":"Produto 1", "description":"Descrição 1", "price":10.23, "category":"Categoria 1"}' http://localhost:8080/xy-inc/api/product
  {
    "id" : 5,
    "name" : "Produto 1",
    "description" : "Descrição 1",
    "price" : 10.23,
    "category" : "Categoria 1"
  }
```

PUT /products - Edita um produto

```sh
$ curl -H "Content-Type: application/json" -X PUT -d '{"id":5, "name":"Produto Atualizado", "description":"Descrição Atualizada", "price":10.23, category":"Categoria Atualizada"}' http://localhost:8080/xy-inc/api/product
  {
    "id" : 5,
    "name" : "Produto Atualizado",
    "description" : "Descrição Atualizada",
    "price" : 10.23,
    "category" : "Categoria Atualizada"
  }
```

DELETE /products/{id} - Deleta um produto

```sh
$ curl -H "Content-Type: application/json" -X DELETE http://localhost:8080/xy-inc/api/product/5
```
DELETE /products - Deleta um produto

```sh
$ curl -H "Content-Type: application/json" -X DELETE -d '{"id":5}' http://localhost:8080/xy-inc/api/product
```

## 4. Outros Recursos

Como um dos objetivos da aplicação é que seja consumida por outras aplicações, foi disponibilizada uma documentação dos serviços. Esta documentação é acessível na URL http://localhost:8080/xy-inc/swagger-ui.html

![Swagger](https://cloud.githubusercontent.com/assets/1013619/24131856/dfff61b2-0dcf-11e7-9340-66d5a7fe6a0f.png)

É possível acessar o banco embarcado pela a URL: http://localhost:8080/xy-inc/h2-console

* JDBC URL: jdbc:h2:mem:xy_inc
* Username: root
* Password: 

![Swagger](https://cloud.githubusercontent.com/assets/1013619/24131859/e3f357d8-0dcf-11e7-8a38-9be645f39fba.png)

## 5. Observações

Diversas funcionalidades podem ser desenvolvidas no projeto. Como prentende-se que seja uma aplicação simples e direta, não foram realizados outros desenvolvimentos.

Foram criados alguns testes de integração, outros testes mais elaborados podem ser desenvolvidos de acordo com as regras de negócio.

Alguns testes de desempenho foram realizados, tendo alcançado bons resultados. Em caso de necessidade tais informações podem ser geradas conforme seja requisitado.
