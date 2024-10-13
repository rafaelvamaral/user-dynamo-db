# user-dynamo-db
Api para CRUD usando o dynamo com testcontainers

![technology java](https://img.shields.io/badge/technology-Java-purple.svg)
![technology Gradle](https://img.shields.io/badge/technology-Gradle-blue.svg)
![technology Testcontainers](https://img.shields.io/badge/technology-Testcontainers-green)


- ###  Pré-requisitos
    - [**Java 21**](https://www.oracle.com/java/technologies/downloads/#java21)
    - [**Gradle**](https://docs.gradle.org/current/userguide/userguide.html) | _or use the wrapper ./gradlew_
    - [**Spring Boot 3**](https://spring.io/projects/spring-boot)
    - [**AWS Credentials**](https://docs.aws.amazon.com/cli/v1/userguide/cli-configure-files.html)
  
## Estrutura do Projeto

Simples implementação de CRUD utilizando como banco de dados o dynamoDB. Utiliza testcontainers para os testes de integração.

## Arquitetura

O projeto apresenta a arquitetura simples MVC, seguindo o controller, service, repository pattern.

## Execução


### Instalando dependências

````
./gradlew clean build
````

### Rodando os testes

```
./gradlew clean test
```

### Via IDE

Em `Run/Debug Configuration`, crie uma configuração Gradle e define conforme as opções abaixo:

Gradle project

```
user-dynamo-db
```

Task

```
bootRun
```

### Executando os testes

Execute o comando abaixo para executar os testes da aplicação.

```./gradlew clean test```


## Documentação da API <br>

- UI: http://localhost:8080/user-dynamo/swagger-ui/index.html
- Openapi 3.0: http://localhost:8080/user-dynamo/v3/api-docs

## Estilo de código

Seguir o [style guide de Java do Google](https://google.github.io/styleguide/javaguide.html).
Siga [estas instruções](https://github.com/HPI-Information-Systems/Metanome/wiki/Installing-the-google-styleguide-settings-in-intellij-and-eclipse) para configurá-lo no IntelliJ.