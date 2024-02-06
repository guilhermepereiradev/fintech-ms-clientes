package com.fintech.msclientes.application;

import com.fintech.msclientes.application.representation.ClienteSaveRequest;
import com.fintech.msclientes.domain.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClientesController {
    private static final Logger log = LoggerFactory.getLogger(ClientesController.class);

    private final ClienteService clienteService;
    @GetMapping
    public String status() {
        log.info("Obtendo o status do microservice de clientes.");
        return "ok!";
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ClienteSaveRequest request) {
        var cliente = request.toModel();
        clienteService.save(cliente);

        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();

        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<Cliente> dadosDoCliente(@RequestParam("cpf") String cpf) {
        var cliente = clienteService.findByCpf(cpf);

        if(cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente.get());
    }
}
