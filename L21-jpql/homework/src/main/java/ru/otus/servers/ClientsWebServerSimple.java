package ru.otus.servers;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlets.ClientsApiServlet;
import ru.otus.servlets.ClientsServlet;

public class ClientsWebServerSimple implements ClientsWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    protected final TemplateProcessor templateProcessor;
    private final DataTemplateHibernate<Client> clientDataTemplateHibernate;
    protected final TransactionManager transactionManager;
    private final Gson gson;
    private final Server server;

    public ClientsWebServerSimple(
        int port,
        DataTemplateHibernate<Client> clientDataTemplateHibernate,
        TransactionManager transactionManager,
        Gson gson,
        TemplateProcessor templateProcessor
    ) {
        this.clientDataTemplateHibernate = clientDataTemplateHibernate;
        this.transactionManager = transactionManager;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    protected Handler applySecurity(
        ServletContextHandler servletContextHandler,
        String... paths
    ) {
        return servletContextHandler;
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/user/*"));

        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[] {START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(
            new ServletHolder(new ClientsServlet(templateProcessor, clientDataTemplateHibernate, transactionManager)),
            "/clients"
        );
        servletContextHandler.addServlet(
            new ServletHolder(new ClientsApiServlet(clientDataTemplateHibernate, transactionManager, gson)),
            "/api/client/*"
        );
        return servletContextHandler;
    }
}
