# spring-boot-starter
spring-boot 演習

- プロジェクト作成
mvn -B archetype:generate -DgroupId=com.exmple -DartifactId=hajiboot-rayering -Dversion=1.0.0-SNAPSHOT -DarchetypeArtifactId=maven-archetype-quickstart

- 3.2.2 rest API
  - GET
    ```
    curl http://localhost:8080/api/customers -v -X GET
    ```
    ```
    curl http://localhost:8080/api/customers/1 -v -X GET
    ```
  - POST
    ```
    curl http://localhost:8080/api/customers -v -X POST -H "Content-Type: application/json" -d "{\"firstName\" : \"Tamako\", \"lastName\" :  \"Nobi\"}"
    ```
  - PUT
    ```
    curl http://localhost:8080/api/customers/1 -v -X PUT -H "Content-Type: application/json" -d "{\"firstName\" : \"Nobio\", \"lastName\" :  \"Nobi\"}"
    ```
  - DELETE
    ```
    curl http://localhost:8080/api/customers/5 -v -X DELETE
    ```