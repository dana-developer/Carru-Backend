package capstone.carru.exception;

import capstone.carru.dto.ErrorCode;
import lombok.Getter;

@Getter
public abstract class GeneralException extends RuntimeException{
    private final ErrorCode errorCode;

    protected GeneralException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    protected GeneralException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
