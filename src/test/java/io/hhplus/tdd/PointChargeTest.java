package io.hhplus.tdd;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

/**
 * 단위 테스트 작성
 * */
@ExtendWith(MockitoExtension.class)
public class PointChargeTest {
    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

//    @Mock
//    private TransactionType type;

    @InjectMocks
    private PointService pointService;

    long maxPoint = 10000L;
    long minPoint = 0L;

    @DisplayName("보유 포인트가 0일 경우 - 포인트 충전 성공")
    @Test
    void shouldChargeSuccessfully_WhenUserHasZeroPoints () {
        // given
        long userId = 1L;
        long chargeAmount = 10L;
        long timeStamp = System.currentTimeMillis();
        UserPoint emptyUserPoint = new UserPoint(userId, minPoint, timeStamp);
        UserPoint chargedUserPoint = new UserPoint(userId, chargeAmount, timeStamp);

        Mockito.when(userPointTable.selectById(userId)).thenReturn(emptyUserPoint);
        Mockito.when(userPointTable.insertOrUpdate(userId, chargeAmount)).thenReturn(chargedUserPoint);

        // when
        UserPoint userPoint = pointService.chargeUserPoint(userId, chargeAmount);

        // then
        assertThat(userPoint.point()).isEqualTo(chargeAmount);
    }

    @DisplayName("충전 포인트 : N, 보유 포인트 : M일 경우 - 포인트 충전 성공")
    @Test
    void shouldChargeSuccessfully_WhenUserHasMPoints () {
        // given
        long userId = 1L;
        long chargedPoint = 1000L;
        long chargeAmount = 100L;
        long newAmount =chargeAmount + chargedPoint;
        UserPoint chargedUserPoint = new UserPoint(userId, chargedPoint, System.currentTimeMillis());
        UserPoint chargUserPoint = new UserPoint(userId, chargedPoint+ chargeAmount, System.currentTimeMillis());
        Mockito.when(userPointTable.selectById(userId)).thenReturn(chargedUserPoint);
        Mockito.when(userPointTable.insertOrUpdate(userId,newAmount)).thenReturn(chargUserPoint);

        // when
        UserPoint userPoint = pointService.chargeUserPoint(userId, chargeAmount);

        // then
        assertThat(userPoint.point()).isEqualTo(chargedPoint + chargeAmount);
    }

    @DisplayName("보유 포인트가 최대값일 경우 - 포인트 충전 실패")
    @Test
    void shouldFailToCharge_WhenUserHasMaxPoints() {
        // given
        long userId = 1L;
        long chargeAmount = 100L;
        UserPoint userMaxPoint = new UserPoint(userId, maxPoint, System.currentTimeMillis());
        // Mock 대상은 pointService가 아니라 userPointTable이어야 함
        Mockito.when(userPointTable.selectById(userId)).thenReturn(userMaxPoint);

        // when & then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            pointService.chargeUserPoint(userId, chargeAmount);
        });

        assertEquals("포인트 최대값 초과되었습니다.", exception.getMessage());
    }

    @DisplayName("충전 후 포인트가 최대값을 초과했을 경우 - 포인트 충전 실패")
    @Test
    void shouldFailToCharge_WhenExceedingMaxPoint() {
        // given
        long userId = 1L;
        long chargeAmount = 100L;
        long chargedAmount = 9999L;
        UserPoint chargedUserPoint = new UserPoint(userId, chargedAmount, System.currentTimeMillis());
        Mockito.when(userPointTable.selectById(userId)).thenReturn(chargedUserPoint);

        // when
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            pointService.chargeUserPoint(userId, chargeAmount);
        });

        assertEquals("포인트 최대값 초과되었습니다.", exception.getMessage());
    }

    @DisplayName("충전 포인트가 음수일 경우 - 포인트 충전 실패")
    @Test
    void shouldFailCharge_WhenNegativeAmount() {
        // given
        long userId = 1L;
        long amount = -1000L;

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,  () -> {
            pointService.chargeUserPoint(userId, amount);
        });

        assertEquals("충전 포인트는 0보다 커야합니다.", exception.getMessage());

    }

}
