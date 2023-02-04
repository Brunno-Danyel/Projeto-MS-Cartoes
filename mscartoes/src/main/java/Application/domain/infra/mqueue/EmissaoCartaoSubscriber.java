package Application.domain.infra.mqueue;

import Application.domain.entites.Cartao;
import Application.domain.entites.ClienteCartao;
import Application.domain.entites.DadosSolicitacaoEmissaoCartao;
import Application.domain.repository.CartaoRepository;
import Application.domain.repository.ClienteCartaoRepository;
import ch.qos.logback.core.net.server.Client;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmissaoCartaoSubscriber {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private ClienteCartaoRepository clienteCartaoRepository;

    @RabbitListener(queues = "${mq.queue.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String payload) {
        try {
            var mapper = new ObjectMapper();
            DadosSolicitacaoEmissaoCartao dadosSolicitacaoEmissaoCartao = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);

            Cartao cartao = cartaoRepository.findById(dadosSolicitacaoEmissaoCartao.getIdCartao())
                    .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

            ClienteCartao clienteCartao = new ClienteCartao();
            clienteCartao.setCartao(cartao);
            clienteCartao.setCpf(dadosSolicitacaoEmissaoCartao.getCpf());
            clienteCartao.setLimite(dadosSolicitacaoEmissaoCartao.getLimiteLiberado());

            clienteCartaoRepository.save(clienteCartao);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
