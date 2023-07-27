package ru.otus.servers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.services.ClientAuthService;
import ru.otus.services.ClientAuthServiceImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

class ClientsWebServerWithFilterBasedSecurityTest extends AbstractHibernateTest {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    @Test
    void runServer() throws Exception {
        ClientAuthService authService = new ClientAuthServiceImpl(clientTemplate, transactionManager);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        ClientsWebServer usersWebServer = new ClientsWebServerWithFilterBasedSecurity(
            WEB_SERVER_PORT,
            authService,
            clientTemplate,
            transactionManager,
            gson,
            templateProcessor
        );

        var client = new Client(
            null,
            "Vasya",
            new Address(null, "AnyStreet"),
            List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333")),
            "12345"
        );

        transactionManager.doInTransaction(session -> {
            clientTemplate.insert(session, client);
            return client;
        });

        usersWebServer.start();
        usersWebServer.join();
    }
}