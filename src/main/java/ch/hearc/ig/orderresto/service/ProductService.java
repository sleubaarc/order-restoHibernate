package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProductService {

    public List<Product> getProductsByRestaurant(Restaurant restaurant) {
        return JpaUtils.inTransaction(em -> {
            TypedQuery<Product> query = em.createQuery(
                    "SELECT p FROM Product p " +
                            "LEFT JOIN FETCH p.restaurant r " +
                            "WHERE p.restaurant.id = :restaurantId", Product.class);
            query.setParameter("restaurantId", restaurant.getId());
            return query.getResultList();
        });
    }
}