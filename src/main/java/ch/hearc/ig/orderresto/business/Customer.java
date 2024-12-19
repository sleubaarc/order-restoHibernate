package ch.hearc.ig.orderresto.business;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "CLIENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 1)
public abstract class Customer {
   @Id
   @Column(name = "NUMERO")
   @GeneratedValue(
            strategy = GenerationType.SEQUENCE, // Utilise une séquence pour générer les valeurs
            generator = "seq_client"        // Associe l'ID au générateur nommé "seq_client"
    )
    @SequenceGenerator(
            name = "seq_client",            // Nom du générateur
            sequenceName = "SEQ_CLIENT",    // Nom de la séquence dans la base de données
            allocationSize = 1                 // Taille d'allocation définie à 1 pour correspondre à la séquence dans la base
    )
    private Long id;

    @Column(name = "TELEPHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();

    @Embedded
    private Address address;

    @Column(name = "TYPE", insertable = false, updatable = false)
    private String type;

    protected Customer() {}

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