package ch.hearc.ig.orderresto.business;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

@Entity // La classe est une entité persistante qui est mappée à une table de la DB
@Table(name="PRODUIT") // la table de la DB où est mappée l'entité s'appelle "PRODUIT"
public class Product {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE, // Utilise une séquence pour générer les valeurs
            generator = "seq_produit"        // Associe l'ID au générateur nommé "seq_restaurant"
    )
    @SequenceGenerator(
            name = "seq_produit",            // Nom du générateur
            sequenceName = "SEQ_PRODUIT",    // Nom de la séquence dans la base de données
            allocationSize = 1                 // Taille d'allocation définie à 1 pour correspondre à la séquence dans la base
    )
    @Column(name = "NUMERO")
    private Long id;

    @Column(name="NOM") // attribut mappé sur la colonne NOM
    private String name;

    @Column(name="PRIX_UNITAIRE") // attribut mappé sur la colonne PRIX_UNITAIRE
    private BigDecimal unitPrice;

    @Column(name="DESCRIPTION") // attribut mappé sur la colonne DESCRIPTION
    private String description;

    @ManyToMany(mappedBy="products", fetch = FetchType.LAZY) // relation many-to-many avec la table COMMANDE
    private Set<Order> orders;

    @ManyToOne (fetch=FetchType.LAZY)  // relation many-to-one avec la table RESTAURANT
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

    public Product() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != null && product.id != null) {
            return id.equals(product.id);
        }

        return name != null && restaurant != null &&
                name.equals(product.name) &&
                restaurant.equals(product.restaurant);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(name, restaurant);
    }
}