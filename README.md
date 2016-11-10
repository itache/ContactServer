##ContactServer

REST Service that return contacts from DB, exclude those that match the regular expression.

### Launch

#### Database initialization
Service based on Spring Boot and PostgreSQL. For connection to DB you need to specify next configuration properties in \src\main\resources\application.properties
spring.datasource.url=jdbc:postgresql://{host}:{port}/{dbname}
spring.datasource.username={dbuser}
spring.datasource.password={dbpass}

Spring Boot can load SQL from the standard locations schema.sql and data.sql. schema.sql contains Contact table creation SQL, data.sql - one million randomly generated contacts.
To initialize DB set next configuration property in application.properties
spring.datasource.initialize=true

####Running
$ mvn spring-boot:run

###Usage


