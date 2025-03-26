package io.hhplus.tdd;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
