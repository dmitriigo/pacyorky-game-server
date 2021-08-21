package ee.pacyorky.gameserver.gameserver;

import ee.pacyorky.gameserver.gameserver.entities.game.Player;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;

public abstract class BaseTest {

    private final EntityManager entityManager;

    public BaseTest(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    void setUp() {
        deleteAllEntities(Player.class);
    }

    private <T> int deleteAllEntities(Class<T> entityType) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<T> query = builder.createCriteriaDelete(entityType);
        query.from(entityType);
        return entityManager.createQuery(query).executeUpdate();
    }

}
