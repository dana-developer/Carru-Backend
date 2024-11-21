package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.shipper.PendingLogisticsResponse;
import capstone.carru.dto.shipper.RegisterLogisticsRequest;
import capstone.carru.dto.shipper.SearchWarehouseResponse;
import capstone.carru.entity.Warehouse;
import capstone.carru.repository.WarehouseRepository;
import capstone.carru.service.ShipperService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ShipperController {

    private final ShipperService shipperService;
    private final WarehouseRepository warehouseRepository;

    @Operation(summary = "물류 등록", description = "화주가 물류를 등록합니다.")
    @PostMapping("/v1/shipper/logistics")
    public ApiResponse<String> registerLogistics(Authentication authentication, @RequestBody RegisterLogisticsRequest registerLogisticsRequest) {
        String email = authentication.getName();
        shipperService.registerLogistics(email, registerLogisticsRequest);
        return ApiResponse.success("물류가 등록되었습니다.");
    }

    @Operation(summary = "창고 검색", description = "키워드를 기반으로 창고를 검색합니다.")
    @GetMapping("/v1/shipper/search")
    public ApiResponse<List<SearchWarehouseResponse>> searchWarehouse(@RequestParam String keyword) {
        List<Warehouse> warehouses = warehouseRepository.findByNameContainingOrLocationContaining(keyword, keyword);
        List<SearchWarehouseResponse> searchWarehouseRequests = warehouses.stream()
                .map(SearchWarehouseResponse::fromEntity)
                .toList();
        return ApiResponse.success(searchWarehouseRequests);
    }

    @Operation(summary = "미승인 물류 리스트 조회", description = "해당 화주가 등록했으나 관리자 승인 대기 중인 물류 리스트를 조회합니다.")
    @GetMapping("/v1/shipper/logistics/pending")
    public ApiResponse<List<PendingLogisticsResponse>> getPendingLogistics(Authentication authentication) {
        String email = authentication.getName();
        List<PendingLogisticsResponse> pendingLogistics = shipperService.getPendingLogistics(email);
        return ApiResponse.success(pendingLogistics);
    }
}