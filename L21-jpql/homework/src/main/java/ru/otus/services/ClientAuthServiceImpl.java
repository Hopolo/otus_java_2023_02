package ru.otus.services;

import java.util.List;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

public class ClientAuthServiceImpl implements ClientAuthService {

    private final DataTemplateHibernate<Client> clientDataTemplateHibernate;
    private final TransactionManager transactionManager;

    public ClientAuthServiceImpl(
        DataTemplateHibernate<Client> clientDataTemplateHibernate,
        TransactionManager transactionManager
    ) {
        this.clientDataTemplateHibernate = clientDataTemplateHibernate;
        this.transactionManager = transactionManager;
    }

    @Override
    public boolean authenticate(
        String login,
        String password
    ) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            List<Client> clients = clientDataTemplateHibernate.findByEntityField(
                session,
                "password",
                password
            );
            return clients.stream().anyMatch(client -> client.getName().equals(login));
        });
    }

}
