package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.*;
import ch.hearc.ig.orderresto.persistence.CustomerMapper;

import java.sql.Connection;

public class CustomerService {

    public Customer findCustomerByEmail(String email) {
        try (Connection connection = ConnectionDb.getInstance().getConnection()) {
            return CustomerMapper.findByEmail(connection, email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            ConnectionDb.getInstance().releaseConnection();
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
        try (Connection connection = ConnectionDb.getInstance().getConnection()) {
            CustomerMapper.insert(connection, customer);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try (Connection connection = ConnectionDb.getInstance().getConnection()) {
                connection.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            ConnectionDb.getInstance().releaseConnection();
        }
    }

    public String getCustomerEmail(Customer customer) {
        return customer.getEmail();
    }
}
