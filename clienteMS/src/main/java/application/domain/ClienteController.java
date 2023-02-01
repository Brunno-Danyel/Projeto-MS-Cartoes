package application.domain;

import application.domain.dto.ClienteDTO;
import application.domain.service.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("clientes")
@Slf4j
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping
    public String ok(){
        return "Ok";
    }

    @PostMapping
    public ResponseEntity save(@RequestBody ClienteDTO dto) {
        var cliente = dto.fromDto();
        service.createdCliente(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity dadosCliente(@RequestParam("cpf") String cpf) {
        var cliente = service.getByCpf(cpf);
        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
}
