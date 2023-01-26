package application.service;

import application.infra.ClienteControllerClient;
import application.model.DadosCliente;
import application.model.SituacaoCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AvaliadorCreditoService {

    @Autowired
    private ClienteControllerClient clienteControllerClient;

    public SituacaoCliente obterSituacaoCliente(String cpf){
      ResponseEntity<DadosCliente> clienteResponseEntity = clienteControllerClient.dadosCliente(cpf);
      return SituacaoCliente
              .builder()
              .cliente(clienteResponseEntity.getBody())
              .build();
    }
}
