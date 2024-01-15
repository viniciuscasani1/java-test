# Java Test

Projeto de desafio teste

## Requisitos
* Possuir o Java e o maven instalados
* Possuir um banco MySql executando, podendo ser instalação direta ou um container docker
    * O mesmo deve conter um database chamado ``teste``, as demais configurações de acesso do banco podem ser consultadas no ``applications.properties``
* Importar o projeto na sua IDE de preferência

## Build

* ### Build
    * Rodar o comando  ``mvn clean install``
* ### Testes
    * Rodar o comando  ``mvn clean install``

## Executando
* ### Backend
    * Acessar a classe ```DemoApplication.java``` e executar a mesma
    * A documentação swagger pode ser acessada em http://localhost:8080/swagger-ui.html#/
    * Já existe um usuário ADMIN criado, o mesmo deve ser usado para criar os demais users
      * username: user-root
      * password: senha_hash
    * As collections pra teste se encontram na pasta resources, que fica na raiz do projeto


