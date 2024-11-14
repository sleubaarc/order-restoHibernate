package ch.hearc.ig.orderresto.business;

import java.util.HashSet;
import java.util.Set;

public abstract class Customer {

    private Long id;
    private String phone;
    private String email;
    private Set<Order> orders;
    private Address address;
    private String type;

    protected Customer(Long id, String phone, String email, Address address, String type) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.orders = new HashSet<>();
        this.address = address;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

}