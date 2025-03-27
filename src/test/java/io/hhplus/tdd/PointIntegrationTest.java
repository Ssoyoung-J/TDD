package io.hhplus.tdd;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PointIntegrationTest {


    @Autowired
    PointService pointService;

    @Autowired
    UserPointTable userPointTable;

    private final ConcurrentHashMap<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    @DisplayName("1명의 사용자가 동시에 여러 번 포인트 충전 요청 - CountDownLatch")
    @Test
    void shouldHandleMultipleSimultaneousCharges_ForSingleUser() throws InterruptedException{
        // given
        long userId = 1L;
        CountDownLatch countDownLatch = new CountDownLatch(100);
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // when
        for (int i = 1; i <= 100; i++) {
//            ReentrantLock lock = lockMap.computeIfAbsent(userId, k -> new ReentrantLock());
//            // 사용자별 락
//            lock.lock();
            try {
                pointService.chargeUserPoint(userId, 100L);
            } finally {
//                lock.unlock();
                countDownLatch.countDown();
            }
        }

        countDownLatch.await();
        executor.shutdown();

        List<PointHistory> pointHistoryList = pointService.selectUserPointHistory(userId);

        // then
        assertEquals(pointHistoryList.size(), 100);
        assertEquals(10000L, userPointTable.selectById(userId).point());
    }

    @DisplayName("동시에 여러 사용자가 포인트 사용을 요청했을 경우 - CountDownLatch")
    @Test
    void shouldHandleConcurrentPointUses_WithCountDownLatch() throws InterruptedException{
        // given
        CountDownLatch countDownLatch = new CountDownLatch(100);
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // when
        for (int i = 1; i <= 100; i++) {
           long userId = (long) i;
           pointService.chargeUserPoint(userId, 100L);
//           ReentrantLock lock = lockMap.computeIfAbsent(userId, k -> new ReentrantLock());
            // 사용자별 락
//           lock.lock();
           try {
               pointService.useUserPoint(userId, 10L);
           } finally {
//               lock.unlock();
               countDownLatch.countDown();
           }
        }

        countDownLatch.await();
        executor.shutdown();

        // then
        for(int i =1; i<= 100; i++) {
            long userId = (long) i;
            assertEquals(userPointTable.selectById(userId).point(), 90L);
        }
    }

    /**
     * 추가로 테스트 해보고 싶은 통합 테스트 시나리오
     *
     * 포인트를 충전 후 포인트 사용 시 포인트가 부족하면 예외 발생
     * 동시에 포인트 충전 후 최대 포인트 값 넘어가면 예외 발생
     * 위에 실행해본 동시성 제어 방법과 다른 방법을 활용하여 동시성 제어 테스트를 해볼 것
     * */
}
