package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    public Order createOrder(Customer customer, Restaurant restaurant, Product product, String takeAway) {
        Order order = new Order(null, customer, restaurant, takeAway, LocalDateTime.now());
        order.addProduct(product);
        return order;
    }

    public void insertOrder(Order order) {
        EntityManager em = null;
        try {
            em = JpaUtils.getEntityManager();
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Order> getOrdersByCustomer(Customer customer) {
        EntityManager em = null;
        try {
            em = JpaUtils.getEntityManager();
            TypedQuery<Order> query = em.createQuery(
                    "SELECT o FROM Order o WHERE o.customer = :customer", Order.class);
            query.setParameter("customer", customer);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des commandes du client.", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // PROBLEME AVEC CETTE METHODES A CORRIGER

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
}