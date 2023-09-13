package ru.otus.controllers;

import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.services.DBServiceClient;

@Controller
public class ClientController {
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    private final DBServiceClient clientService;

    public ClientController(
        DBServiceClient clientService
    ) {
        this.clientService = clientService;
    }

    @GetMapping({"/client/list"})
    public String clientsListView(Model model) {
        log.info("+clientsListView()");
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        log.info("-clientsListView(): list: {}", clients);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        log.info("clientCreateView(): client create");
        return "clientCreate";
    }

    @PostMapping(value = "/client/save")
    public RedirectView clientSave(
        @RequestParam("name") String name,
        @RequestParam("address") String address,
        @RequestParam("phone") String phone,
        @RequestParam("password") String password
    ) {
        log.info("+clientSave(): params: {}, {}, {}, {}", name, address, phone, password);
        long clientId = System.currentTimeMillis();
        Client savedClient = clientService.saveClient(new Client(
            clientId,
            name,
            new Address(address, clientId),
            Set.of(new Phone(phone, clientId)),
            password,
            true
        ));
        log.info("-clientSave(): saved client: {}", savedClient);
        return new RedirectView("/client/list", true);
    }
}
