package ru.otus.servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DataTemplateHibernate<Client> clientDataTemplateHibernate;
    private final TransactionManager transactionManager;
    private final Gson gson;

    public ClientsApiServlet(
        DataTemplateHibernate<Client> clientDataTemplateHibernate,
        TransactionManager transactionManager,
        Gson gson
    ) {
        this.clientDataTemplateHibernate = clientDataTemplateHibernate;
        this.transactionManager = transactionManager;
        this.gson = gson;
    }

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        Optional<Client> client =
            transactionManager.doInReadOnlyTransaction(session -> clientDataTemplateHibernate.findById(
                session,
                extractIdFromRequest(request)
            ));

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(client));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }

}
