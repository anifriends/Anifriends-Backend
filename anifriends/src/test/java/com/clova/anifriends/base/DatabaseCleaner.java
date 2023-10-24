package com.clova.anifriends.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.Type;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DatabaseCleaner {

    private final EntityManager entityManager;
    private final List<String> tableNames;

    public DatabaseCleaner(EntityManager em) {
        this.entityManager = em;
        this.tableNames = entityManager.getMetamodel()
            .getEntities()
            .stream()
            .map(Type::getJavaType)
            .map(javaType -> javaType.getAnnotation(Table.class))
            .map(Table::name)
            .toList();
    }

    public void clear() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE")
            .executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("truncate table " + tableName + " RESTART IDENTITY ")
                .executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY true")
            .executeUpdate();
    }
}
