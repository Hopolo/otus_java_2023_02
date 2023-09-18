package ru.otus.crm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "address")
@Getter
public class Address implements Cloneable {

    @Id
    private final Long id;
    @Nonnull
    private final String street;
    private final Long clientId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Address(
        @JsonProperty(value = "street") String street,
        @JsonProperty(value = "clientId") Long clientId
    ) {
        this(null, street, clientId);
    }

    @PersistenceCreator
    public Address(
        Long id,
        String street,
        Long clientId
    ) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }

    @Override
    public Address clone() {
        return new Address(id, street, clientId);
    }

    @Override
    public String toString() {
        return street;
    }

}
