package Application.domain.service;

import Application.domain.entites.Cartao;
import Application.domain.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository repository;

    @Transactional
    public Cartao salvar(Cartao cartao) {
        return repository.save(cartao);
    }

    public List<Cartao> getCartao(Long renda){
        var rendaBigDecimal = BigDecimal.valueOf(renda);
        return repository.findByRendaLessThanEqual(rendaBigDecimal);
    }

    public ResponseEntity<List<Cartao>> listCard() {
        List<Cartao> list = repository.findAll();
        return ResponseEntity.ok().body(list);
    }
}
