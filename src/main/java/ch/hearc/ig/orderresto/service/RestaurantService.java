package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.Restaurant;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class RestaurantService {

    public List<Restaurant> getAllRestaurants() {
        EntityManager em = null;
        try {
            // Obtenir l'EntityManager via JpaUtils
            em = JpaUtils.getEntityManager();

            // Créer une requête JPQL pour récupérer tous les restaurants
            TypedQuery<Restaurant> query = em.createQuery(
                    "SELECT r FROM Restaurant r", Restaurant.class);

            // Exécuter la requête et retourner la liste
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des restaurants.");
            return null;
        } finally {
            // Fermer l'EntityManager si ouvert --> à utiliser méthode de JpaUtils
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

        public String getRestaurantName(Restaurant restaurant) {
        return restaurant.getName();
    }

}
