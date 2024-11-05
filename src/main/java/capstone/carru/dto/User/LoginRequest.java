package capstone.carru.dto.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Size(max = 320, message = "최대 320자까지 입력할 수 있습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(max = 100, message = "최대 100자까지 입력할 수 있습니다.")
    private String password;
}
