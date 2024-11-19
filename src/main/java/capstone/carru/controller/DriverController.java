package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.driver.GetLogisticMatchingReservingListResponse;
import capstone.carru.dto.driver.GetLogisticsMatchingDetailResponse;
import capstone.carru.dto.driver.GetLogisticsMatchingListRequest;
import capstone.carru.dto.driver.GetLogisticsMatchingListResponse;
import capstone.carru.dto.driver.GetRouteMatchingResevingListResponse;
import capstone.carru.dto.driver.ReserveRouteMatchingRequest;
import capstone.carru.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DriverController {

    private final DriverService driverService;

    @Operation(summary = "물류 매칭 리스트 조회", description = "물류 매칭 리스트를 조회할 수 있습니다.")
    @PostMapping("/v1/driver/logisticsMatching")
    public ApiResponse<Slice<GetLogisticsMatchingListResponse>> getLogisticsMatchingList(Authentication authentication,
            Pageable pageable,
            @RequestBody GetLogisticsMatchingListRequest getLogisticsMatchingListRequest) {
        String email = authentication.getName();
        return ApiResponse.success(driverService.getLogisticsMatchingListRequest(email, pageable, getLogisticsMatchingListRequest));
    }

    @Operation(summary = "물류 매칭 상세 조회", description = "물류 매칭 상세를 조회할 수 있습니다.")
    @PostMapping("/v1/driver/logisticsMatching/{logisticsMatchingId}")
    public ApiResponse<GetLogisticsMatchingDetailResponse> getLogisticsMatching(Authentication authentication,
            @PathVariable Long logisticsMatchingId) {
        String email = authentication.getName();
        return ApiResponse.success(driverService.getLogisticsMatching(email, logisticsMatchingId));
    }

    @Operation(summary = "물류 매칭 예약", description = "물류 매칭을 예약할 수 있습니다.")
    @PostMapping("/v1/driver/logisticsMatching/{logisticsMatchingId}")
    public ApiResponse<String> reserveLogisticsMatching(Authentication authentication,
            @PathVariable Long logisticsMatchingId) {
        String email = authentication.getName();
        driverService.reserveLogisticsMatching(email, logisticsMatchingId);
        return ApiResponse.success();
    }

    @Operation(summary = "경로 탐색 예약", description = "경로 탐색을 예약할 수 있습니다.")
    @PostMapping("/v1/driver/routeMatching")
    public ApiResponse<String> reserveRouteMatching(Authentication authentication,
            @RequestBody ReserveRouteMatchingRequest reserveRouteMatchingRequest) {
        String email = authentication.getName();
        driverService.reserveRouteMatching(email, reserveRouteMatchingRequest);
        return ApiResponse.success();
    }

    @Operation(summary = "물류 매칭 예약 목록", description = "예약 목록을 확인할 수 있습니다. listType = 0(Todo), 1(In-Progress), 2(Finished)")
    @PostMapping("/v1/driver/logisticsMatching/reservingList")
    public ApiResponse<Slice<GetLogisticMatchingReservingListResponse>> getLogisticMatchingReservingList(
            @RequestParam("listType") int listType, Authentication authentication,
            Pageable pageable) {
        String email = authentication.getName();
        return ApiResponse.success(driverService.getLogisticMatchingReservingList(email, pageable, listType));
    }

    @Operation(summary = "경로 탐색 예약 목록", description = "예약 목록을 확인할 수 있습니다. listType = 0(Todo), 1(In-Progress), 2(Finished")
    @PostMapping("/v1/driver/routeMatching/reservingList")
    public ApiResponse<Slice<GetRouteMatchingResevingListResponse>> getRouteMatchingReservingList(
            @RequestParam("listType") int listType, Authentication authentication,
            Pageable pageable) {
        String email = authentication.getName();
        return ApiResponse.success(driverService.getRouteMatchingReservingList(email, pageable, listType));
    }
}
