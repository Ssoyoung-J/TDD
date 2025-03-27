package io.hhplus.tdd;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
        long amount = 9000L;

    }

}
