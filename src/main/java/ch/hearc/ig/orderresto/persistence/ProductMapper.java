package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;

public class ProductMapper {

    private static final IdentityMap<Product, Long> cache = new IdentityMap<>();

    public Product findById(Connection connection, Long id) throws SQLException {
        if (cache.contains(id)) {
            return cache.get(id);
        }

        String query = "SELECT NUMERO, NOM, PRIX_UNITAIRE, DESCRIPTION, FK_RESTO FROM PRODUIT WHERE NUMERO = ?";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            OracleResultSet rs = (OracleResultSet) stmt.executeQuery();

            if (rs.next()) {
                return populateCache(connection, rs);
            }
        }
        return null;
    }

    public List<Product> findAllProducts(Connection connection) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT NUMERO, NOM, PRIX_UNITAIRE, DESCRIPTION, FK_RESTO FROM PRODUIT";

        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            OracleResultSet rs = (OracleResultSet) stmt.executeQuery();

            while (rs.next()) {
                Product product = populateCache(connection, rs);
                products.add(product);
            }
        }

        return products;
    }

    private Product populateCache(Connection connection, OracleResultSet rs) throws SQLException {
        Long id = rs.getLong("NUMERO");

        if (!cache.contains(id)) {
            String nom = rs.getString("NOM");
            BigDecimal prixUnitaire = rs.getBigDecimal("PRIX_UNITAIRE");
            String description = rs.getString("DESCRIPTION");
            Long fkResto = rs.getLong("FK_RESTO");

            Restaurant restaurant = new RestaurantMapper().findById(connection, fkResto);

            Product product = new Product(id, nom, prixUnitaire, description, restaurant);
            cache.put(id, product);
        }

        return cache.get(id);
    }
}
