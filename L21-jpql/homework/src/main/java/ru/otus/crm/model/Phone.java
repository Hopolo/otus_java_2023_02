package ru.otus.crm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phone")
@NoArgsConstructor
public class Phone implements Cloneable {

    @Id
    @SequenceGenerator(name = "phone_gen", sequenceName = "phone_seq",
        initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_gen")
    @Column(name = "phone_id")
    private Long id;
    private String number;
    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;
    public Phone(
        Long id,
        String number
    ) {
        this.id = id;
        this.number = number;
    }

    @Override
    public Phone clone() {
        return new Phone(id, number);
    }

}
