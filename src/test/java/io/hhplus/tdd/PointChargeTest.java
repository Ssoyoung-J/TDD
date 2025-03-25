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


    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(pointController).build();
    }
    

    @DisplayName("포인트 충전 성공")
    @Test
    void pointChargeTest() throws Exception {
        // given
        long userId = 1L;
        long chargeAmount = 10L;
        UserPoint expectedUserPoint = new UserPoint(userId, chargeAmount, System.currentTimeMillis());
        Mockito.when(pointService.chargeUserPoint(userId, chargeAmount)).thenReturn(expectedUserPoint);
        UserPoint userPoint = pointService.chargeUserPoint(userId, chargeAmount);
        System.out.println(userPoint.point());

       assertThat(userPoint).isNotNull().returns(chargeAmount, UserPoint::point);

    }


}
