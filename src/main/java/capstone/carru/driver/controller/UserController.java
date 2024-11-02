package capstone.carru.driver.controller;

import capstone.carru.driver.dto.ApiResponse;
import capstone.carru.driver.dto.CreateUserRequest;
import capstone.carru.driver.service.UserService;
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
