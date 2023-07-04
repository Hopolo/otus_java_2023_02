package ru.otus.services;

import org.hibernate.Session;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.crm.model.Client;

public class ClientAuthServiceImpl implements ClientAuthService {

    private final DataTemplateHibernate<Client> clientDataTemplateHibernate;

    public ClientAuthServiceImpl(
        Session session,
        DataTemplateHibernate<Client> clientDataTemplateHibernate
    ) {
        this.clientDataTemplateHibernate = clientDataTemplateHibernate;
    }

    @Override
    public boolean authenticate(
        Session session,
        String login,
        String password
    ) {
        return clientDataTemplateHibernate.findByEntityField(session, "password", password)
            .stream()
            .anyMatch(client -> client.getPassword().equals(password));
    }

}
