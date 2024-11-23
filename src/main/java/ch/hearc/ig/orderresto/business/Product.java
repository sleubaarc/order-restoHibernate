package ch.hearc.ig.orderresto.business;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity // La classe est une entité persistante qui est mappée à une table de la DB
@Table(name="PRODUIT") // la table de la DB où est mappée l'entité s'appelle "PRODUIT"
public class Product {
    @Id // id est la clé primaire de la table
    @GeneratedValue(
            strategy=GenerationType.SEQUENCE, // la valeur de id est générée par une séquence
            generator="SEQ_PRODUIT" // nom de la séquence
    )
    private Long id;
    @Column(name="NOM") // attribut mappé sur la colonne NOM
    private String name;
    @Column(name="PRIX_UNITAIRE") // attribut mappé sur la colonne PRIX_UNITAIRE
    private BigDecimal unitPrice;
    @Column(name="DESCRIPTION") // attribut mappé sur la colonne DESCRIPTION
    private String description;
    @ManyToMany(mappedBy="products") // relation many-to-many avec la table COMMANDE
    private Set<Order> orders;
    @ManyToOne  // relation many-to-one avec la table RESTAURANT
    @JoinColumn(name="FK_RESTO", nullable=false) // clé étrangère vers la table RESTAURANT
    private Restaurant restaurant;

    public Product(Long id, String name, BigDecimal unitPrice, String description, Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.description = description;
        this.orders = new HashSet<>();
        this.restaurant = restaurant;
        restaurant.registerProduct(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public String toString() {
        return String.format(
                "%s - %.2f de chez %s: %s",
                this.getName(),
                this.getUnitPrice(),
                this.getRestaurant().getName(),
                this.getDescription()
        );
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}