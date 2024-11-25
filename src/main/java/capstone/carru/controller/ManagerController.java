package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.manager.GetApprovedLogisticsListDetailResponse;
import capstone.carru.dto.manager.GetApprovedLogisticsListResponse;
import capstone.carru.dto.manager.GetApprovedUserListResponse;
import capstone.carru.dto.manager.GetApprovingLogisticsListResponse;
import capstone.carru.dto.manager.GetApprovingUserListResponse;
import capstone.carru.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ManagerController {
    private final ManagerService managerService;

    @Operation(summary = "사용자 가입 미승인 목록 조회", description = "사용자 가입 미승인 목록을 조회할 수 있습니다. listType = 0(화물기사), 1(화주)")
    @GetMapping("/v1/manager/approvingList/user")
    public ApiResponse<Slice<GetApprovingUserListResponse>> getApprovingList(
            Authentication authentication,
            @RequestParam("listType") int listType, Pageable pageable) {
        String email = authentication.getName();
        return ApiResponse.success(managerService.getApprovingList(email, listType, pageable));
    }

    @Operation(summary = "사용자 가입 승인", description = "사용자 가입을 승인할 수 있습니다. listType = 0(화물기사), 1(화주)")
    @PatchMapping("/v1/manager/approvingList/user/{userId}")
    public ApiResponse<String> approveUser(
            Authentication authentication, @PathVariable Long userId) {
        String email = authentication.getName();
        managerService.approveUser(email, userId);
        return ApiResponse.success();
    }


    @Operation(summary = "물류 미승인 목록 조회", description = "물류 미승인 목록을 조회할 수 있습니다")
    @GetMapping("/v1/manager/approvingList/logistics")
    public ApiResponse<Slice<GetApprovingLogisticsListResponse>> getApprovingLogisticsList(
            Authentication authentication, Pageable pageable) {
        String email = authentication.getName();
        return ApiResponse.success(managerService.getApprovingLogisticsList(email, pageable));
    }

    @Operation(summary = "물류 승인", description = "물류를 승인할 수 있습니다.")
    @PatchMapping("/v1/manager/approvingList/logistics/{productId}")
    public ApiResponse<String> approveLogistics(
            Authentication authentication, @PathVariable Long productId) {
        String email = authentication.getName();
        managerService.approveLogistics(email, productId);
        return ApiResponse.success();
    }

    @Operation(summary = "사용자 가입 승인 목록 조회", description = "가입 승인된 사용자 목록을 조회할 수 있습니다. listType = 0(화물기사), 1(화주)")
    @GetMapping("/v1/manager/approvedList/user")
    public ApiResponse<Slice<GetApprovedUserListResponse>> getApprovedList(
            Authentication authentication,
            @RequestParam("listType") int listType, Pageable pageable) {
        String email = authentication.getName();
        return ApiResponse.success(managerService.getApprovedUserList(email, listType, pageable));
    }

    @Operation(summary = "물류 승인 목록 조회", description = "물류 승인된 목록을 조회할 수 있습니다.")
    @GetMapping("/v1/manager/approvedList/logistics")
    public ApiResponse<Slice<GetApprovedLogisticsListResponse>> getApprovedLogisticsList(
            Authentication authentication, Pageable pageable) {
        String email = authentication.getName();
        return ApiResponse.success(managerService.getApprovedLogisticsList(email, pageable));
    }

    @Operation(summary = "물류 승인 목록 상세 조회", description = "물류 승인된 목록을 상세 조회할 수 있습니다.")
    @GetMapping("/v1/manager/approvedList/logistics/{productId}")
    public ApiResponse<GetApprovedLogisticsListDetailResponse> getApprovedLogisticsListDetail(
            Authentication authentication, @PathVariable Long productId) {
        String email = authentication.getName();
        return ApiResponse.success(managerService.getApprovedLogisticsListDetail(email, productId));
    }
}
