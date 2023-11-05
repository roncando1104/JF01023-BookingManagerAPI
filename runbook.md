#### Stories
* Please refer CHANGELOG.md file for the changes being deployed

#### Packages

| Package           | Old Version | New Version |
|-------------------|-------------|-------------|
| BookingManagerAPI | 1.0.1       | 1.0.2       |

#### Git Repository
* Go to: https://github.com/roncando1104/JF01023-BookingManagerAPI
* username: roncando1104
* access token: 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

* command: git clone https://github.com/roncando1104/JF01023-BookingManagerAPI.git

#### Database
* PostgreSQL
  * url: jdbc:postgresql://localhost:5432/postgres
  * username: postgres
  * password: ron1104
* H2 Database (for Unit Testing)
  * url: jdbc:h2:mem:testdb
  * username: sa
  * password: password

#### Swagger Open API
* API Definition: http://localhost:8080/api-docs (add .yaml to download as YAML file)
* Swagger UI: http://localhost:8080/swagger-ui/index.html (then type /api-docs in Explore field)
* Other URL: http://localhost:8080/swagger-ui-custom.html

#### Spring Security and JWT Authentication
* To perform an action, sign in first and get the token
* Use the token as an Authorization with Bearer Token
* Then perform the transaction
* NOTE: Token is only good for 3 hours