package Application.domain.entites;

import Application.domain.enumeration.BandeiraCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private BandeiraCartao bandeiraCartao;

    private BigDecimal renda;

    private BigDecimal limite;

    public Cartao(String nome, BandeiraCartao bandeiraCartao, BigDecimal renda, BigDecimal limite) {
        this.nome = nome;
        this.bandeiraCartao = bandeiraCartao;
        this.renda = renda;
        this.limite = limite;
    }
}
