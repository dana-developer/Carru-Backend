package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.shipper.RegisterLogisticsRequest;
import capstone.carru.entity.Warehouse;
import capstone.carru.service.ShipperService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ShipperController {

    private final ShipperService shipperService;

    @Operation(summary = "물류 등록", description = "화주가 물류를 등록합니다.")
    @PostMapping("/v1/shipper/logistics")
    public ApiResponse<String> registerLogistics(@RequestBody RegisterLogisticsRequest registerLogisticsRequest) {
        shipperService.registerLogistics(registerLogisticsRequest);
        return ApiResponse.success();
    }
}