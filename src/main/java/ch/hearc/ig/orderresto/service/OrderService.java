package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.persistence.OrderMapper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.*;

public class OrderService {

    private final OrderMapper orderMapper = new OrderMapper();

    public Order createOrder(Customer customer, Restaurant restaurant, Product product, String takeAway) {
        Order order = new Order(null, customer, restaurant, takeAway, LocalDateTime.now());
        order.addProduct(product);
        return order;
    }

    public void insertOrder(Order order) {
        try (Connection connection = ConnectionDb.getInstance().getConnection()) {
            orderMapper.insert(connection, order);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try (Connection connection = ConnectionDb.getInstance().getConnection()) {
                connection.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            ConnectionDb.getInstance().releaseConnection();
        }
    }

    public List<Order> getOrdersByCustomer(Customer customer) {
        try (Connection connection = ConnectionDb.getInstance().getConnection()) {
            Collection<Order> orders = orderMapper.findByCustomer(connection, customer);
            return new ArrayList<>(orders);  // Conversion explicite en List
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des commandes du client.", e);
        }
    }

    public String getFormattedOrderInfo(Order order) {
        LocalDateTime when = order.getWhen();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy à HH:mm");
        return String.format("%.2f, le %s chez %s",
                order.getTotalAmount(),
                when.format(formatter),
                order.getRestaurant().getName());
    }

    public List<String> getFormattedProductsInfo(Order order) {
        List<String> productsInfo = new ArrayList<>();
        int index = 1;
        for (Product product : order.getProducts()) {
            productsInfo.add(String.format("%d. %s", index, product.getName()));
            index++;
        }
        return productsInfo;
    }

    public String getOrderDate(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy à HH:mm");
        return order.getWhen().format(formatter);
    }

    public String getOrderRestaurantName(Order order) {
        return order.getRestaurant().getName();
    }
    public BigDecimal getOrderTotalAmount(Order order) {
        return order.getTotalAmount();
    }
    public String getOrderTakeAway(Order order) {
        return order.getTakeAway();
    }

}
