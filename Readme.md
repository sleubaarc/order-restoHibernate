# RestoOrder (Hibernate version)

A simple application to order food from a restaurant and persist the data in an Oracle database using framework Hibernate

## Usage
Launch application and follow the instructions.

## Done
* edited the resources/META-INF/persistence.xml file
* edited the pom.xml file
* fully annotated the Product class
* fully annotated the Restaurant class
* fully annotated the Order class
* annnotated (@Embeddable) the Address class and use itin the Restaurant and Customer classes
* remove Mappers
* remove ConnectionDb
* migrate the DataMappers SQL requests to Hibernate annotated service classes
* annotate the abstract Customer class
* annotate the PrivateCustomer class
* annotate the OrganizationCustomer class
* modify the service classes to use the annotations
* test the persistence
* Correct methode in OrderService to format order and display order and not bug

##ToDo

* choose FetchType on every relation OneTo.. et ManyTo...
* Optimise SELECT query to avoir multiple call to DB
* Add DAO in persistence to move methode from service to persistence, shall we do interface, abstract, implmentation for DAO?
