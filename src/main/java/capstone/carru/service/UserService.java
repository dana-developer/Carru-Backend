package capstone.carru.service;

import capstone.carru.dto.ErrorCode;
import capstone.carru.dto.User.CreateUserRequest;
import capstone.carru.dto.User.GetProfileResponse;
import capstone.carru.dto.User.LoginRequest;
import capstone.carru.dto.User.UpdateNameRequest;
import capstone.carru.dto.User.GetLocationResponse;
import capstone.carru.entity.User;
import capstone.carru.entity.Warehouse;
import capstone.carru.entity.status.UserStatus;
import capstone.carru.exception.NotFoundException;
import capstone.carru.repository.WarehouseRepository;
import capstone.carru.repository.user.UserRepository;
import capstone.carru.exception.InvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public User validateUser(String email){
        User user = userRepository.findByEmailAndDeletedDateIsNull(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_MEMBER));

        if(user.getApprovedDate() == null) {
            throw new InvalidException(ErrorCode.INVALID_MEMBER);
        }

        return user;
    }

    @Transactional
    public void createUser(int userStatus, CreateUserRequest createUserRequest) {
        //1. 이미 가입한 회원인지 확인
        if (userRepository.findByEmailAndDeletedDateIsNull(createUserRequest.getEmail()).isPresent()) {
            throw new InvalidException(ErrorCode.INVALID_DUPLICATED_EMAIL);
        }

        //2. 가입한 회원이 아니라면 회원가입 진행
        UserStatus status = null;
        if(userStatus == 0) { // 0: 화물 기사
            status = UserStatus.DRIVER;
        } else if(userStatus == 1) { // 1: 화물 주인
            status = UserStatus.OWNER;
        } else { // 잘못된 요청
            throw new InvalidException(ErrorCode.INVALID);
        }

        User user = User.builder()
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .name(createUserRequest.getName())
                .phoneNumber(createUserRequest.getPhoneNumber())
                .location(createUserRequest.getLocation())
                .locationLat(createUserRequest.getLocationLat())
                .locationLng(createUserRequest.getLocationLng())
                .userStatus(status)
                .build();

        userRepository.save(user);

        if(userStatus == 1) {
            Warehouse warehouse = Warehouse.builder()
                    .user(user)
                    .name(createUserRequest.getWarehouseName())
                    .location(createUserRequest.getLocation())
                    .locationLat(createUserRequest.getLocationLat())
                    .locationLng(createUserRequest.getLocationLng())
                    .build();

            warehouseRepository.save(warehouse);
        }
    }

    @Transactional(readOnly = true)
    public String loginUser(int userStatus, LoginRequest loginRequest) {
        if(userStatus != 0 && userStatus != 1 && userStatus != 2) {
            throw new InvalidException(ErrorCode.INVALID);
        }

        User user = validateUser(loginRequest.getEmail());

        if(user.getUserStatus() == UserStatus.OWNER && userStatus != 1) {
            throw new InvalidException(ErrorCode.INVALID_MEMBER);
        } else if(user.getUserStatus() == UserStatus.DRIVER && userStatus != 0) {
            throw new InvalidException(ErrorCode.INVALID_MEMBER);
        } else if (user.getUserStatus() == UserStatus.MANAGER && userStatus != 2) {
            throw new InvalidException(ErrorCode.INVALID_MEMBER);
        }

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new InvalidException(ErrorCode.INVALID_PASSWORD);
        }

        return jwtTokenProvider.generateToken(user.getEmail(), "USER");
    }

    @Transactional(readOnly = true)
    public GetProfileResponse getProfile(String email) {
        User user = validateUser(email);
        return GetProfileResponse.of(user.getEmail(), user.getName());
    }

    @Transactional
    public void updateName(String email, UpdateNameRequest updateNameRequest) {
        User user = validateUser(email);
        user.updateName(updateNameRequest.getName());
    }

    @Transactional(readOnly = true)
    public GetLocationResponse getLocation(String email) {
        User user = validateUser(email);
        return GetLocationResponse.of(user);
    }
}
