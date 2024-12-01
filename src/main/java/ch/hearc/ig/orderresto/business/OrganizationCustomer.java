package ch.hearc.ig.orderresto.business;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("O")
public class OrganizationCustomer extends Customer {
    @Column(name = "NOM")
    private String name;

    @Column(name = "FORME_SOCIALE")
    private String legalForm;

    public OrganizationCustomer() {
        super();
    }

    public OrganizationCustomer(Long id, String phone, String email, Address address, String name, String legalForm, String type) {
        super(id, phone, email, address, type);
        this.name = name;
        this.legalForm = legalForm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }
}