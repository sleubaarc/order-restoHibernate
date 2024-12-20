package ch.hearc.ig.orderresto.business;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity // La classe est une entité persistante qui est mappée à une table de la DB
@Table(name="COMMANDE") // la table de la DB où est mappée l'entité s'appelle "COMMANDE"
public class Order {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE, // Utilise une séquence pour générer les valeurs
            generator = "seq_commande"        // Associe l'ID au générateur nommé "seq_restaurant"
    )
    @SequenceGenerator(
            name = "seq_commande",            // Nom du générateur
            sequenceName = "SEQ_COMMANDE",    // Nom de la séquence dans la base de données
            allocationSize = 1                 // Taille d'allocation définie à 1 pour correspondre à la séquence dans la base
    )
    @Column(name = "NUMERO")
    private Long id;


    @ManyToOne (fetch = FetchType.LAZY) // relation many-to-one avec la table CLIENT
    @JoinColumn(name="FK_CLIENT", nullable=false) // clé étrangère vers la table CLIENT
    private Customer customer;

    @ManyToOne (fetch = FetchType.LAZY) // relation many-to-one avec la table RESTAURANT
    @JoinColumn(name="FK_RESTO", nullable=false) // clé étrangère vers la table RESTAURANT
    private Restaurant restaurant;

    @ManyToMany (fetch = FetchType.LAZY) // relation many-to-many avec la table PRODUIT
    @JoinTable(
            name="PRODUIT_COMMANDE", // table associative
            joinColumns=@JoinColumn(name="FK_COMMANDE"), // clé étrangère vers la table COMMANDE
            inverseJoinColumns=@JoinColumn(name="FK_PRODUIT") // clé étrangère vers la table PRODUIT
    )
    private Set<Product> products;

    @Column(name="A_EMPORTER")
    private String takeAway;

    @Column(name="QUAND")
    private LocalDateTime when;

    // On ne mappe pas le totalAmount car il est calculé à partir des produits
    @Transient
    private BigDecimal totalAmount;

    public Order(Long id, Customer customer, Restaurant restaurant, String takeAway, LocalDateTime when) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
        this.products = new HashSet<>();
        this.takeAway = takeAway;
        this.totalAmount = new BigDecimal(0);
        this.when = when;
    }

    public Order() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public String getTakeAway() {
        return takeAway;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public BigDecimal getTotalAmount() {
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
            for (Product product : products) {
                totalAmount = totalAmount.add(product.getUnitPrice());
            }
        }
        return totalAmount;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        this.totalAmount = this.totalAmount.add(product.getUnitPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != null && order.id != null) {
            return id.equals(order.id);
        }

        return customer != null && restaurant != null && when != null &&
                customer.equals(order.customer) &&
                restaurant.equals(order.restaurant) &&
                when.equals(order.when);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }

        return Objects.hash(customer, restaurant, when);
    }
}