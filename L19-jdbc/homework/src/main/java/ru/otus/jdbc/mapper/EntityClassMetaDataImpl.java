package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import ru.otus.crm.annotations.Id;

@SuppressWarnings("unchecked")
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final T object;

    public EntityClassMetaDataImpl(T object) {
        this.object = object;
    }

    @Override
    public String getName() {
        return object.getClass().getSimpleName().toLowerCase();

    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return (Constructor<T>) object.getClass().getDeclaredConstructor(getAllFields()
                                                                                 .stream()
                                                                                 .map(Field::getType)
                                                                                 .toArray(Class<?>[]::new));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Не удалось получить конструктор", e);
        }
    }

    @Override
    public Field getIdField() {
        return Arrays
            .stream(object.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Id.class))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(object.getClass().getDeclaredFields()).toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream().filter(method -> !method.isAnnotationPresent(Id.class)).toList();
    }
}
