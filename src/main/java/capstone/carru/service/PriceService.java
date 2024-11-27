package capstone.carru.service;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService {

    public Long calculateShippingPrice(double weight, long distance) {
         int[][] priceTable = {
                {4, 5, 7, 8, 11, 13, 18, 20},  // 20km
                {5, 6, 8, 9, 12, 14, 19, 21},  // 30km
                {6, 7, 9, 10, 13, 15, 20, 22}, // 50km
                {7, 8, 10, 11, 14, 17, 21, 23}, // 70km
                {8, 9, 11, 12, 16, 19, 22, 25}, // 90km
                {9, 10, 12, 13, 17, 20, 24, 27}, // 110km
                {10, 11, 13, 14, 18, 21, 25, 28}, // 130km
                {11, 12, 14, 15, 19, 23, 27, 30}, // 150km
                {12, 13, 15, 16, 20, 25, 28, 31}, // 170km
                {13, 14, 18, 19, 23, 27, 30, 32}, // 200km
                {15, 16, 19, 20, 25, 33, 39, 42}, // 250km
                {17, 18, 25, 26, 30, 37, 45, 48}, // 300km
                {19, 20, 27, 28, 33, 38, 50, 53}, // 350km
                {20, 21, 28, 30, 35, 42, 55, 58}, // 400km
                {22, 23, 30, 32, 37, 44, 58, 62}, // 450km
                {25, 26, 32, 34, 38, 46, 60, 64}, // 500km
                {27, 28, 35, 37, 41, 48, 63, 67}, // 550km
                {30, 31, 38, 40, 43, 50, 65, 70}  // 600km
        };

        List<Integer> distances = Arrays.asList(20, 30, 50, 70, 90, 110, 130, 150,
                170, 200, 250, 300, 350, 400, 450, 500, 550, 600);
        List<Double> weights = Arrays.asList(1.0, 1.4, 2.5, 3.5, 5.0, 11.0, 18.0, 25.0);

        // 거리와 화물 무게의 유효 값 찾기
        int validDistance = 600; // 기본값
        for (int d : distances) {
            if (d >= distance) {
                validDistance = d;
                break;
            }
        }

        double validWeight = 25.0; // 기본값
        for (double w : weights) {
            if (w >= weight) {
                validWeight = w;
                break;
            }
        }

        // 인덱스 얻기
        int distanceIndex = distances.indexOf(validDistance);
        int weightIndex = weights.indexOf(validWeight);

        // 가격 계산
        return (long) priceTable[distanceIndex][weightIndex];
    }
}
