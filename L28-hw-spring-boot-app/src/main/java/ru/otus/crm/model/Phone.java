package ru.otus.crm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "phone")
public class Phone implements Cloneable {
    @Id
    private final Long id;
    @Nonnull
    private final String number;
    private final Long clientId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Phone(
        @JsonProperty(value = "number") String number,
        @JsonProperty(value = "clientId") Long clientId
    ) {
        this(null, number, clientId);
    }

    @PersistenceCreator
    public Phone(
        Long id,
        String number,
        Long clientId
    ) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    @Override
    public Phone clone() {
        return new Phone(id, number, clientId);
    }

    @Override
    public String toString() {
        return number;
    }

}
