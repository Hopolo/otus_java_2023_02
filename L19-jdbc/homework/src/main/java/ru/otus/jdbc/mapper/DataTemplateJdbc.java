package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.annotations.Id;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
        DbExecutor dbExecutor,
        EntitySQLMetaData entitySQLMetaData,
        EntityClassMetaData<T> entityClassMetaData
    ) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(
        Connection connection,
        long id
    ) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                T result = null;
                if (rs.next()) {
                    List<Field> allFields = entityClassMetaData.getAllFields();
                    Object[] instanceParams = new Object[allFields.size()];
                    int i = 0;
                    for (Field field : allFields) {
                        if (field.isAnnotationPresent(Id.class)) {
                            instanceParams[i] = rs.getLong(field.getName().toLowerCase());
                            i++;
                            continue;
                        }
                        instanceParams[i] = rs.getString(field.getName().toLowerCase());
                        i++;
                    }
                    result = entityClassMetaData.getConstructor().newInstance(instanceParams);
                }
                return result;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            ArrayList<T> objectList = new ArrayList<>();
            try {
                while (rs.next()) {
                    List<Field> allFields = entityClassMetaData.getAllFields();
                    Object[] instanceParams = new Object[allFields.size()];
                    int i = 0;
                    for (Field field : allFields) {
                        if (field.isAnnotationPresent(Id.class)) {
                            instanceParams[i] = rs.getLong(field.getName().toLowerCase());
                            continue;
                        }
                        instanceParams[i] = rs.getString(field.getName().toLowerCase());
                        i++;
                    }
                    objectList.add(entityClassMetaData.getConstructor().newInstance(instanceParams));
                }
                return objectList;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Object> params = new ArrayList<>();
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(Optional.ofNullable(field.get(client)).orElse("null"));
            }
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> params = new ArrayList<>();
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(field.get(client));
            }
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
