package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.User.CreateUserRequest;
import capstone.carru.dto.User.GetProfileResponse;
import capstone.carru.dto.User.LoginRequest;
import capstone.carru.dto.User.LoginResponse;
import capstone.carru.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/v1/user/login")
    public ApiResponse<LoginResponse> loginUser(LoginRequest loginRequest) {
        String token = userService.loginUser(loginRequest);
        return ApiResponse.success(LoginResponse.of(token));
    }

    @GetMapping("/v1/user/profile")
    public ApiResponse<GetProfileResponse> loginUser(Authentication authentication) {
        String email = authentication.getName();
        return ApiResponse.success(userService.getProfile(email));
    }
}