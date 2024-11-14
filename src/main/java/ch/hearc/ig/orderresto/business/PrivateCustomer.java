package ch.hearc.ig.orderresto.business;

public class PrivateCustomer extends Customer {

    private String gender;
    private String firstName;
    private String lastName;

    public PrivateCustomer(Long id, String phone, String email, Address address, String gender, String firstName, String lastName, String type) {
        super(id, phone, email, address, type);
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}