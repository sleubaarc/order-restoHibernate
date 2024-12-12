package ch.hearc.ig.orderresto.business;

import java.math.BigDecimal;
import java.util.HashSet;
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

    @ManyToMany(mappedBy="products", fetch = FetchType.EAGER) // relation many-to-many avec la table COMMANDE
    private Set<Order> orders;

    @ManyToOne (fetch=FetchType.EAGER)  // relation many-to-one avec la table RESTAURANT
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

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        // Si les IDs sont présents, on compare uniquement les IDs
        if (id != null && product.id != null) {
            return id.equals(product.id);
        }

        // Sinon, on compare les attributs métier qui identifient un produit
        return name != null && restaurant != null &&
                name.equals(product.name) &&
                restaurant.equals(product.restaurant);
    }

    @Override
    public int hashCode() {
        // Si l'ID est présent, pas besoin de calcul complexe
        if (id != null) {
            return id.hashCode();
        }

        // Sinon, on calcule avec les attributs métier
        final int prime = 31;
        int result = 1;  // On commence avec 1

        // Pour le nom "Pizza" (imaginons que son hashCode est 12345)
        result = prime * result + (name != null ? name.hashCode() : 0);
        // result = 31 * 1 + 12345 = 31 + 12345 = 12376

        // Pour le restaurant (imaginons que son hashCode est 67890)
        result = prime * result + (restaurant != null ? restaurant.hashCode() : 0);
        // result = 31 * 12376 + 67890 = 383656 + 67890 = 451546

        return result;
    }

}