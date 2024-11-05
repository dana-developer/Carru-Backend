package capstone.carru.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class GetProfileResponse {
    private String email;
    private String name;
}
