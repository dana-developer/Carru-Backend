package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.driver.GetLogisticsMatchingListRequest;
import capstone.carru.dto.driver.GetLogisticsMatchingListResponse;
import capstone.carru.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DriverController {

    private final DriverService driverService;

    @GetMapping("/v1/driver/logisticsMatching")
    public ApiResponse<Slice<GetLogisticsMatchingListResponse>> getLogisticsMatchingList(Authentication authentication,
            Pageable pageable,
            @RequestBody GetLogisticsMatchingListRequest getLogisticsMatchingListRequest) {
        String email = authentication.getName();
        return ApiResponse.success(driverService.getLogisticsMatchingListRequest(email, pageable, getLogisticsMatchingListRequest));
    }
}
