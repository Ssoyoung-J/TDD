package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    /**
     * 해당 Table을 활용하여 기능 작성
     */
    private UserPointTable userPointTable;

    /**
     *
     * 특정 유저의 포인트를 충전하는 기능
     * 유저의 포인트 충전 후 return될 값이 필요 없음
     */
    public UserPoint chargeUserPoint(long id, long amount) {
        return userPointTable.insertOrUpdate(id, amount);
    }

}
