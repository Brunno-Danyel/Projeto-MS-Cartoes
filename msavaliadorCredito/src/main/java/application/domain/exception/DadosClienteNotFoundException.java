package application.domain.exception;

public class DadosClienteNotFoundException extends Exception {

    public DadosClienteNotFoundException() {
        super("Dados do Cliente não econtrados para o cpf informado!");
    }
}
