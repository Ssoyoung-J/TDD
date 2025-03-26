package io.hhplus.tdd;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 단위 테스트 작성
 * */
@ExtendWith(MockitoExtension.class)
public class PointUseTest {
    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private PointService pointService;

    long maxPoint = 10000L;
    long minPoint = 0L;

    @DisplayName("보유 포인트가 사용 포인트보다 클 경우 - 포인트 사용 성공")
    @Test
    void shouldUseSuccessfully_WhenUserHasSufficientPoints() {
        // given
        long userId = 2L;
        long chargedAmount = 5000L;
        long usedAmount = 1500L;
        long timeStamp = System.currentTimeMillis();
        long newAmount = chargedAmount - usedAmount;
        UserPoint chargedPoint = new UserPoint(userId, chargedAmount, timeStamp);
        UserPoint usedPoint = new UserPoint(userId, newAmount, timeStamp);
        Mockito.when(userPointTable.selectById(userId)).thenReturn(chargedPoint);
        Mockito.when(userPointTable.insertOrUpdate(userId, newAmount)).thenReturn(usedPoint);

        // when
        UserPoint userPoint = pointService.useUserPoint(userId, usedAmount);

        // then
        assertThat(userPoint.point()).isEqualTo(newAmount);
    }

    @DisplayName("보유 포인트와 사용 포인트의 값이 같을 경우 - 포인트 사용 성공")
    @Test
    void shouldUseSuccessfully_WhenUserHasExactPoints() {
        // given
        long userId = 2L;
        long chargedAmount = 9000L;
        long usedAmount = 9000L;
        long timeStamp = System.currentTimeMillis();
        long newAmount = chargedAmount - usedAmount;
        UserPoint chargedPoint = new UserPoint(userId, chargedAmount, timeStamp);
        UserPoint usedPoint = new UserPoint(userId, newAmount, timeStamp);
        Mockito.when(userPointTable.selectById(userId)).thenReturn(chargedPoint);
        Mockito.when(userPointTable.insertOrUpdate(userId, newAmount)).thenReturn(usedPoint);

        // when
        UserPoint userPoint = pointService.useUserPoint(userId, usedAmount);

        // then
        assertThat(userPoint.point()).isEqualTo(0L);
    }

    @DisplayName("보유 포인트가 0일 때 - 포인트 사용 불가")
    @Test
    void shouldUseSuccessfully_WhenUserHasZeroPoint() {
        // given
        long userId = 2L;
        long amount = 100L;
        long timeStamp = System.currentTimeMillis();
        UserPoint emptyPoint = new UserPoint(userId, 0L, timeStamp);
        Mockito.when(userPointTable.selectById(userId)).thenReturn(emptyPoint);

        // when & then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            pointService.useUserPoint(userId,amount);
        });

        assertEquals("보유 포인트가 0이므로 포인트 사용 불가합니다.", exception.getMessage());
    }

    @DisplayName("보유 포인트가 사용 포인트 미만일 경우 - 포인트 사용 불가")
    @Test
    void shouldUseSuccessfully_WhenUserHasInSufficientPoints() {
        // given
        long userId = 2L;
        long chargedAmount = 4500L;
        long usedAmount = 5000L;
        long timeStamp = System.currentTimeMillis();
        UserPoint chargedPoint = new UserPoint(userId, chargedAmount, timeStamp);
        Mockito.when(userPointTable.selectById(userId)).thenReturn(chargedPoint);

        // when & then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            pointService.useUserPoint(userId, usedAmount);
        });

        assertEquals("보유 포인트를 초과한 포인트 사용 불가합니다.", exception.getMessage());

    }

}
