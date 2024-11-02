package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.CreateUserRequest;
import capstone.carru.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    @PostMapping("/v1/user/sign-up")
    public ApiResponse<String> createUser(CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return ApiResponse.success();
    }
}
