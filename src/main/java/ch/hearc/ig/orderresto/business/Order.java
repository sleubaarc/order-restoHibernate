package ch.hearc.ig.orderresto.business;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity // La classe est une entité persistante qui est mappée à une table de la DB
@Table(name="COMMANDE") // la table de la DB où est mappée l'entité s'appelle "COMMANDE"
public class Order {
    @Id // id est la clé primaire de la table
    @GeneratedValue(
            strategy=GenerationType.SEQUENCE, // la valeur de id est générée par une séquence
            generator="SEQ_COMMANDE" // nom de la séquence
    )
    private Long id;

    @ManyToOne // relation many-to-one avec la table CLIENT
    @JoinColumn(name="FK_CLIENT", nullable=false) // clé étrangère vers la table CLIENT
    private Customer customer;

    @ManyToOne // relation many-to-one avec la table RESTAURANT
    @JoinColumn(name="FK_RESTO", nullable=false) // clé étrangère vers la table RESTAURANT
    private Restaurant restaurant;

    @ManyToMany // relation many-to-many avec la table PRODUIT
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
        return totalAmount;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        this.totalAmount = this.totalAmount.add(product.getUnitPrice());
    }
}