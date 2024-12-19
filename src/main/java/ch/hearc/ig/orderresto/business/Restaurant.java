package ch.hearc.ig.orderresto.business;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

@Entity // La classe est une entité persistante qui est mappée à une table de la DB
@Table(name="RESTAURANT") // la table de la DB où est mappée l'entité s'appelle "RESTAURANT"
public class Restaurant {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE, // Utilise une séquence pour générer les valeurs
            generator = "seq_restaurant"        // Associe l'ID au générateur nommé "seq_restaurant"
    )
    @SequenceGenerator(
            name = "seq_restaurant",            // Nom du générateur
            sequenceName = "SEQ_RESTAURANT",    // Nom de la séquence dans la base de données
            allocationSize = 1                 // Taille d'allocation définie à 1 pour correspondre à la séquence dans la base
    )
    @Column(name = "NUMERO")
    private Long id;

    @Column(name="NOM") // attribut mappé sur la colonne NOM
    private String name;

    @OneToMany(mappedBy="restaurant", fetch = FetchType.LAZY) // relation one-to-many avec la table COMMANDE, il faut indiquer l'attribut dans commande --> en minuscule
    private Set<Order> orders;

    @Embedded // l'adresse existe comme objet mais pas comme table
    private Address address;

    @OneToMany(mappedBy="restaurant", fetch = FetchType.LAZY) // relation one-to-many avec la table PRODUIT, il faut indiquer l'attribut dans produit --> en minuscule
    private Set<Product> productsCatalog;

    public Restaurant(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.orders = new HashSet<>();
        this.productsCatalog = new HashSet<>();
    }

    public Restaurant() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant restaurant = (Restaurant) o;

        if (id != null && restaurant.id != null) {
            return id.equals(restaurant.id);
        }

        return name != null && address != null &&
                name.equals(restaurant.name) &&
                address.equals(restaurant.address);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(name, address);
    }
}