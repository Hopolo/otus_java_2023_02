package ru.otus.controllers;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Client;
import ru.otus.services.DBServiceClient;

@Slf4j
@Controller
public class ClientController {

    private final DBServiceClient clientService;

    public ClientController(
        DBServiceClient clientService
    ) {
        this.clientService = clientService;
    }

    @GetMapping({"/client/list"})
    public String clientsListView(Model model) {
        log.info("clientsListView(): list");
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        log.info("clientCreateView(): client create");
        model.addAttribute("client", new Client());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(Client client) {
        log.info("clientSave(): client save");
        clientService.saveClient(client);
        return new RedirectView("/", true);
    }
}
