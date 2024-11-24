package ch.hearc.ig.orderresto.business;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="CLIENT")
public abstract class Customer {

    @Id // id est la clé primaire de la table
    @GeneratedValue(
            strategy=GenerationType.SEQUENCE, // la valeur de id est générée par une séquence
            generator="SEQ_CLIENT" // nom de la séquence
    )
    private Long id;

    @Column(name="TELEPHONE") // attribut mappé sur la colonne TELEPHONE
    private String phone;

    @Column(name="EMAIL") // attribut mappé sur la colonne EMAIL
    private String email;

    @OneToMany(mappedBy="customer")
    private Set<Order> orders;

    @Embedded // l'adresse existe comme objet mais pas comme table
    private Address address;

    @Column(name="TYPE") // attribut mappé sur la colonne TYPE
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