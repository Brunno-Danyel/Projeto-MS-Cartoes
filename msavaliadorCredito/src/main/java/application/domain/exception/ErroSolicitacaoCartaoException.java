package application.domain.exception;

public class ErroSolicitacaoCartaoException extends RuntimeException {
    public ErroSolicitacaoCartaoException(String message) {
        super(message);
    }
}
