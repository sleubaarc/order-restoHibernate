package ch.hearc.ig.orderresto.business;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity // La classe est une entité persistante qui est mappée à une table de la DB
@Table(name="RESTAURANT") // la table de la DB où est mappée l'entité s'appelle "RESTAURANT"
public class Restaurant {

    @Id // id est la clé primaire de la table
    @GeneratedValue(
            strategy=GenerationType.SEQUENCE, // la valeur de id est générée par une séquence
            generator="SEQ_RESTAURANT" // nom de la séquence
    )
    private Long id;
    @Column(name="NOM") // attribut mappé sur la colonne NOM
    private String name;
    @OneToMany(mappedBy="RESTAURANT") // relation one-to-many avec la table COMMANDE
    private Set<Order> orders;
    @Embedded // l'adresse existe comme objet mais pas comme table
    private Address address;
    @OneToMany(mappedBy="RESTAURANT") // relation one-to-many avec la table PRODUIT
    private Set<Product> productsCatalog;

    public Restaurant(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.orders = new HashSet<>();
        this.productsCatalog = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Product> getProductsCatalog() {
        return productsCatalog;
    }

    public void registerProduct(Product p) {
        if (p.getRestaurant() != this) {
            throw new RuntimeException("Restaurant mismatch!");
        }
        this.productsCatalog.add(p);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}