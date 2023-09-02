package ru.otus.crm.model;

import jakarta.annotation.Nonnull;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
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

    public Client() {
        this(null, null, null, null, null, true);
    }

    public Client(
        Long id,
        String name,
        Address address,
        Set<Phone> phones,
        String password,
        boolean isNew
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
