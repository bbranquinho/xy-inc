# xy-inc-api

Este projeto é responsável por criar APIs/Aplicações REST. Os serviços são criados de acordo com o desejo do usuário, tomando como base modelos de entidade.

## 1. Requisitos e Configurações

Para executar o projeto é necessária a instalação das seguintes ferramentas:

    1. JDK 1.8
    2. Kotlin 1.1 (opcional)
    3. Gradle (versão 2.14.1 - recomendada/testada)

Apesar de não ser necessário para rodar o projeto, é indicado o uso da IDE IntelliJ ([Thank You Jetbrains](https://www.jetbrains.com/idea/)) para realizar novos desenvolvimentos.

## 2. Executando o Projeto

Após baixar o projeto, para executá-lo é necessário rodar os seguintes comandos dentro da pasta raiz (***xy-inc-api***).

```sh
$ gradle clean build
$ java -jar xy-inc-factory/build/libs/xy-inc-factory-0.0.1-SNAPSHOT.war
```

A aplicação conta com um servidor de aplicação embarcado. Contudo, este mesmo projeto (xy-inc-0.0.1-SNAPSHOT.war) pode ser executado no JBoss EAP 7.0 ou Wildfly 10.

## 3. Criação de APIs e Entidades

Para realizar a criação de uma nova API/projeto é necessário especificar alguns parâmetros, sendo eles:

* ***name***: Nome do projeto.
    * *Exemplo*: **mobile-api**
* ***basePackage***: Nome do pacote que será usado como base para a criação do projeto.
    * *Exemplo*: **br.com.mobile.api**
* ***group***: É preciso estabelecer o nome do groupId, sendop este um identificador dos projetos. Em caso de dúvida, utilize o mesmo valor usado no **basePackage**.
    * *Exemplo*: **br.com.mobile.api**
* ***databaseName***: Nome do banco de dados. O banco de dados é criado dinamicamente toda a versão que novos elementos são criados no projeto ou quando a aplicação reinicia.
    * *Exemplo*: **mobile_api**
* ***databaseUsername***: Nome de usuário do banco de dados.
    * *Exemplo*: **root**
* ***databasePassword***: Senha que ficará o banco de dados.
    * *Exemplo*: **root**
* ***port***: Porta em que a aplicação será iniciada. Por padrão é recomentada a porta 8080 ou outras portas que não estão em uso, logo, não podem ser criados dois projetos com a mesma porta.
    * *Exemplo*: **8080**
* ***version***: Versão do projeto.
    * *Exemplo*: **0.0.1-SNAPSHOT**

De posse destes dados é necessário realizar uma chamada POST ao serviço **http://localhost:9000/xy-inc/api/project**, como mostrado a seguir:

```sh
$ curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "name": "mobile-api", \ 
   "basePackage": "br.com.mobile.api", \ 
   "group": "br.com.mobile.api", \ 
   "databaseName": "mobile_api", \  
   "databaseUsername": "root", \
   "databasePassword": "root", \ 
   "port": 8080, \ 
   "version": "0.0.1-SNAPSHOT" \ 
 }' 'http://localhost:9000/xy-inc/api/project'
```

Após a criação do projeto, podem ser criadas entidades. Estas entidades são compostas pelos seguintes parâmetros:

* ***projectName***: Nome do projeto. Este nome faz referência ao projeto que já foi criado anteriormente.
    * *Exemplo*: **mobile-api**
* ***name***: Nome da entidade.
    * *Exemplo*: **product**
* ***fields***: Lista de propriedades da entidade.
    * **name**: Nome da propriedade.
    * **name**: Tipo da propriedade. Existem os seguintes tipos disponíveis: **DATETIME**, **DECIMAL**, **DOUBLE**, **FLOAT**, **INTEGER**, **LONG** e **STRING**. Observação: o tipo deve estar em maiúsclo.
    * *Exemplo*: **"name": "description", "type": "STRING"**

A partir do atributos da entidade, é realizada sua criação com a chamado POST do serviço **http://localhost:9000/xy-inc/api/model**, como mostrado a seguir:

```sh
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "projectName": "mobile-api", \ 
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

Para toda entidade criada é atribuída uma propriedade **id**, responsável por ser o identificador único dos registros da entidade. Sendo assim, não crie propriedade com o nome **id**.

### 3.1. Especificações dos Projetos

Toda vez que projetos ou entidades são criadas, alguns eventos ocorrem na aplicação (projeto).

Após a criação de um novo projeto ele automaticamente é executado, conforme os parâmetros de sua criação.

Outro ponto importante, toda entidade criada faz com que o projeto relacionado a entidade seja reiniciado. 

Como ainda existem diversos pontos a serem melhorados, um deles é a persistência de dados. Toda a vez que a aplicação é reiniciada o banco de dados é recriado, não sendo assim guardados valores anteriormente cadastrados. Uma melhoria futura consiste em controlar alterações do banco de dados com o [Flyway](https://flywaydb.org/) ou [Liquibase](http://www.liquibase.org/). Além disso, é muito importante reassaltar que o banco de dados atual é o [H2](http://www.h2database.com/html/main.html) em memória.

### 3.2. Testando os Serviços

Uma vez com a aplicação rodando é as entidades criadas, são disponibilizados os seguintes serviços. A seguir são mostradas as chamadas e os respectivos resultados.

* GET /**{nome do projeto}**/api/**{entidade}** - Lista todos os dados para a entidade especificada.

```sh
$ curl http://localhost:8080/xy-inc/api/product
```

* GET /**{nome do projeto}**/api/**{entidade}**/**{id}** - Busca uma entidade pelo seu id.

```sh
$ curl http://localhost:8080/xy-inc/api/product/1
```

* POST /**{nome do projeto}**/api/**{entidade}** - Cria um novo registro para a entidade.

```sh
$ curl -H "Content-Type: application/json" -X POST -d '{"name":"Produto 1", "description":"Descrição 1", "price":10.23, "category":"Categoria 1"}' http://localhost:8080/xy-inc/api/product
```

* PUT /**{nome do projeto}**/api/**{entidade}** - Edita o registro de uma entidade.

```sh
$ curl -H "Content-Type: application/json" -X PUT -d '{"id":3, "name":"Produto Atualizada", "description":"Descrição Atualizada", "price":10.23, "category":"Categoria Atualizada"}' http://localhost:8080/xy-inc/api/product
```

* DELETE /**{nome do projeto}**/api/**{entidade}**/**{id}** - Deleta o registro de uma entidade pelo **id**.

```sh
$ curl -H "Content-Type: application/json" -X DELETE http://localhost:8080/xy-inc/api/product/3
```

* DELETE /**{nome do projeto}**/api/**{entidade}** - Deleta o registro de uma entidade pelo **id**.

```sh
$ curl -H "Content-Type: application/json" -X DELETE -d '{"id":3}' http://localhost:8080/xy-inc/api/product
```

### 3.3. Outros Recursos

Para cada projeto existe uma interface Swagger com as informações dos serviços disponíveis por entidade, estando acessível em: **http://localhost:{porta do projeto}/{nome do projeto}/swagger-ui.html**

Além do Swagger, o banco de dados pode ser acessado em **http://localhost:{porta do projeto}/{nome do projeto}/h2-console** O usuário e a senha do banco são aqueles usados na criação do projeto e a url é **jdbc:h2:mem:{nome do banco}**, lembrando que o **{nome do banco}** corresponde ao nome do banco definido no momento da criação do projeto.

## 4. Outros Recursos e Observações

Neste primeiro momento não existe uma interface especifica que facilita a manipulação das APIs. Logo, foi disponibilizado o Swagger para facilitar o acesso e a manipulação dos projetos. Esta documentação é acessível na URL **http://localhost:9000/xy-inc/swagger-ui.html**

Diversas funcionalidades podem ser desenvolvidas no projeto. Como prentende-se que seja uma aplicação simples e direta, não foram realizados outros desenvolvimentos, tanto a nível de arquitetura, requisitos, deploy e testes. Foram criados testes unitários para alguns pontos da API, contudo, outros testes podem ser desenvolvidos futuramente.
