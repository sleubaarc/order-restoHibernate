package ch.hearc.ig.orderresto.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtils {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    private JpaUtils() {
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

