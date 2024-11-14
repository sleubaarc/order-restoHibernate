package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Address;
import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.OrganizationCustomer;
import ch.hearc.ig.orderresto.business.PrivateCustomer;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import java.sql.Connection;
import java.sql.SQLException;

public class CustomerMapper {

    private static AddressMapper addressMapper = new AddressMapper();
    private static final IdentityMap<Customer, Long> cache = new IdentityMap<>();

    public static Customer findByEmail(Connection connection, String email) throws SQLException {

        for (Customer customer : cache.getAll()) {
            if (customer.getEmail().equalsIgnoreCase(email)) {
                return customer;
            }
        }

        String query = "SELECT NUMERO, TELEPHONE, EMAIL, NOM, CODE_POSTAL, LOCALITE, RUE, NUM_RUE, PAYS, EST_UNE_FEMME, PRENOM, FORME_SOCIALE, TYPE FROM CLIENT WHERE EMAIL = ?";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            stmt.setString(1, email);
            OracleResultSet rs = (OracleResultSet) stmt.executeQuery();

            if (rs.next()) {
                return populateCache(rs);
            }
        }
        return null;
    }

    public Customer findById(Connection connection, Long id) throws SQLException {

        if (cache.contains(id)) {
            return cache.get(id);
        }

        String query = "SELECT NUMERO, TELEPHONE, EMAIL, NOM, CODE_POSTAL, LOCALITE, RUE, NUM_RUE, PAYS, EST_UNE_FEMME, PRENOM, FORME_SOCIALE, TYPE FROM CLIENT WHERE NUMERO = ?";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            OracleResultSet rs = (OracleResultSet) stmt.executeQuery();

            if (rs.next()) {
                return populateCache(rs);
            }
        }
        return null;
    }

    private static Customer populateCache(OracleResultSet rs) throws SQLException {
        Long id = rs.getLong("NUMERO");

        if (!cache.contains(id)) {
            String phone = rs.getString("TELEPHONE");
            String email = rs.getString("EMAIL");
            String nom = rs.getString("NOM");
            String prenom = rs.getString("PRENOM");
            Boolean estUneFemme = rs.getBoolean("EST_UNE_FEMME");
            String formeSociale = rs.getString("FORME_SOCIALE");
            String type = rs.getString("TYPE");

            Address address = addressMapper.extractAddressFromResultSet(rs);

            Customer customer;
            if ("P".equalsIgnoreCase(type)) {
                String gender = estUneFemme ? "O" : "N";
                customer = new PrivateCustomer(id, phone, email, address, gender, prenom, nom, type);
            } else if ("O".equalsIgnoreCase(type)) {
                customer = new OrganizationCustomer(id, phone, email, address, nom, formeSociale, type);
            } else {
                return null;
            }

            cache.put(id, customer);
        }

        return cache.get(id);
    }

    public static void insert(Connection connection, Customer customer) throws SQLException {
        String query = "INSERT INTO CLIENT (TELEPHONE, EMAIL, NOM, CODE_POSTAL, LOCALITE, RUE, NUM_RUE, PAYS, EST_UNE_FEMME, PRENOM, FORME_SOCIALE, TYPE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING NUMERO INTO ?";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            stmt.setString(1, customer.getPhone());
            stmt.setString(2, customer.getEmail());

            if (customer instanceof PrivateCustomer) {
                PrivateCustomer privateCustomer = (PrivateCustomer) customer;
                stmt.setString(3, privateCustomer.getLastName());
                stmt.setString(9, privateCustomer.getGender().equalsIgnoreCase("O") ? "O" : "N");
                stmt.setString(10, privateCustomer.getFirstName());
                stmt.setNull(11, OracleTypes.VARCHAR);  // Pas de forme sociale
                stmt.setString(12, "P");
            } else if (customer instanceof OrganizationCustomer) {
                OrganizationCustomer organizationCustomer = (OrganizationCustomer) customer;
                stmt.setString(3, organizationCustomer.getName());
                stmt.setNull(9, OracleTypes.VARCHAR);  // Pas d'information sur le genre
                stmt.setNull(10, OracleTypes.VARCHAR);  // Pas de prénom
                stmt.setString(11, organizationCustomer.getLegalForm());
                stmt.setString(12, "O");
            }

            addressMapper.populatePreparedStatementWithAddress(customer.getAddress(), stmt, 4);

            stmt.registerReturnParameter(13, OracleTypes.NUMBER);
            stmt.executeUpdate();

            try (OracleResultSet rs = (OracleResultSet) stmt.getReturnResultSet()) {
                if (rs.next()) {
                    customer.setId(rs.getLong(1));
                    cache.put(customer.getId(), customer);
                }
            }
        } catch (SQLException e) {
            System.err.println("ERREUR: Échec de l'insertion pour le client : " + e.getMessage());
            throw e;
        }
    }
}
