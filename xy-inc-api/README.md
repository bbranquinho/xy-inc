# xy-inc-api

Este projeto é responsável por realizar a criação de uma aplicação que disponibiliza serviços REST. OS serviços são criados de acordo com o desejo do usuário, tomando como base modelos de entidade.

O projeto criado é composto por um grupo de tecnologias, escolhidas com o objetivo de permitir futuras evoluções da aplicação.

## 1. Requisitos e Configurações

Para executar o projeto é necessária a instalação das seguintes ferramentas:

    1. JDK 1.8
    2. Kotlin 1.1 (opcional)
    3. Gradle (versão 2.14.1 - recomendada/testada)

Apesar de não ser necessário para rodar o projeto, é indicado o uso da IDE IntelliJ ([Thank You Jetbrains](https://www.jetbrains.com/idea/)) para realizar desenvolvimentos no projeto.

## 2. Executando o Projeto

Após baixar o projeto, para executá-lo é necessário rodar os seguintes comandos dentro da pasta raiz.

```sh
$ gradle clean build
$ java -jar xy-inc-factory/build/libs/xy-inc-factory-0.0.1-SNAPSHOT.war
```

A aplicação conta com um servidor de aplicação embarcado. Contudo, este mesmo projeto (xy-inc-0.0.1-SNAPSHOT.war) pode ser executado no JBoss EAP 7.0 ou Wildfly 10.

## 3. Criando um novo Projeto

Para realizar a criação de um novo projeto é necessário especificar alguns parâmetros, sendo eles:

* ***name***: Nome do projeto.
    * *Exemplo*: **xy-inc**
* ***basePackage***: Nome do pacote que será usado como base para a criação do projeto.
    * *Exemplo*: **br.com.xy.inc**
* ***group***: É preciso estabelecer o nome do groupId, sendop este um identificador dos projetos. Em caso de dúvida, utilize o mesmo valor usado no **basePackage**.
    * *Exemplo*: **br.com.xy.inc**
* ***databaseName***: Nome do banco de dados. O banco de dados é criado dinamicamente toda a versão que novos elementos são criados no projeto ou quando a aplicação reinicia.
    * *Exemplo*: **xy_inc**
* ***databaseUsername***: Nome de usuário do banco de dados.
    * *Exemplo*: **root**
* ***databasePassword***: Senha que ficará o banco de dados.
    * *Exemplo*: **root**
* ***port***: Porta em que a aplicação será iniciada. Por padrão é recomentada a porta 8080 ou outras portas que não estão em uso.
    * *Exemplo*: **8080**
* ***version***: Versão do projeto.
    * *Exemplo*: **0.0.1-SNAPSHOT**

De posse destes dados é necessário realizar uma chamada POST ao serviço **http://localhost:9000/xy-inc/api/project**, como mostrado a seguir:

```sh
$ curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "name": "xy-inc", \ 
   "basePackage": "br.com.xy.inc", \ 
   "group": "br.com.xy.inc", \ 
   "databaseName": "xy_inc", \  
   "databaseUsername": "root", \
   "databasePassword": "root", \ 
   "port": 8080, \ 
   "version": "0.0.1-SNAPSHOT" \ 
 }' 'http://localhost:9000/xy-inc/api/project'
```

Após a criação do projeto, podem ser criadas entidades. Estas entidades são compostas pelos seguintes parâmetros:

* ***projectName***: Nome do projeto. Este nome faz referência ao projeto que já foi criado anteriormente.
    * *Exemplo*: **xy-inc**
* ***name***: Nome da entidade.
    * *Exemplo*: **product**
* ***fields***: Propriedade da entidade.
    * **name**: Nome da propriedade.
    * **name**: Tipo da propriedade. Existem os seguintes tipos disponíveis: **DATETIME**, **DECIMAL**, **DOUBLE**, **FLOAT**, **INTEGER**, **LONG** e **STRING**. Observação: o tipo deve estar em maiúsclo.
    * *Exemplo*: **"name": "description", "type": "STRING"**

A partir do atributos da entidade, é realizada sua criação com a chamado POST do serviço **http://localhost:9000/xy-inc/api/model**, como mostrado a seguir:

```sh
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "projectName": "xy-inc", \ 
   "entity": { \ 
     "name": "product", \ 
     "tableName": "tb_product", \ 
     "fields": [ \ 
       { \ 
         "name": "name", \ 
         "type": "STRING" \ 
       }, \ 
      { \ 
         "name": "description", \ 
         "type": "STRING" \ 
       }, \ 
      { \ 
         "name": "price", \ 
         "type": "DECIMAL" \ 
       }, \ 
      { \ 
         "name": "category", \ 
         "type": "STRING" \ 
       } \ 
     ] \ 
   } \ 
 }' 'http://localhost:9000/xy-inc/api/model'
 ```

Após a criação de novos projetos eles começam automaticamente a serem executados. 

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
    "id" : 3,
    "name" : "Produto 1",
    "description" : "Descrição 1",
    "price" : 10.23,
    "category" : "Categoria 1"
  }
```

PUT /products - Edita um produto

```sh
$ curl -H "Content-Type: application/json" -X PUT -d '{"id":3, "name":"Produto Atualizada", "description":"Descrição Atualizada", "price":10.23, "category":"Categoria Atualizada"}' http://localhost:8080/xy-inc/api/product
  {
    "id" : 3,
    "name" : "Produto Atualizado",
    "description" : "Descrição Atualizada",
    "price" : 10.23,
    "category" : "Categoria Atualizada"
  }
```

DELETE /products/{id} - Deleta um produto

```sh
$ curl -H "Content-Type: application/json" -X DELETE http://localhost:8080/xy-inc/api/product/3
```
DELETE /products - Deleta um produto

```sh
$ curl -H "Content-Type: application/json" -X DELETE -d '{"id":3}' http://localhost:8080/xy-inc/api/product
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
