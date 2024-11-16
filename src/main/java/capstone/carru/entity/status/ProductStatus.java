package capstone.carru.entity.status;

public enum ProductStatus {
    // APPROVED: 물류 승인 완료, REJECTED: 물류 승인 거절, WAITING: 물류 승인 대기중(물류 예약의 초기 상태)
    // DRIVER_TODO: 물류 승인 완료 후 운송자가 예약한 상태(아직 운송을 시작하지 않음)
    // DRIVER_INPROGRESS: 물류 운송 진행중
    // DRIVER_FINISHED: 물류 운송 완료
    APPROVED, REJECTED, WAITING,
    DRIVER_TODO, DRIVER_INPROGRESS, DRIVER_FINISHED
}
