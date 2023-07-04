package ru.otus.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.Client;
import ru.otus.services.TemplateProcessor;

public class ClientsServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "randomUser";
    private final DataTemplateHibernate<Client> clientDataTemplateHibernate;
    private final TemplateProcessor templateProcessor;
    private TransactionManagerHibernate transactionManager;

    public ClientsServlet(
        TemplateProcessor templateProcessor,
        DataTemplateHibernate<Client> clientDataTemplateHibernate
    ) {
        this.templateProcessor = templateProcessor;
        this.clientDataTemplateHibernate = clientDataTemplateHibernate;
    }

    @Override
    protected void doGet(
        HttpServletRequest req,
        HttpServletResponse response
    ) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        transactionManager.doInReadOnlyTransaction(session ->
                                                       clientDataTemplateHibernate
                                                           .findAll(session)
                                                           .stream()..ifPresent(randomUser -> paramsMap.put(
            TEMPLATE_ATTR_RANDOM_USER,
            randomUser
        ));)
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

}
