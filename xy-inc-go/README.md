# xy-inc-go

Este projeto conta com uma aplicação REST com serviços de CRUD para uma entidade chamada **Product**.

## 1. Requisitos e Configurações

Para executar o projeto é necessária a instalação das seguintes ferramentas:

    1. go 1.7.4
    2. Apache Cassandra 3.10

Inicialmente é preciso rodar o Apache Cassadra. Neste projeto não foram realizadas configurações de segurança, logo, não é necessário de usuário e senha para acessar o Cassandra.

Para executar o cassandra é necessário apenas colocar ele para rodar.

```sh
$ cd $CASSANDRA_HOME/bin
$ ./cassandra
```
Uma vez com o banco rodando, é necessário executar os seguintes scripts de criação do banco.

```sql
CREATE KEYSPACE IF NOT EXISTS xy_inc WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE IF NOT EXISTS xy_inc.product (
	id uuid,
	name text,
	description text,
	price float,
	category text,
	PRIMARY KEY (id)
);

INSERT INTO xy_inc.product (id, name, description, price, category) VALUES (uuid(), 'Samsung 40', 'UN40K5100 40-Inch 1080p LED TV', 277.990, 'Electronics');
INSERT INTO xy_inc.product (id, name, description, price, category) VALUES (uuid(), 'iPhone 7', 'Apple iPhone 7 Unlocked Phone 128 GB', 724.890, 'Cell Phones');
```

Após o banco estar rodando, é preciso instalar dependências do GO. Para isso realize a execução dos comandos.

```sh
$ go get -u github.com/gorilla/mux
$ go get -u github.com/gocql/gocql
```

## 2. Executando o Projeto

Após baixar o projeto, para executá-lo é necessário rodar os seguintes comandos dentro da pasta raiz.

```sh
$ go build
$ ./xy-inc-go
```

A aplicação conta com um servidor de aplicação embarcado. Contudo, este mesmo projeto (xy-inc-0.0.1-SNAPSHOT.war) pode ser executado no JBoss EAP 7.0 ou Wildfly 10.

## 3. Testando os serviços

Uma vez com a aplicação rodando é necessário apenas realizar a chamada dos serviços. A seguir são mostradas as chamadas e os respectivos resultados.

GET /products - Lista todos os produtos

```sh
$ curl http://localhost:8080/api/product
[
   {
      "Id":"745e41e3-110f-4f64-8468-d06254e7adb3",
      "Name":"Samsung 40",
      "Description":"UN40K5100 40-Inch 1080p LED TV",
      "Price":277.99,
      "Category":"Electronics"
   },
   {
      "Id":"fc126b42-650a-4594-b301-ebca257a2ecb",
      "Name":"iPhone 7",
      "Description":"Apple iPhone 7 Unlocked Phone 128 GB",
      "Price":724.89,
      "Category":"Cell Phones"
   }
]
```

GET /products/{id} - Busca um produto por id

```sh
$ curl http://localhost:8080/api/product/fc126b42-650a-4594-b301-ebca257a2ecb
{
   "Id":"fc126b42-650a-4594-b301-ebca257a2ecb",
   "Name":"iPhone 7",
   "Description":"Apple iPhone 7 Unlocked Phone 128 GB",
   "Price":724.89,
   "Category":"Cell Phones"
}
```

POST /products - Cria um novo produto

```sh
$ curl -H "Content-Type: application/json" -X POST -d '{"name":"Produto 1", "description":"Descrição 1", "price":10.23, "category":"Categoria 1"}' http://localhost:8080/api/product
{
   "Id":"7c8c68e1-0d79-11e7-9aa5-f40f2421754f",
   "Name":"Produto 1",
   "Description":"Descrição 1",
   "Price":10.23,
   "Category":"Categoria 1"
}
```

PUT /products/{id} - Edita um produto

```sh
$ curl -H "Content-Type: application/json" -X PUT -d '{"Id":"7c8c68e1-0d79-11e7-9aa5-f40f2421754f","Name":"Product A","Description":"Atualizacao","Price":25.9,"Category":"Category A"}' http://localhost:8080/api/product
  
```

DELETE /products/{id} - Deleta um produto

```sh
$ curl -H "Content-Type: application/json" -X DELETE -d '{"Id":"7c8c68e1-0d79-11e7-9aa5-f40f2421754f"}' http://localhost:8080/api/product
```

## 4. Observações

Diversas funcionalidades podem ser desenvolvidas no projeto. Como prentende-se que seja uma aplicação simples e direta, não foram realizados outros desenvolvimentos.

Alguns testes de desempenho foram realizados, tendo alcançado bons resultados. Em caso de necessidade tais informações podem ser geradas conforme sejam requisitadas.
