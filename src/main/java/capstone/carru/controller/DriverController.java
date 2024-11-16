package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.driver.GetLogisticsMatchingDetailResponse;
import capstone.carru.dto.driver.GetLogisticsMatchingListRequest;
import capstone.carru.dto.driver.GetLogisticsMatchingListResponse;
import capstone.carru.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DriverController {

    private final DriverService driverService;

    @Operation(summary = "운송 매칭 리스트 조회", description = "운송 매칭 리스트를 조회할 수 있습니다.")
    @GetMapping("/v1/driver/logisticsMatching")
    public ApiResponse<Slice<GetLogisticsMatchingListResponse>> getLogisticsMatchingList(Authentication authentication,
            Pageable pageable,
            @RequestBody GetLogisticsMatchingListRequest getLogisticsMatchingListRequest) {
        String email = authentication.getName();
        return ApiResponse.success(driverService.getLogisticsMatchingListRequest(email, pageable, getLogisticsMatchingListRequest));
    }

    @Operation(summary = "운송 매칭 상세 조회", description = "운송 매칭 상세를 조회할 수 있습니다.")
    @GetMapping("/v1/driver/logisticsMatching/{logisticsMatchingId}")
    public ApiResponse<GetLogisticsMatchingDetailResponse> getLogisticsMatching(Authentication authentication,
            @PathVariable Long logisticsMatchingId) {
        String email = authentication.getName();
        return ApiResponse.success(driverService.getLogisticsMatching(email, logisticsMatchingId));
    }

    @Operation(summary = "운송 매칭 예약", description = "운송 매칭을 예약할 수 있습니다.")
    @PostMapping("/v1/driver/logisticsMatching/{logisticsMatchingId}")
    public ApiResponse<GetLogisticsMatchingDetailResponse> reserveLogisticsMatching(Authentication authentication,
            @PathVariable Long logisticsMatchingId) {
        String email = authentication.getName();
        driverService.reserveLogisticsMatching(email, logisticsMatchingId);
        return ApiResponse.success();
    }
}
