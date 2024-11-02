package capstone.carru.exception;

import capstone.carru.dto.ErrorCode;

public class InternalServerException extends GeneralException {

    public InternalServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InternalServerException(String message) {
        super(message, ErrorCode.INTERNAL_SERVER);
    }

    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
