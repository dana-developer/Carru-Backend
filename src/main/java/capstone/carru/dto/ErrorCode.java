package capstone.carru.dto;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /**
     * 400 Bad Request (잘못된 요청)
     */
    INVALID("BR000", "잘못된 요청입니다."),
    INVALID_AUTH_TOKEN("BR001", "만료되거나 잘못된 엑세스 토큰입니다."),
    INVALID_DUPLICATED_EMAIL("BR002", "이미 사용중인 이메일입니다."),
    INVALID_MEMBER("BR003", "승인되지 않은 회원입니다."),
    INVALID_PASSWORD("BR004", "비밀번호가 일치하지 않습니다."),
    INVALID_PRODUCT_STATUS("BR005", "승인되지 않은 물류이거나 이미 예약된 물류입니다."),

    /**
     * 404 Not Found (존재하지 않는 리소스)
     */
    NOT_EXISTS("NF000", "존재하지 않습니다."),
    NOT_EXISTS_MEMBER("NF001", "존재하지 않는 회원입니다."),
    NOT_EXISTS_PRODUCT("NF002", "존재하지 않는 물류입니다."),
    NOT_EXISTS_WAREHOUSE("NF003", "존재하지 않는 창고입니다."),
    NOT_EXISTS_USERWAREHOUSE("NF004", "해당 사용자의 창고가 존재하지 않습니다 또는 화주가 아닙니다."),
    NOT_EXISTS_RESERVATION("NF005", "존재하지 않는 예약건입니다."),
    /**
     * 500 Internal Server Exception (서버 내부 에러)
     */
    INTERNAL_SERVER("IS000", "서버 내부 에러가 발생했습니다."),
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
