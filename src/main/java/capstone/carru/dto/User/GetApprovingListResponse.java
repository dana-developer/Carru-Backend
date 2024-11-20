package capstone.carru.dto.User;

import capstone.carru.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetApprovingListResponse {
    private Long userId;
    private String email;
    private String name;
    private String phoneNumber;
    private LocalDateTime createdAt; // 가입일

    public static GetApprovingListResponse of(User user) {
        return GetApprovingListResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedDate())
                .build();
    }
}
