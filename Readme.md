# RestoOrder

A simple application to order food from a restaurant and persist the data in an Oracle database

## Usage
Launch application and follow the instructions.

## Done
* created DataMappers for all business classes
* created IdentityMap
* delete unused methods
* delete unused classes (the old ones from the original project)
* delete unused comments
* delete unused imports
* identity Map class
* corrected method createNewOrder if email is not valid
* corrected method createCustomer if choice = 0
* cleaned OrderCli and OrderService and OrderMapper
* set autocommit to false in the connection class
* commit or rollback transactions in the service layer
* disconnect from db after transactions!
* Check Customer enterNewCustomer() to avoid error
* Check public List<Order> getOrdersByCustomer(Customer customer) to avoid error
* Add Try/Catch in Service + try/catch in Mapper for connection and give the connection from service to mapper
* Add Commit and Rollback in Service layer




