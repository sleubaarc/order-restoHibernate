package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.*;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerService {

    public Customer findCustomerByEmail(String email) {
        EntityManager em = null;
        try {
            em = JpaUtils.getEntityManager();
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c WHERE c.email = :email", Customer.class);
            query.setParameter("email", email);

            List<Customer> customers = query.getResultList();
            return customers.isEmpty() ? null : customers.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            JpaUtils.closeEntityManager();
        }
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

    private void insertCustomer(Customer customer) {
        EntityManager em = null;
        try {
            em = JpaUtils.getEntityManager();
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            JpaUtils.closeEntityManager();
        }
    }

    public String getCustomerEmail(Customer customer) {
        return customer.getEmail();
    }
}