package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Address;
import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.service.CustomerService;

public class CustomerCLI extends AbstractCLI {

    private final CustomerService customerService = new CustomerService();

    public Customer displayExistingCustomer() {
        this.ln("Quelle est votre adresse email?");
        String email = this.readEmailFromUser();

        Customer customer = customerService.findCustomerByEmail(email);
        if (customer == null) {
            throw new NullPointerException("Aucune client trouvé avec cet email, annulation du traitement");
        }
        return customer;
    }

    public Customer enterNewCustomer() {
        this.ln("Êtes-vous un client privé ou une organisation?");
        this.ln("0. Annuler");
        this.ln("1. Un client privé");
        this.ln("2. Une organisation");
        int customerTypeChoice = this.readIntFromUser(2);
        if (customerTypeChoice == 0) {
            throw new NullPointerException("annulation de la commande et de la création du client ");
            }

        this.ln("Quelle est votre adresse email?");
        String email = this.readEmailFromUser();

        this.ln("Quelle est votre numéro de téléphone?");
        String phone = this.readStringFromUser();

        Address address = (new AddressCLI()).displayNewAddress();

        Customer newCustomer;
        if (customerTypeChoice == 1) {
            this.ln("Êtes-vous un homme ou une femme (H/F)?");
            String gender = this.readChoicesFromUser(new String[]{"H", "F"}).equals("H") ? "N" : "O";
            this.ln("Quel est votre prénom?");
            String firstName = this.readStringFromUser();
            this.ln("Quel est votre nom?");
            String lastName = this.readStringFromUser();

            newCustomer = customerService.createPrivateCustomer(email, phone, address, gender, firstName, lastName);
        } else {
            this.ln("Quel est le nom de votre organisation?");
            String name = this.readStringFromUser();
            this.ln(String.format("%s est une société anonyme (SA)?, une association (A) ou une fondation (F)?", name));
            String legalForm = this.readChoicesFromUser(new String[]{"SA", "A", "F"});

            newCustomer = customerService.createOrganizationCustomer(email, phone, address, name, legalForm);
        }

        this.ln("Le client a été créé avec succès.");
        return newCustomer;
    }
}
