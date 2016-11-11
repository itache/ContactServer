##ContactServer

REST Service that return contacts from DB, exclude those that match the regular expression.

### Launch

#### Database initialization
Service based on Spring Boot and PostgreSQL. For connection to DB you need to specify next configuration properties in \src\main\resources\application-dev.properties:

**spring.datasource.url**=**jdbc:postgresql://{host}:{port}/{dbname}**  
**spring.datasource.username**=**{dbuser}**
**spring.datasource.password**=**{dbpass}**

Spring Boot can load SQL from the standard locations schema.sql and data.sql. 
schema-postgresql.sql contains Contact table creation SQL, data-postgresql.sql - one million randomly generated contacts.
To initialize DB set next configuration property in application-dev.properties:

**spring.datasource.initialize**=**true**

####Running
$ mvn spring-boot:run

###Usage

####Request examples
/hello/contacts?nameFilter=^A.*$ - returns all contacts that NOT start with A

/hello/contacts?nameFilter=^.*[aei].*$ - returns all contacts that NOT contain a, e, i

####Response
Contact array in json fornat

{
	contacts: [ Contact, ..... ],
	link: [next, self, previous]
}


Contact
{
	“id”: integer,
 	“name”: string
}

link
{
	“rel” : request link to rel
}

