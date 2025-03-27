package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PointService {

    /**
     * 해당 Table을 활용하여 기능 작성
     */
    @Autowired
    private UserPointTable userPointTable;

    @Autowired
    private PointHistoryTable pointHistoryTable;
    
    // point의 최대, 최소값 설정
    private final long maxPoint = 10000L;
    long minPoint = 0L;
    
    /**
     *
     * 특정 유저의 포인트를 충전하는 기능
     */
    public UserPoint chargeUserPoint(long id, long amount) {
        // currentPoint 조회
        UserPoint currentPoint = userPointTable.selectById(id);
        
        // 충전 포인트가 0보다 작을 경우 예외 처리
        if(amount < 0) {
            throw new RuntimeException("충전 포인트는 0보다 커야합니다.");
        }

        long newAmount = currentPoint.point() +amount;

        // 충전 포인트 + 보유 포인트가 최대값보다 초과일 경우 예외 처리
        if(newAmount > maxPoint) {
            throw new RuntimeException("포인트 최대값 초과되었습니다.");
        }
        UserPoint result = userPointTable.insertOrUpdate(id, newAmount);
        pointHistoryTable.insert(id, newAmount, TransactionType.CHARGE, result.updateMillis());

        return result;
    }
    
    /**
     * 특정 유저의 포인트를 사용하는 기능
     * */
    public UserPoint useUserPoint(long id, long amount) {
        // currentPoint 조회
        UserPoint currentPoint = userPointTable.selectById(id);
        
        // 보유 포인트가 0일 경우 예외처리
        if(currentPoint.point() ==  0) {
            throw new RuntimeException("보유 포인트가 0이므로 포인트 사용 불가합니다.");
        }

        // 보유 포인트가 사용 포인트보다 미만일 경우 예외처리
        if(currentPoint.point() < amount ) {
            throw new RuntimeException("보유 포인트를 초과한 포인트 사용 불가합니다.");
        }

        long newAmount = currentPoint.point() - amount;

        // 포인트 차감 시 0보다 작을 경우 예외 처리
        if(newAmount < 0) {
            throw new RuntimeException("포인트 차감 시 0보다 작으므로 포인트 사용 불가합니다.");
        }

        UserPoint result = userPointTable.insertOrUpdate(id, newAmount);
        pointHistoryTable.insert(id, newAmount, TransactionType.USE, result.updateMillis());

        return result;

    }

    /**
     * 특정 유저의 포인트를 조회하는 기능
     * */
    public UserPoint selectUserPointById(long id) {
        // currentPoint 조회
        UserPoint result = userPointTable.selectById(id);

        return result;
    }
    
    /**
     * 특정 유저의 포인트 충전/사용 내역 조회하는 기능
     * */
    public List<PointHistory> selectUserPointHistory(long id) {
        List<PointHistory> result = pointHistoryTable.selectAllByUserId(id);

        return result;
    }

}
