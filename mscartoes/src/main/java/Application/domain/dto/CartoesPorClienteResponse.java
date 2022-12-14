package Application.domain.dto;

import Application.domain.entites.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartoesPorClienteResponse {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static CartoesPorClienteResponse fromDto(ClienteCartao dto) {
        return new CartoesPorClienteResponse(dto.getCartao().getNome(),
                dto.getCartao().getBandeiraCartao().toString(),
                dto.getLimite());
    }
}
