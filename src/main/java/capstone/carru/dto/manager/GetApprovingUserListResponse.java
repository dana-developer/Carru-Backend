package capstone.carru.dto.manager;

import capstone.carru.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetApprovingUserListResponse {
    private Long userId;
    private String email;
    private String name;
    private String phoneNumber;
    private LocalDateTime createdAt; // 가입일

    public static GetApprovingUserListResponse of(User user) {
        return GetApprovingUserListResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedDate())
                .build();
    }
}
