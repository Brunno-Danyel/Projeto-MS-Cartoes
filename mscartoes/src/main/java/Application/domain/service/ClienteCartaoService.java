package Application.domain.service;

import Application.domain.entites.ClienteCartao;
import Application.domain.repository.ClienteCartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteCartaoService {

    @Autowired
    private ClienteCartaoRepository repository;

    public List<ClienteCartao> findByCpf(String cpf){
        return repository.findByCpf(cpf);
    }
}
