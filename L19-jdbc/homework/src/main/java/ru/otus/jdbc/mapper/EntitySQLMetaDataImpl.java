package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String tableName;
    private final String idField;
    private final List<String> fieldsWitohutIdNames;

    public EntitySQLMetaDataImpl(
        EntityClassMetaData<?> entityClassMetaData
    ) {
        tableName = entityClassMetaData.getName();
        fieldsWitohutIdNames = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).map(String::toLowerCase).toList();
        idField = entityClassMetaData.getIdField().getName().toLowerCase();
    }

    @Override
    public String getSelectAllSql() {
        return new StringBuilder("select * from ")
            .append(tableName)
            .toString();
    }

    @Override
    public String getSelectByIdSql() {
        return new StringBuilder("select * from ")
            .append(tableName)
            .append(" where ")
            .append(idField)
            .append(" = ?")
            .toString();
    }

    @Override
    public String getInsertSql() {
        return new StringBuilder("insert into ")
            .append(tableName)
            .append("(")
            .append(String.join(", ", fieldsWitohutIdNames))
            .append(") values (")
            .append(Stream.generate(() -> "?").limit(fieldsWitohutIdNames.size()).collect(Collectors.joining(", ")))
            .append(")")
            .toString();
    }

    @Override
    public String getUpdateSql() {
        StringBuilder sb = new StringBuilder("update ")
            .append(tableName)
            .append(" set ")
            .append(String.join(" = ?, ", fieldsWitohutIdNames))
            .append(" = ?");
        sb.append(" where ")
            .append(idField)
            .append(" = ?");
        return sb.toString();
    }
}
