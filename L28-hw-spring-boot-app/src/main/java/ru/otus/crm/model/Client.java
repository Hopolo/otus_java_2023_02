package ru.otus.crm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import java.util.Set;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Table(name = "client")
public class Client implements Cloneable, Persistable<Long> {

    @Id
    private final Long id;
    @Nonnull
    private final String name;
    @Nonnull
    @MappedCollection(idColumn = "client_id")
    private final Address address;
    @Nonnull
    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;
    @Nonnull
    private final String password;
    @Transient
    private final boolean isNew;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Client(
        @JsonProperty(value = "id") Long id,
        @JsonProperty(value = "name") String name,
        @JsonProperty(value = "address") Address address,
        @JsonProperty(value = "phones") Set<Phone> phones,
        @JsonProperty(value = "password") String password,
        @JsonProperty(value = "isNew") boolean isNew
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.password = password;
        this.isNew = isNew;
    }

    @PersistenceCreator
    public Client(
        Long id,
        String name,
        Address address,
        Set<Phone> phones,
        String password
    ) {
        this(id, name, address, phones, password, false);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name, this.address.clone(), Set.copyOf(this.phones), password, isNew);
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", address='" + address + '\'' +
            ", phones='" + phones + '\'' +
            ", password='" + password + '\'' +
            ", isNew='" + isNew + '\'' +
            '}';
    }
}
