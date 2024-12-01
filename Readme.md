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

##ToDo
* choose which Data structure to use for each ...toMany relationship
* annotate the abstract Customer class
* annotate the PrivateCustomer class
* annotate the OrganizationCustomer class
* modify the service classes to use the annotations
* migrate the DataMappers SQL requests to Hibernate annotated service classes
* remove the mo-more-used DataMappers classes
* test the persistence
* remove the ConnectionDb class




