package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProductService {

    public List<Product> getProductsByRestaurant(Restaurant restaurant) {
        EntityManager em = null;
        try {
            // Obtenir l'EntityManager via JpaUtils
            em = JpaUtils.getEntityManager();

            // Créer une requête JPQL pour filtrer les produits par restaurant
            TypedQuery<Product> query = em.createQuery(
                    "SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId", Product.class);
            query.setParameter("restaurantId", restaurant.getId());

            // Exécuter la requête et retourner la liste
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des produits pour le restaurant " + restaurant.getName());
            return null;
        } finally {
            // Fermer l'EntityManager si ouvert
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}