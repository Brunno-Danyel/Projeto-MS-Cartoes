package application.domain.dto;

import application.domain.entities.Cliente;
import lombok.Data;

@Data
public class ClienteDTO {

    private String cpf;
    private String nome;
    private Integer idade;

    public Cliente fromDto(){
        return new Cliente(cpf, nome, idade);
    }

}
