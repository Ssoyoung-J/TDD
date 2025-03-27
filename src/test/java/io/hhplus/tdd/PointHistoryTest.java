package io.hhplus.tdd;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PointHistoryTest {
    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private PointService pointService;

    long maxPoint = 10000L;
    long minPoint = 0L;

    @DisplayName("보유 포인트가 0이면서 충전 이력이 없는 경우 - 포인트 충전/사용 내역 조회 성공")
    @Test
    void shouldReturnEmptyHistory_WhenNoPointAndNoHistory() {
        // given
        long userId = 3L;
        Mockito.when(pointHistoryTable.selectAllByUserId(userId)).thenReturn(Collections.EMPTY_LIST);

        // when
        List<PointHistory> pointHistory = pointService.selectUserPointHistory(userId);

        // then
        assertNotNull(pointHistory, "반환된 포인트 내역 리스트는 null이 아니어야 합니다.");
        assertTrue(pointHistory.isEmpty(), "포인트 충전/사용 내역이 없습니다.");

    }

    @DisplayName("보유 포인트가 있는 경우 - 포인트 충전/사용 내역 조회 성공")
    @Test
    void shouldReturnPointHistory_WhenUserHasPoint() {
        // given
        long userId = 3L;
        long chargeAmount = 300L;
        long useAmount = 10L;
        long timeStamp = System.currentTimeMillis();

        List<PointHistory> userPointHistory = new ArrayList<>();
        for(long i = 0; i < 5; i++) {
            userPointHistory.add(new PointHistory(i+1, userId, chargeAmount, TransactionType.CHARGE ,timeStamp));
            userPointHistory.add(new PointHistory(i+1, userId, useAmount, TransactionType.USE ,timeStamp));
        }

        Mockito.when(pointHistoryTable.selectAllByUserId(userId)).thenReturn(userPointHistory);

        // when
        List<PointHistory> pointHistory = pointHistoryTable.selectAllByUserId(userId);

        // then
        assertNotNull(pointHistory, "포인트 충전/사용 내역이 있습니다.");
        assertTrue(pointHistory.size() == 10, "포인트 충전/사용 내역 조회 성공하였습니다.");

    }

}
