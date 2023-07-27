package ru.otus.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.services.TemplateProcessor;

public class ClientsServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "clients.ftl";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_PASSWORD = "password";
    private final DataTemplateHibernate<Client> clientDataTemplateHibernate;
    private final TransactionManager transactionManager;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(
        TemplateProcessor templateProcessor,
        DataTemplateHibernate<Client> clientDataTemplateHibernate,
        TransactionManager transactionManager
    ) {
        this.templateProcessor = templateProcessor;
        this.clientDataTemplateHibernate = clientDataTemplateHibernate;
        this.transactionManager = transactionManager;
    }

    @Override
    protected void doGet(
        HttpServletRequest req,
        HttpServletResponse response
    ) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        List<Client> clients =
            transactionManager.doInReadOnlyTransaction(session -> clientDataTemplateHibernate
                .findAll(session)
                .stream()
                .map(Client::clone)
                .toList());

        paramsMap.put("clients", clients);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws IOException {
        String name = req.getParameter(PARAM_NAME);
        String address = req.getParameter(PARAM_ADDRESS);
        String phone = req.getParameter(PARAM_PHONE);
        String password = req.getParameter(PARAM_PASSWORD);

        var client = new Client(null, name, new Address(null, address), List.of(new Phone(null, phone)), password);

        transactionManager.doInTransaction(session -> clientDataTemplateHibernate.insert(session, client));
        resp.sendRedirect("/clients");
    }
}
