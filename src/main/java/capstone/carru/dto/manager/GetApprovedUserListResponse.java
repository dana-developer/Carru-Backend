package capstone.carru.dto.manager;

import capstone.carru.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetApprovedUserListResponse {
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String location; //차고자 또는 창고 위치
    private LocalDateTime createdAt; // 가입일

    public static GetApprovedUserListResponse of(User user) {
        return GetApprovedUserListResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .location(user.getLocation())
                .createdAt(user.getCreatedDate())
                .build();
    }
}
