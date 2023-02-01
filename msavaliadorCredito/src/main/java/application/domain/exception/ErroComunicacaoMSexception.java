package application.domain.exception;

import lombok.Getter;

public class ErroComunicacaoMSexception extends Exception {

    @Getter
    private Integer status;

    public ErroComunicacaoMSexception(String msg, Integer status) {
        super(msg);
        this.status = status;
    }
}
