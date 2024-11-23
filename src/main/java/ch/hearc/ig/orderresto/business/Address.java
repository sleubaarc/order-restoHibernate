package ch.hearc.ig.orderresto.business;

import javax.persistence.Embeddable;
import javax.persistence.Column;

@Embeddable // l'adresse existe comme objet mais pas comme table
public class Address {
    @Column (name="PAYS") // attribut mappé sur la colonne PAYS
    private String countryCode;
    @Column (name="CODE_POSTAL") // attribut mappé sur la colonne CODE_POSTAL
    private String postalCode;
    @Column (name="LOCALITE") // attribut mappé sur la colonne LOCALITE
    private String locality;
    @Column (name="RUE") // attribut mappé sur la colonne RUE
    private String street;
    @Column (name="NUM_RUE") // attribut mappé sur la colonne NUMERO
    private String streetNumber;

    public Address(String countryCode, String postalCode, String locality, String street, String streetNumber) {
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.locality = locality;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getLocality() {
        return locality;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }
}