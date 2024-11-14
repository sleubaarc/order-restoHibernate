package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

public class OrderMapper {

    private CustomerMapper customerMapper = new CustomerMapper();
    private RestaurantMapper restaurantMapper = new RestaurantMapper();
    private ProductMapper productMapper = new ProductMapper();
    private static final IdentityMap<Order, Long> cache = new IdentityMap<>();


    public Order findById(Connection connection, Long id) throws SQLException {
        if (cache.contains(id)) {
            return cache.get(id);
        }
        String query = "SELECT NUMERO, FK_CLIENT, FK_RESTO, A_EMPORTER, QUAND FROM COMMANDE WHERE NUMERO = ?";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            OracleResultSet rs = (OracleResultSet) stmt.executeQuery();
            if (rs.next()) {
                return populateCache(connection, rs);
            }
        }
        return null;
    }

    public Collection<Order> findByCustomer(Connection connection, Customer customer) throws SQLException {
        String query = "SELECT NUMERO FROM COMMANDE WHERE FK_CLIENT = ?";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            if (customer == null) {
                throw new NullPointerException("Retour au menu principale");
            }
            stmt.setLong(1, customer.getId());

            OracleResultSet rs = (OracleResultSet) stmt.executeQuery();

            Set<Order> orders = new HashSet<>();
            while (rs.next()) {
                Long orderId = rs.getLong("NUMERO");
                Order order = findById(connection, orderId);  // Utilise `findById` pour bénéficier du cache
                orders.add(order);
            }

            return orders;
        }
    }

    private Order loadProducts(Connection connection, Order order) throws SQLException {
        String query = "SELECT FK_PRODUIT FROM PRODUIT_COMMANDE WHERE FK_COMMANDE = ?";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            stmt.setLong(1, order.getId());
            OracleResultSet rs = (OracleResultSet) stmt.executeQuery();

            while (rs.next()) {
                Long productId = rs.getLong("FK_PRODUIT");
                Product product = productMapper.findById(connection, productId);
                order.addProduct(product);
            }
        }
        return order;
    }



    public void insert(Connection connection, Order order) {
        String query = "INSERT INTO COMMANDE (FK_CLIENT, FK_RESTO, A_EMPORTER, QUAND) " +
                "VALUES (?, ?, ?, ?) RETURNING NUMERO INTO ?";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            stmt.setLong(1, order.getCustomer().getId());
            stmt.setLong(2, order.getRestaurant().getId());
            stmt.setString(3, order.getTakeAway().equalsIgnoreCase("O") ? "O" : "N");
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(order.getWhen()));
            stmt.registerReturnParameter(5, OracleTypes.NUMBER);

            stmt.executeUpdate();

            try (OracleResultSet rs = (OracleResultSet) stmt.getReturnResultSet()) {
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    order.setId(generatedId);
                } else {
                    throw new SQLException("Impossible de récupérer l'ID généré pour la commande.");
                }
            }

            insertProducts(connection, order);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertProducts(Connection connection, Order order) throws SQLException {
        String query = "INSERT INTO PRODUIT_COMMANDE (FK_COMMANDE, FK_PRODUIT) VALUES (?, ?)";
        try (OraclePreparedStatement stmt = (OraclePreparedStatement) connection.prepareStatement(query)) {
            for (Product product : order.getProducts()) {
                stmt.setLong(1, order.getId());
                stmt.setLong(2, product.getId());
                stmt.executeUpdate();
            }
        }
    }

    private Order populateCache(Connection connection, OracleResultSet rs) throws SQLException {
        Long orderId = rs.getLong("NUMERO");

        if (!cache.contains(orderId)) {
            Long customerId = rs.getLong("FK_CLIENT");
            Long restaurantId = rs.getLong("FK_RESTO");
            String takeAway = rs.getString("A_EMPORTER");
            LocalDateTime when = rs.getTimestamp("QUAND").toLocalDateTime();

            Customer customer = customerMapper.findById(connection, customerId);
            Restaurant restaurant = restaurantMapper.findById(connection, restaurantId);

            Order order = new Order(orderId, customer, restaurant, takeAway, when);
            order = loadProducts(connection, order);

            cache.put(orderId, order);
        }

        return cache.get(orderId);
    }


}