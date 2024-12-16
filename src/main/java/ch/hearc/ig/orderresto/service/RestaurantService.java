package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.Restaurant;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class RestaurantService {

    public List<Restaurant> getAllRestaurants() {
        return JpaUtils.inTransaction(em -> {
            TypedQuery<Restaurant> query = em.createQuery(
                    "SELECT r FROM Restaurant r", Restaurant.class);
            return query.getResultList();
        });
    }

        public String getRestaurantName(Restaurant restaurant) {
        return restaurant.getName();
    }

}
