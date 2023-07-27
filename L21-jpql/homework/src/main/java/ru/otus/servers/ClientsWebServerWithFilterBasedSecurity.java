package ru.otus.servers;

import com.google.gson.Gson;
import java.util.Arrays;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;
import ru.otus.services.ClientAuthService;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlets.AuthorizationFilter;
import ru.otus.servlets.LoginServlet;

public class ClientsWebServerWithFilterBasedSecurity extends ClientsWebServerSimple {
    private final ClientAuthService authService;


    public ClientsWebServerWithFilterBasedSecurity(
        int port,
        ClientAuthService authService,
        DataTemplateHibernate<Client> clientDataTemplateHibernate,
        TransactionManager transactionManager,
        Gson gson,
        TemplateProcessor templateProcessor
    ) {
        super(port, clientDataTemplateHibernate, transactionManager, gson, templateProcessor);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(
        ServletContextHandler servletContextHandler,
        String... paths
    ) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService, transactionManager)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
