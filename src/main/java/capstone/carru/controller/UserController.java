package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.User.CreateUserRequest;
import capstone.carru.dto.User.GetProfileResponse;
import capstone.carru.dto.User.LoginRequest;
import capstone.carru.dto.User.LoginResponse;
import capstone.carru.dto.User.UpdateNameRequest;
import capstone.carru.dto.User.GetLocationResponse;
import capstone.carru.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입을 할 수 있습니다. userStatus = 0(화물기사), 1(화주), 2(관리자)")
    @PostMapping("/v1/user/sign-up")
    public ApiResponse<String> createUser(@RequestParam("userStatus") int userStatus,
            @RequestBody CreateUserRequest createUserRequest) {
        userService.createUser(userStatus, createUserRequest);
        return ApiResponse.success();
    }

    @Operation(summary = "로그인", description = "로그인을 할 수 있습니다. userStatus = 0(화물기사), 1(화주), 2(관리자)")
    @PostMapping("/v1/user/login")
    public ApiResponse<LoginResponse> loginUser(@RequestParam("userStatus") int userStatus,
            @RequestBody LoginRequest loginRequest) {
        String token = userService.loginUser(userStatus, loginRequest);

        return ApiResponse.success(LoginResponse.of(token));
    }

    @Operation(summary = "프로필 조회", description = "프로필을 조회할 수 있습니다.")
    @GetMapping("/v1/user/profile")
    public ApiResponse<GetProfileResponse> loginUser(Authentication authentication) {
        String email = authentication.getName();
        return ApiResponse.success(userService.getProfile(email));
    }

    @Operation(summary = "이름 변경", description = "이름을 변경할 수 있습니다.")
    @PatchMapping("/v1/user/name")
    public ApiResponse<String> updateName(Authentication authentication,
            @RequestBody UpdateNameRequest updateNameRequest) {
        String email = authentication.getName();
        userService.updateName(email, updateNameRequest);
        return ApiResponse.success();
    }

    @Operation(summary = "차고지/물류 창고 조회", description = "차고지/물류 창고를 조회할 수 있습니다.")
    @GetMapping("/v1/user/location")
    public ApiResponse<GetLocationResponse> getLocation(Authentication authentication) {
        String email = authentication.getName();
        return ApiResponse.success(userService.getLocation(email));
    }
}
