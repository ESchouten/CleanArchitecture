# Kotlin Clean Architecture Backend
Kotlin backend based on the Clean Architecture principles.

Ktor, JWT, Exposed, Flyway, KGraphQL/GraphQL generated endpoints.

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
