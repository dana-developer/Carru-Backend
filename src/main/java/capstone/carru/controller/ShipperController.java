package capstone.carru.controller;

import capstone.carru.dto.ApiResponse;
import capstone.carru.dto.shipper.*;
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
    public ApiResponse<List<SearchWarehouseResponse>> searchWarehouse(@RequestParam("keyword") String keyword) {
        List<Warehouse> warehouses = warehouseRepository.findByNameContainingOrLocationContaining(keyword, keyword);
        List<SearchWarehouseResponse> searchWarehouseRequests = warehouses.stream()
                .map(SearchWarehouseResponse::fromEntity)
                .toList();
        return ApiResponse.success(searchWarehouseRequests);
    }

    @Operation(summary = "미승인 물류 리스트 조회", description = "미승인 물류 리스트를 조회합니다.")
    @GetMapping("/v1/shipper/logistics/pending")
    public ApiResponse<List<LogisticsListResponse>> getPendingLogistics(Authentication authentication) {
        String email = authentication.getName();
        List<LogisticsListResponse> pendingLogistics = shipperService.getPendingLogistics(email);
        return ApiResponse.success(pendingLogistics);
    }

    @Operation(summary = "미승인 물류 상세 조회", description = "미승인 물류의 상세 정보를 조회합니다.")
    @GetMapping("/v1/shipper/logistics/pending/{productId}")
    public ApiResponse<PendingLogisticsResponse> getPendingLogisticsDetail(Authentication authentication, @PathVariable Long productId) {
        String email = authentication.getName();
        PendingLogisticsResponse detail = shipperService.getPendingLogisticsDetail(email, productId);
        return ApiResponse.success(detail);
    }

    @Operation(summary = "미승인 물류 삭제", description = "미승인 물류를 삭제합니다.")
    @DeleteMapping("/v1/shipper/logistics/pending/{productId}")
    public ApiResponse<String> deletePendingLogistics(Authentication authentication, @PathVariable Long productId) {
        String email = authentication.getName();
        shipperService.deletePendingLogistics(email, productId);
        return ApiResponse.success("미승인 물류가 삭제되었습니다.");
    }

    @Operation(summary = "미승인 물류 수정", description = "미승인 물류를 수정합니다.")
    @PutMapping("/v1/shipper/logistics/pending/{productId}")
    public ApiResponse<String> updatePendingLogistics(
            Authentication authentication,
            @PathVariable Long productId,
            @RequestBody RegisterLogisticsRequest updateRequest) {
        String email = authentication.getName();
        shipperService.updatePendingLogistics(email, productId, updateRequest);
        return ApiResponse.success("미승인 물류가 수정되었습니다.");
    }

    @Operation(summary = "승인된 물류 리스트 조회", description = "승인된 물류 리스트를 조회합니다.")
    @GetMapping("/v1/shipper/logistics/approved")
    public ApiResponse<List<LogisticsListResponse>> getApprovedLogistics(Authentication authentication, @RequestParam("listType") int listType) {
        String email = authentication.getName();
        List<LogisticsListResponse> todoLogistics = shipperService.getApprovedLogistics(email, listType);
        return ApiResponse.success(todoLogistics);
    }

    @Operation(summary = "승인된 물류 상세 조회", description = "승인된 물류의 상세 정보를 조회합니다.")
    @GetMapping("/v1/shipper/logistics/approved/{productId}")
    public ApiResponse<ApprovedLogisticsResponse> getApprovedLogisticsDetail(Authentication authentication, @PathVariable Long productId, @RequestParam("listType") int listType) {
        String email = authentication.getName();
        ApprovedLogisticsResponse detail = shipperService.getApprovedLogisticsDetail(email, productId, listType);
        return ApiResponse.success(detail);
    }
}