package application.service;

import application.exception.DadosClienteNotFoundException;
import application.exception.ErroComunicacaoMSexception;
import application.infra.CartoesControllerClient;
import application.infra.ClienteControllerClient;
import application.model.CartaoCliente;
import application.model.DadosCliente;
import application.model.SituacaoCliente;
import com.netflix.discovery.converters.Auto;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliadorCreditoService {

    @Autowired
    private ClienteControllerClient clienteControllerClient;

    @Autowired
    private CartoesControllerClient cartoesControllerClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMSexception {
        try {
            ResponseEntity<DadosCliente> clienteResponseEntity = clienteControllerClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartaoResponseEntity = cartoesControllerClient.getCartoesByCliente(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(clienteResponseEntity.getBody())
                    .cartoes(cartaoResponseEntity.getBody())
                    .build();
        }catch (FeignException.FeignClientException ex){
            int status = ex.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMSexception(ex.getMessage(), status);
        }
    }
}
