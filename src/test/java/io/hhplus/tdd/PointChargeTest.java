package io.hhplus.tdd;

import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static java.time.LocalTime.now;

public class PointChargeTest {

    /* API Test를 위한 Mock 객체 생성 필요*/
    long currentTime = System.currentTimeMillis();
    UserPoint userPoint = Mockito.mock(new UserPoint(1, 10, currentTime));
//    @Test
//    public void chargeApiTest(long id, long amount) {
//        Mockito.when().thenReturn();
//    }

}
