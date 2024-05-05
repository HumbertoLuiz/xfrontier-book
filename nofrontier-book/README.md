### BOOK Application
Spring Boot Api project

Technologies used
 - Spring Framework 6.1.6
 - Spring Boot 3.2.5
 - Spring Security 6.2.4
 - Maven 3.9.6
 - MySQL 8.3.0
 
Application.yml
```
server:
  port: 8080
  
#não colocar espaço depois da virgula entre os domínios
cors:
  originPatterns: http://localhost:3000,http://localhost:8080,https://seudominio.com.br

spring:
  application:
    name: **************  
  
  main:
    allow-bean-definition-overriding: true
    lazy-initialization: true
  
  datasource:
    driverClassName: com.mysql.cj.jdbc.Driver   
    url: jdbc:mysql://localhost:3306/book-db?allowPublicKeyRetrieval=true&useSSL=false&useTimezone=true&serverTimezone=UTC&createDatabaseIfNotExist=true
    username: root
    password: **************

  jpa:
    hibernate:
      ddl-auto: none
    properties:
      databasePlataform: org.hibernate.dialect.MySQLDialect
    show-sql: false
    open-in-view: false
    
# Flyway Configs
  flyway:
    enabled: true
    validate-on-migrate: true
    url: jdbc:mysql://localhost/book-db?createDatabaseIfNotExist=true&serverTimezone=UTC
    schemas: book-db
    user: root
    password: **************
    locations: classpath:db/migration

# Jackson Configs
  jackson:
    default-property-inclusion: NON_NULL


# Tokens
    access:
      key: ***********************************************************************
      expiration: 604800
    refresh:
      key: **************
      expiration: 9000000
        
springdoc:
  pathsToMatch: 
    - /auth/**
    - /api/**/v1/**
  swagger-ui:
    use-root-path: true

logging:
  level:
    org:
      springframework:
        security: DEBUG
```