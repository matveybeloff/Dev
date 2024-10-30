package org.example.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseManager {
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("SchoolUnit");

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static void shutdown() {
        entityManagerFactory.close();
    }
}
