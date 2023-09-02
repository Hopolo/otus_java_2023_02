package ru.otus.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.core.cache.HwCache;
import ru.otus.core.repository.ClientRepository;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private final ClientRepository clientRepository;
    @Autowired
    private HwCache<String, Client> myCache;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(
        TransactionManager transactionManager,
        ClientRepository clientRepository
    ) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientRepository.save(clientCloned);
                log.info("created client: {}", clientCloned);
                myCache.put(savedClient.getId().toString(), savedClient);
                return savedClient;
            }
            var savedClient = clientRepository.save(clientCloned);
            myCache.put(savedClient.getId().toString(), savedClient);
            log.info("updated client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return Optional.of(myCache.get(String.valueOf(id))).or(() -> transactionManager.doInTransaction(() -> {
            var clientOptional = clientRepository.findById(id);
            clientOptional.ifPresent(client -> myCache.put(String.valueOf(id), client));
            log.info("client: {}", clientOptional);
            return clientOptional;
        }));
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInTransaction(() -> {
            var clientList = clientRepository.findAll();
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    @Override
    public Client findRandom() {
        List<Client> clients = findAll();
        return clients.get(new Random().nextInt(clients.size()));
    }
}
