### BOOK Application
Spring Boot Api project

Technologies used
 - Spring Framework 6.1.13
 - Spring Boot 3.3.4
 - Spring Security 6.3.3
 - Maven 3.9.6
 - MySQL 8.3.0
 - jdk 23
 
## Application.yml

```
server:
  port: 8080
  
  error:
    include-stacktrace: never

  tomcat:
    max-http-form-post-size: 5000000
  
## Não colocar espaço depois da virgula entre os domínios
cors:
  originPatterns: http://localhost:3000,http://localhost:8080,https://suaaplicacao
file:
  upload-dir: /Code/UploadDir

## TOKEN
security:
  jwt:  
    token:
      secret-key: 53cr37
      expire-length: 3600000

spring:
  application:
    name: suaaplicacao  
  servlet:
    multipart:
      enabled: true
      file-size-threshold: 2KB
      max-file-size: 200MB
      max-request-size: 215MB

## THYMELEAF  
  thymeleaf:
    cache: false
    check-template-location: false
    prefix: classpath:/templates/
  
# FREEMARKER   
  freemarker:
    check-template-location: false # Check that the templates location exists.
    template-loader-path: classpath:/templates/
    settings:
      locale: pt_BR
  
  main:
    allow-circular-references: true
    allow-bean-definition-overriding: true
    lazy-initialization: true
  
  mvc:      
    media-types: application/json
    content-negotiation:
      favor-path-extension: false
      favor-parameter: false
      favor-parameter-parameter-name: mediaType
      ignore-accept-header: true
      default-content-type: application/json
    converters:
      preferred-json-mapper: jackson

# JACKSON 
  jackson:
    serialization:
      write-dates-as-timestamps: false
    deserialization:
      fail-on-unknown-properties: false
    time-zone: UTC

  messages:
    basename: ValidationMessages

  datasource:
    url: jdbc:mysql://localhost:3306/book-db?allowPublicKeyRetrieval=true&useSSL=false&useTimezone=true&serverTimezone=UTC&createDatabaseIfNotExist=true
    username: root
    password: suasenha
  flyway:
    enabled: true   
    validate-on-migrate: true
    url: jdbc:mysql://localhost:3306/book-db?allowPublicKeyRetrieval=true&useSSL=false&useTimezone=true&serverTimezone=UTC&createDatabaseIfNotExist=true
    user: root
    password: suasenha
    locations: classpath:db/
    baseline-on-migrate: true
    clean-on-validation-error: true
  jpa:
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
      ddl-auto: none
    show-sql: false
    open-in-view: false

#Email
  mail:
    host: smtp.mailgun.org
    port: 587
    username: postmaster@sandbox671166aa8eb44f39b0b179a67f691aee.mailgun.org
    password: e8781c162c8b39ed38aeef12e123713f-135a8d32-2b84c50d
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true 
      
com:
  nofrontier:    
     
# Google Matrix API
    googleMatrix:
      apiKey: YOUR_GOOGLE_MATRIX_KEY
        
# Payme
    payme:
      apiKey: YOUR_API_KEY

# AWS S3
    s3:
      accessKey: YOUR_S3_ACCESS
      secretKey: YOUR_S3_SECRET
      bucket: YOUR_S3_BUCKET
      region: YOUR_S3_REGION
      
# HOST
    backend:
      host: http://localhost:8080
    frontend:
      host: http://localhost:3000

# Email
    email:
      impl: fake
      sender: suaaplicação <naoresponder@suaaplicacao.com.br>
 
springdoc:
  pathsToMatch: 
    - /auth/**
    - /api/**/v1/**
  swagger-ui:
    use-root-path: true

logging:
  level:
    org:
      springframework: INFO
      web: DEBUG
      flywaydb: DEBUG
    hibernate:
      SQL: DEBUG
      type: TRACE
    com:
      nofrontier:
        book: DEBUG
        security: DEBUG
```