package ch.hearc.ig.orderresto.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;
import java.util.function.Function;

public class JpaUtils {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<>();

    private JpaUtils() {
    }
    // Méthode générique pour exécuter une opération transactionnelle JPA
    // <T> : Type générique de retour de la fonction
    public static <T> T inTransaction(Function<EntityManager, T> function) {
        EntityManager em = null;
        boolean shouldClose = false;
        try {
            em = getEntityManager();
            boolean isNewTransaction = !em.getTransaction().isActive();
            if (isNewTransaction) {
                em.getTransaction().begin();
                shouldClose = true;
            }
            T result = function.apply(em);
            if (isNewTransaction) {
                em.getTransaction().commit();
            }
            return result;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erreur lors de la transaction", e);
        } finally {
            if (shouldClose) {
                closeEntityManager();
            }
        }
    }

    public static synchronized EntityManager getEntityManager() {
        EntityManager em = threadLocalEntityManager.get();
        if (em == null || !em.isOpen()) {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("GuideRestoPersistenceUnit");
            }
            em = emf.createEntityManager();
            threadLocalEntityManager.set(em);
        }
        return em;
    }


    public static void closeEntityManager() {
        EntityManager em = threadLocalEntityManager.get();
        if (em != null && em.isOpen()) {
            em.close();
            threadLocalEntityManager.remove();
        }
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public static void closeEntityManagerFactoryAndEntityManager() {
        closeEntityManagerFactory();
        closeEntityManager();
    }
}

