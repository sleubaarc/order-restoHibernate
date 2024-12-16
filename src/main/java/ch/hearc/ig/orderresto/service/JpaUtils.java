package ch.hearc.ig.orderresto.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;
import java.util.function.Function;

public class JpaUtils {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    private JpaUtils() {
    }
    // Méthode générique pour exécuter une opération transactionnelle JPA
    // <T> : Type générique de retour de la fonction
    public static <T> T inTransaction(Function<EntityManager, T> function) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            // Exécuter la fonction passée en paramètre
            // function.apply() exécute le code métier fourni par l'appelant
            // avec l'EntityManager comme contexte de persistance
            T result = function.apply(em);
            // Si la fonction s'exécute sans exception, valider (commit) la transaction
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erreur lors de la transaction", e);
        } finally {
            closeEntityManager();
        }
    }

    public static synchronized EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("GuideRestoPersistenceUnit");
            }
            em = emf.createEntityManager();
        }
        return em;
    }



    public static void closeEntityManager() {
        if (em != null && em.isOpen()) {
            em.close();
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

