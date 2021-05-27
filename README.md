# Kotlin Clean Architecture Backend
Kotlin backend based on the Clean Architecture principles.

The application is separated into three modules: Domain, Usecases and Adapters
- Domain module contains all entities, it's validation and repository interfaces
- Usecases module performs actions on the domain entities and repositories and does authorization

The domain and usecases modules do not have any external dependencies.

- Adapter layer: each adapter is implemented as a standalone module, lowering dependence on specific frameworks and libraries and making them interchangable. The server module consumes all adapters (e.g. databases, (graphql) endpoints, authentication logic)

GraphQL endpoints are auto-generated from the Usecases

##### Technologies:
Ktor, JWT, Exposed, Flyway, KGraphQL/GraphQL generated endpoints, Gradle.

![image](https://user-images.githubusercontent.com/9130193/119733737-3afed980-be7a-11eb-8970-3891fc2632f1.png)

## GraphQL Plaground
Default url: http://localhost:8080/graphql

### Login
```
query Login {
  LoginUser(a0: { email: "erik@erikschouten.com", password: "P@ssw0rd!" })
}
```
### Retrieve current user
```
query CurrentUser {
  AuthenticatedUser {
    id
    email
    authorities
  }
}
```
##### HTTP Headers
```
{
  "Authorization": "Bearer [TOKEN FROM LOGIN]"
}
```
### Create new user
```
mutation CreateUser {
  CreateUser(
    a0: {
      email: "calvin@erikschouten.com"
      password: "Penait1!"
      authorities: [USER]
    }
  ) {
    id
    email
    authorities
  }
}
```
##### HTTP Headers
```
{
  "Authorization": "Bearer [TOKEN FROM LOGIN]"
}
```
