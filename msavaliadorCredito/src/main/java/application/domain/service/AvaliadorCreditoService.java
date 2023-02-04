package application.domain.service;

import application.domain.exception.DadosClienteNotFoundException;
import application.domain.exception.ErroComunicacaoMSexception;
import application.domain.exception.ErroSolicitacaoCartaoException;
import application.domain.infra.CartoesResourceClient;
import application.domain.infra.ClienteResourceClient;
import application.domain.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import application.domain.model.*;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AvaliadorCreditoService {

    @Autowired
    private ClienteResourceClient clientesClient;

    @Autowired
    private CartoesResourceClient cartoesClient;

    @Autowired
    private SolicitacaoEmissaoCartaoPublisher solicitacaoEmissaoCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMSexception {
        try {
            ResponseEntity<DadosCliente> clienteResponseEntity = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartaoResponseEntity = cartoesClient.getCartoesByCliente(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(clienteResponseEntity.getBody())
                    .cartoes(cartaoResponseEntity.getBody())
                    .build();
        } catch (FeignException.FeignClientException ex) {
            int status = ex.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMSexception(ex.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda)
            throws DadosClienteNotFoundException, ErroComunicacaoMSexception {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.rendaCartao(renda);


            List<Cartao> cartoes = cartoesResponse.getBody();
            var listaCartoesAprovados = cartoes.stream().map(cartao -> {

                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                var fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado cartaoAprovado = new CartaoAprovado();
                cartaoAprovado.setCartao(cartao.getNome());
                cartaoAprovado.setBandeira(cartao.getBandeira());
                cartaoAprovado.setLimiteAprovado(limiteAprovado);

                return cartaoAprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        } catch (FeignException.FeignClientException ex) {
            int status = ex.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMSexception(ex.getMessage(), status);
        }
    }

    public ProtocoloSolicitarCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dadosSolicitacaoEmissaoCartao) {
        try {
            solicitacaoEmissaoCartaoPublisher.solicitarCartao(dadosSolicitacaoEmissaoCartao);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitarCartao(protocolo);
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}
