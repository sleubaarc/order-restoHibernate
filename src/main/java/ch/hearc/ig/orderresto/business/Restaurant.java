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

    @OneToMany(mappedBy="restaurant") // relation one-to-many avec la table COMMANDE, il faut indiquer l'attribut dans commande --> en minuscule
    private Set<Order> orders;

    @Embedded // l'adresse existe comme objet mais pas comme table
    private Address address;

    @OneToMany(mappedBy="restaurant") // relation one-to-many avec la table PRODUIT, il faut indiquer l'attribut dans produit --> en minuscule
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant restaurant = (Restaurant) o;

        // Si les IDs sont présents, on compare uniquement les IDs
        if (id != null && restaurant.id != null) {
            return id.equals(restaurant.id);
        }

        // Sinon, on compare le nom et l'adresse
        // Un restaurant est considéré unique par son nom et son adresse
        return name != null && address != null &&
                name.equals(restaurant.name) &&
                address.equals(restaurant.address);
    }

    @Override
    public int hashCode() {
        // Si l'ID est présent, on utilise uniquement l'ID
        if (id != null) {
            return id.hashCode();
        }

        // Sinon, on utilise les mêmes attributs que dans equals()
        final int prime = 31;
        int result = 1;
        result = prime * result + (name != null ? name.hashCode() : 0);
        result = prime * result + (address != null ? address.hashCode() : 0);
        return result;
    }

}