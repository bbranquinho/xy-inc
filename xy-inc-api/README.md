# xy-inc-api

Este projeto é responsável por criar APIs/projetos REST. Os serviços são criados de acordo com o desejo do usuário, tomando como base modelos.

Neste primeiro momento não existe uma interface especifica que facilita a manipulação das APIs. Logo, foi disponibilizado o Swagger para facilitar o acesso e a manipulação das APIs. Esta documentação é acessível na URL **ht<span>tp://localhost:9000/xy-inc/swagger-ui.html**

Diversas funcionalidades podem ser desenvolvidas na API. Como prentende-se que seja uma aplicação simples e direta, não foram realizados outros desenvolvimentos, tanto a nível de arquitetura, requisitos, deploy, validações e testes. Foram criados testes unitários para alguns pontos da API, contudo, outros testes podem ser desenvolvidos futuramente. Um ponto muito importante que precisa ser realizado em trabalhos futuros é a validação dos parâmetros dos serviços.

## 1. Requisitos e Configurações

Para executar o projeto é necessária a instalação das seguintes ferramentas:

    1. JDK 1.8
    2. Gradle (versão 3.5 - testada)

Apesar de não ser necessário para rodar o projeto, é indicado o uso da IDE IntelliJ ([thank you Jetbrains](https://www.jetbrains.com/idea/)) para realizar novos desenvolvimentos.

## 2. Executando o Projeto

Após baixar o projeto, para executá-lo é necessário rodar os seguintes comandos dentro da pasta raiz (***xy-inc-api***).

```sh
$ gradle clean build
$ java -jar xy-inc-factory/build/libs/xy-inc-factory-0.0.1-SNAPSHOT.war
```

A aplicação conta com um servidor de aplicação embarcado. Contudo, este mesmo projeto (**xy-inc-factory-0.0.1-SNAPSHOT.war**) pode ser executado no JBoss EAP 7.0 ou Wildfly 10.

## 3. Criação de APIs e Modelos

Para realizar a criação de uma nova API é necessário especificar alguns parâmetros, sendo eles:

* ***name***: Nome da API.
    * *Exemplo*: **mobile-api**
* ***basePackage***: Nome do pacote que será usado como base para a criação da API.
    * *Exemplo*: **br.com.mobile.api**
* ***group***: É preciso estabelecer o nome do groupId, sendo este um identificador de um grupo de APIs. Em caso de dúvida, utilize o mesmo valor usado no **basePackage**.
    * *Exemplo*: **br.com.mobile.api**
* ***databaseName***: Nome do banco de dados. O banco de dados é criado dinamicamente toda vez que novos elementos são criados na API ou quando a aplicação reinicia.
    * *Exemplo*: **mobile_api**
* ***databaseUsername***: Nome de usuário do banco de dados.
    * *Exemplo*: **root**
* ***databasePassword***: Senha que ficará o banco de dados.
    * *Exemplo*: **root**
* ***port***: Porta em que a aplicação será iniciada. Por padrão é recomentada a porta 8080 ou outras portas que não estão em uso, logo, não podem ser criadas duas APIs com a mesma porta.
    * *Exemplo*: **8080**
* ***version***: Versão da API.
    * *Exemplo*: **0.0.1-SNAPSHOT**

De posse destes dados é necessário realizar uma chamada POST ao serviço **ht<span>tp://localhost:9000/xy-inc/api/project**, como mostrado a seguir:

```sh
$ curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "name":"mobile-api", "basePackage":"br.com.mobile.api", "group":"br.com.mobile.api", "databaseName":"mobile_api", "databaseUsername":"root", "databasePassword":"root", "port":8080, "version":"0.0.1-SNAPSHOT" }' 'http://localhost:9000/xy-inc/api/project'
```

Após a criação da API, podem ser criados modelos. Estes modelos são compostas pelos seguintes parâmetros:

* ***projectName***: Nome da API. Este nome faz referência a API que já foi criada anteriormente.
    * *Exemplo*: **mobile-api**
* ***name***: Nome do modelo.
    * *Exemplo*: **product**
* ***fields***: Lista de propriedades do modelo.
    * **name**: Nome da propriedade.
    * **name**: Tipo da propriedade. Existem os seguintes tipos disponíveis: **DATETIME**, **DECIMAL**, **DOUBLE**, **FLOAT**, **INTEGER**, **LONG** e **STRING**. Observação: o tipo deve estar em maiúsclo.
    * *Exemplo*: **"name": "description", "type": "STRING"**

A partir dos atributos do modelo, é realizada sua criação com a chamado POST do serviço **ht<span>tp://localhost:9000/xy-inc/api/project/model**, como mostrado a seguir:

```sh
$ curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "name": "Product", "tableName": "tb_product", "fields": [{ "name":"name", "type":"STRING" }, { "name":"description", "type":"STRING" }, { "name":"price", "type":"DECIMAL" }, { "name":"category", "type":"STRING" }] }' 'http://localhost:9000/xy-inc/api/project/{projectName}/model?projectName=mobile-api'
```

Para todo modelo criado é atribuída uma propriedade **id**, responsável por ser o identificador único dos registros do modelo. Sendo assim, não crie propriedade com o nome **id**.

Toda vez que APIs ou modelos são criados os serviços de gerenciamento da API. Estes serviços são responsáveis por iniciar, parar, reiniciar e monitorar a API.

Após a criação de uma API ela é automaticamente colocada para rodar, conforme os parâmetros de sua criação. Outro ponto importante, todo modelo criado faz com que a API relacionado ao modelo seja reiniciada.

Como ainda existem diversos pontos a serem melhorados, um deles é a persistência de dados. Toda a vez que a aplicação é reiniciada o banco de dados é recriado, não sendo assim guardados valores anteriormente cadastrados. Além disso, é muito importante reassaltar que o banco de dados atual é o [H2](http://www.h2database.com/html/main.html) em memória.

## 4. Gerenciamento das APIs

Para facilitar o controle das APIs, foram criados serviços que realizam a listagem das APIs e dos modelos.  A seguir são mostrados estes serviços e exemplos de chamadas.

* GET **ht<span>tp://localhost:9000/xy-inc/api/project** - Lista todas as APIs.

```sh
$ curl -X GET --header 'Accept: application/json' 'http://localhost:9000/xy-inc/api/project'
```

* GET **ht<span>tp://localhost:9000/xy-inc/api/project/{nome da API}** - Busca os dados de uma API.

```sh
$ curl -X GET --header 'Accept: application/json' 'http://localhost:9000/xy-inc/api/project/mobile-api'
```

* GET **ht<span>tp://localhost:9000/xy-inc/api/project/{nome da API}/model** - Lista os modelos de uma API.

```sh
$ curl -X GET --header 'Accept: application/json' 'http://localhost:9000/xy-inc/api/project/mobile-api/model'
```

* GET **ht<span>tp://localhost:9000/xy-inc/api/project/{nome da API}/model/{nome do modelo}** - Busca informações de um modelo de uma API.

```sh
$ curl -X GET --header 'Accept: application/json' 'http://localhost:9000/xy-inc/api/project/mobile-api/model/product'
```

Para gerenciar a API foram criados serviços responsáveis por iniciar, parar, reiniciar e monitorar a API. A seguir são mostrados estes serviços e exemplos de chamadas.

* POST **ht<span>tp://localhost:9000/xy-inc/api/manager/start/{nome da API}** - Inicia a execução da API.

```sh
$ curl -X POST --header 'Accept: application/json' 'http://localhost:9000/xy-inc/api/manager/start/mobile-api'
```

* POST **ht<span>tp://localhost:9000/xy-inc/api/manager/stop/{nome da API}** - Para a execução da API.

```sh
$ curl -X POST --header 'Accept: application/json' 'http://localhost:9000/xy-inc/api/manager/stop/mobile-api'
```

* POST **ht<span>tp://localhost:9000/xy-inc/api/manager/restart/{nome da API}** - Reinicia a execução da API.

```sh
$ curl -X POST --header 'Accept: application/json' 'http://localhost:9000/xy-inc/api/manager/restart/mobile-api'
```

* GET **ht<span>tp://localhost:9000/xy-inc/api/manager/log/{nome da API}** - Recupera parte do log da API.

```sh
$ curl -X GET --header 'Accept: application/json' 'http://localhost:9000/xy-inc/api/manager/log/mobile-api'
```

* GET **ht<span>tp://localhost:9000/xy-inc/api/manager/running** - Lista as APIs que estão em execução.

```sh
$ curl -X GET --header 'Accept: application/json' 'http://localhost:9000/xy-inc/api/manager/running'
```

## 5. Testando as APIs


Para cada API existe uma interface Swagger com as informações dos serviços disponíveis por modelo, estando acessível em: **ht<span>tp://localhost:{porta da API}/{nome da API}/swagger-ui.html** Por exemplo, para a API **mobile-api** na porta 8080 temos o caminho **ht<span>tp://localhost:8080/mobile-api/swagger-ui.html**

Além do Swagger, o banco de dados pode ser acessado em **ht<span>tp://localhost:{porta da API}/{nome da API}/h2-console** Por exemplo, para a API **mobile-api** na porta 8080 temos o caminho **ht<span>tp://localhost:8080/mobile-api/h2-console** O usuário e a senha do banco são aqueles usados na criação da API e a url é **jdbc:h2:mem:{nome do banco}**, lembrando que o **{nome do banco}** corresponde ao nome do banco definido no momento da criação da API.

Uma vez com a API rodando e os modelos criados, são disponibilizados os seguintes serviços. A seguir são mostradas as chamadas dos serviços disponíveis.

* GET ht<span>tp://localhost:**{porta da API}**/**{nome da API}**/api/**{modelo}** - Lista todos os dados para a modelo especificada.

```sh
$ curl http://localhost:8080/mobile-api/api/product
```

* GET ht<span>tp://localhost:**{porta da API}**/**{nome da API}**/api/**{modelo}**?page=**{página}**&size=**{tamanho da página}** - Lista paginado (size = tamanho da página, page = número da página) os dados para o modelo especificado. Além de realizar a paginação, no cabeçalho da requisição são retornados os dados das páginas e da quantidade total de registros.

```sh
$ curl http://localhost:8080/mobile-api/api/product?page=0&size=10
```

* GET ht<span>tp://localhost:**{porta da API}**/**{nome da API}**/api/**{modelo}**?page=**{página}**&size=**{tamanho da página}**&direction=**{direção da ordenação}**&fields=**{campos do modelo}** - Lista paginado (size = tamanho da página)(page = número da página)(direction = direção da ordenação)(fields = lista de campos do modelo separados por vírgula) os dados para o mode especificado usando ordenação por campos do modelo. Além de realizar a paginação, no cabeçalho da requisição são retornados os dados das páginas e da quantidade total de registros.

```sh
$ curl http://localhost:8080/mobile-api/api/product?page=0&size=10&direction=DESC&fields=name,description
```

* GET ht<span>tp://localhost:**{porta da API}**/**{nome da API}**/api/**{modelo}**/**{id}** - Busca um modelo pelo seu id.

```sh
$ curl http://localhost:8080/mobile-api/api/product/1
```

* POST ht<span>tp://localhost:**{porta da API}**/**{nome da API}**/api/**{modelo}** - Cria um novo registro para um modelo.

```sh
$ curl -H "Content-Type: application/json" -X POST -d '{"name":"Produto 1", "description":"Descrição 1", "price":10.23, "category":"Categoria 1"}' http://localhost:8080/mobile-api/api/product
```

* PUT ht<span>tp://localhost:**{porta da API}**/**{nome da API}**/api/**{modelo}** - Edita o registro de um modelo.

```sh
$ curl -H "Content-Type: application/json" -X PUT -d '{"id":1, "name":"Produto Atualizada", "description":"Descrição Atualizada", "price":10.23, "category":"Categoria Atualizada"}' http://localhost:8080/mobile-api/api/product
```

* DELETE ht<span>tp://localhost:**{porta da API}**/**{nome da API}**/api/**{modelo}**/**{id}** - Deleta o registro de um modelo pelo **id**.

```sh
$ curl -H "Content-Type: application/json" -X DELETE http://localhost:8080/mobile-api/api/product/1
```

* DELETE ht<span>tp://localhost:**{porta da API}**/**{nome da API}**/api/**{modelo}** - Deleta o registro de um modelo pelo **id**.

```sh
$ curl -H "Content-Type: application/json" -X DELETE -d '{"id":1}' http://localhost:8080/mobile-api/api/product
```

Para cada API é criado um projeto, sendo este colocado na pasta **{local do projeto xy-inc-api}/projects**. Se necessário este projeto pode ser rodado sem a dependência do projeto de criação de APIs (**xy-inc-api**). Para isso é necessário apenas executar os seguintes comandos na pasta do projeto **{local do projeto xy-inc-api}/projects/{nome da API}**:

```sh
$ gradle clean build
$ java -jar build/libs/mobile-api-0.0.1-SNAPSHOT.war
```

Uma vantagem de ter um projeto separado é que isto permite a evolução da API, com regras mais elaboradas. Apesar de rodar com um servidor de aplicação embarcado, a API também pode ser colocada no JBoss EAP 7.0 ou Wildfly 10.
