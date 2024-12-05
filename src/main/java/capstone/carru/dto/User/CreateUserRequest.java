package capstone.carru.dto.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 100, message = "최대 100자까지 입력할 수 있습니다.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Size(max = 320, message = "최대 320자까지 입력할 수 있습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(max = 100, message = "최대 100자까지 입력할 수 있습니다.")
    private String password;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Size(max = 20, message = "최대 20자까지 입력할 수 있습니다.")
    private String phoneNumber;

    @NotBlank(message = "차고지 주소 또는 물류 창고 주소를 입력해주세요.")
    @Size(max = 2083, message = "최대 2083자까지 입력할 수 있습니다.")
    private String location;

    private BigDecimal locationLat;
    
    private BigDecimal locationLng;

    private String warehouseName;
}
