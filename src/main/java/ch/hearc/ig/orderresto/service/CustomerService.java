package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.*;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerService {

    public Customer findCustomerByEmail(String email) {
        return JpaUtils.inTransaction(em -> {
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c " +
                            "LEFT JOIN FETCH c.orders " +
                            "WHERE c.email = :email", Customer.class);
            query.setParameter("email", email);
            List<Customer> customers = query.getResultList();
            return customers.isEmpty() ? null : customers.get(0);
        });
    }

    public Customer createPrivateCustomer(String email, String phone, Address address, String gender, String firstName, String lastName) {
        PrivateCustomer newCustomer = new PrivateCustomer(null, phone, email, address, gender, firstName, lastName, "P");
        insertCustomer(newCustomer);
        return newCustomer;
    }

    public Customer createOrganizationCustomer(String email, String phone, Address address, String name, String legalForm) {
        OrganizationCustomer newCustomer = new OrganizationCustomer(null, phone, email, address, name, legalForm, "O");
        insertCustomer(newCustomer);
        return newCustomer;
    }

    private Customer insertCustomer(Customer customer) {
        return JpaUtils.inTransaction((em) -> {
            em.persist(customer);
            return customer;
        });
    }

    public String getCustomerEmail(Customer customer) {
        return customer.getEmail();
    }
}