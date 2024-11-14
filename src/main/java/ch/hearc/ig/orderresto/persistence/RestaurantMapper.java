package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.business.Address;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import java.sql.Connection;
import java.sql.SQLException;

public class RestaurantMapper {

    private static final IdentityMap<Restaurant, Long> cache = new IdentityMap<>();

    public Restaurant findById(Connection connection, Long id) throws SQLException {
        if (cache.contains(id)) {
            return cache.get(id);
        }

        String query = "SELECT NUMERO, NOM, CODE_POSTAL, LOCALITE, RUE, NUM_RUE, PAYS FROM RESTAURANT WHERE NUMERO = ?";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            OracleResultSet rs = (OracleResultSet) stmt.executeQuery();

            if (rs.next()) {
                return populateCache(rs);
            }
        }
        return null;
    }

    public Restaurant[] findRestaurants(Connection connection) throws SQLException {
        String query = "SELECT NUMERO, NOM, CODE_POSTAL, LOCALITE, RUE, NUM_RUE, PAYS FROM RESTAURANT";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            OracleResultSet rs = (OracleResultSet) stmt.executeQuery();

            while (rs.next()) {
                populateCache(rs);
            }

            return cache.getAll().toArray(new Restaurant[0]);
        }
    }

    private Restaurant populateCache(OracleResultSet rs) throws SQLException {
        Long id = rs.getLong("NUMERO");

        if (!cache.contains(id)) {
            String nom = rs.getString("NOM");
            String codePostal = rs.getString("CODE_POSTAL");
            String localite = rs.getString("LOCALITE");
            String rue = rs.getString("RUE");
            String numRue = rs.getString("NUM_RUE");
            String pays = rs.getString("PAYS");

            Address address = new Address(rue, numRue, codePostal, localite, pays);
            Restaurant restaurant = new Restaurant(id, nom, address);
            cache.put(id, restaurant);
        }

        return cache.get(id);
    }
}