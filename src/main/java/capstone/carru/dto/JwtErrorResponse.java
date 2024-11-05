package capstone.carru.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtErrorResponse {
    private String resultCode;
    private String message;
}
