package Application.domain.controller;

import Application.domain.dto.CartaoSaveRequest;
import Application.domain.dto.CartoesPorClienteResponse;
import Application.domain.entites.Cartao;
import Application.domain.entites.ClienteCartao;
import Application.domain.service.CartaoService;
import Application.domain.service.ClienteCartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
public class CartoesController {

    @Autowired
    private CartaoService service;

    @Autowired
    ClienteCartaoService clienteCartaoService;

    /*@GetMapping
    public String status() {
        return "ok";
    }*/

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody CartaoSaveRequest cartao) {
        Cartao cartaoRequest = cartao.fromDTO();
        service.salvar(cartaoRequest);
        return ResponseEntity.ok().body(cartaoRequest).status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> rendaCartao(@RequestParam("renda") Long renda) {
        List<Cartao> list = service.getCartao(renda);
        return ResponseEntity.ok(list);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam String cpf){
        List<ClienteCartao> list = clienteCartaoService.findByCpf(cpf);
        List<CartoesPorClienteResponse> list1 = list.stream().map(CartoesPorClienteResponse::fromDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(list1);
    }


}
