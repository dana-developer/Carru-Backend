package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.User.GetApprovingListResponse;
import capstone.carru.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ManagerController {
    private final ManagerService managerService;

    @Operation(summary = "승인 목록 조회", description = "승인 목록을 조회할 수 있습니다. listType = 0(화물기사), 1(화주)")
    @GetMapping("/v1/manager/approvingList")
    public ApiResponse<Slice<GetApprovingListResponse>> getApprovingList(
            Authentication authentication,
            @RequestParam("listType") int listType, Pageable pageable) {
        String email = authentication.getName();
        return ApiResponse.success(managerService.getApprovingList(email, listType, pageable));
    }
}
