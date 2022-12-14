package Application.domain.dto;

import Application.domain.entites.Cartao;
import Application.domain.enumeration.BandeiraCartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoSaveRequest {

    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;


    public Cartao fromDTO(){
        return new Cartao(nome, bandeira, renda, limite);
    }
}
