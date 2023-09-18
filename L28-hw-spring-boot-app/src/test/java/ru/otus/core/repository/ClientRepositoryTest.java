package ru.otus.core.repository;

import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

@SpringBootTest
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Test
    void findById() {
        var client = new Client(1L, "Vasya", new Address("Kolotushkina", 1L), Set.of(new Phone("11-111-1111", 1L)), "112233", true);
        clientRepository.save(client);
        Assertions.assertEquals(client.getId(), clientRepository.findAll().get(0).getId());
    }

}