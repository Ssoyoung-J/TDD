package io.hhplus.tdd;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * 단위 테스트 작성
 * */
@ExtendWith(MockitoExtension.class)
public class PointSelectTest {

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private PointService pointService;

    long maxPoint = 10000L;
    long minPoint = 0L;

    @DisplayName("보유 포인트가 있을 경우 - 포인트 조회 성공")
    @Test
    void shouldRetrivevPointsSuccessfully_WhenUserHasPoints() {
        // given
        long userId = 3L;
        long amount = 4000L;
        long timeStamp = System.currentTimeMillis();
        UserPoint userPoint = new UserPoint(userId, amount, timeStamp);
        Mockito.when(userPointTable.selectById(userId)).thenReturn(userPoint);

        // when
        UserPoint currentPoint = pointService.selectUserPointById(userId);

        // then
        assertThat(currentPoint.point()).isEqualTo(amount);
        assertThat(currentPoint.id()).isEqualTo(userId);
    }
}
