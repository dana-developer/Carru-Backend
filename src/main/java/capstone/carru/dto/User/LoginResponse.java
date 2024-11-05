package capstone.carru.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class LoginResponse {
    private String token;
}
