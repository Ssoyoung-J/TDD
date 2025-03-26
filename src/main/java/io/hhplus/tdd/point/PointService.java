package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class PointService {

    /**
     * 해당 Table을 활용하여 기능 작성
     */
    @Autowired
    private UserPointTable userPointTable;
    
    // point의 최대, 최소값 설정
    private final long maxPoint = 10000L;
    long minPoint = 0L;

    /**
     * 특정 유저의 포인트를 조회하는 기능
     * */
    public UserPoint selectUserPointById(long id) {
        // currentPoint 조회
        UserPoint currentPoint = userPointTable.selectById(id);

        return currentPoint;
    }

    /**
     *
     * 특정 유저의 포인트를 충전하는 기능
     */
    public UserPoint chargeUserPoint(long id, long amount) {
        // currentPoint 조회
        UserPoint currentPoint = userPointTable.selectById(id);
        if(amount < 0) {
            throw new RuntimeException("충전 포인트는 0보다 커야합니다.");
        }

        long newAmount = currentPoint.point() +amount;

        if(newAmount > maxPoint) {
            throw new RuntimeException("포인트 최대값 초과되었습니다.");
        }
        UserPoint result = userPointTable.insertOrUpdate(id, newAmount);

        return result;
    }

}
