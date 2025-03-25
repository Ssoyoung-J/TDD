package io.hhplus.tdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.point.PointController;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static java.time.LocalTime.now;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 단위 테스트 작성
 * */
@ExtendWith(MockitoExtension.class)
public class PointChargeTest {

    @InjectMocks
    private PointController pointController;

    @Mock
    private PointService pointService;


    @DisplayName("보유 포인트가 0일 경우 - 포인트 충전 성공")
    @Test
    void pointChargeTest() {
        // given
        long userId = 1L;
        long chargeAmount = 10L;
        UserPoint expectedUserPoint = new UserPoint(userId, chargeAmount, System.currentTimeMillis());
        Mockito.when(pointService.chargeUserPoint(userId, chargeAmount)).thenReturn(expectedUserPoint);
        // when
        UserPoint userPoint = pointService.chargeUserPoint(userId, chargeAmount);

        // then
        assertThat(userPoint.point()).isEqualTo(chargeAmount);
    }


}
