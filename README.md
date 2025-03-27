# 🛠 Multi Thread 환경에서의 동시성 제어

## 🔹 스레드 동기화
- 멀티스레드 환경에서는 여러 **스레드가 하나의 공유 자원에 동시에 접근하지 못하도록 막음**
- 공유 자원에 대한 **Thread-safe를 보장**
    - `synchronized`: 메서드나 블록 단위로 동기화 처리
    - `volatile`: 변수의 가시성을 보장하지만 원자성은 보장하지 않음

---

## 🔹 Lock
- 여러 쓰레드가 같은 자원에 동시에 접근하는 것을 제한
- **데이터의 일관성과 무결성을 유지**하는 데 사용
- **ReentrantLock**, **ReadWriteLock**, **StampedLock** 등 다양한 Lock 방식이 있음
    - **ReentrantLock**: 명시적 Lock을 사용하여 동기화 제어
    - **ReadWriteLock**: 읽기 쓰레드 간 동시 접근을 허용하지만 쓰기에는 단일 접근만 허용
    - **StampedLock**: 잠금의 성능을 최적화한 새로운 유형의 Lock

---

## 🔹 ReentrantLock
- **명시적 Lock**을 사용하여 동기화 제어
- `synchronized` 키워드 없이 Lock을 직접 관리할 수 있음
- Lock의 범위를 **메서드 내부에서 한정하기 어려울 경우** 또는 **여러 개의 Lock을 동시에 사용해야 할 때** 활용 가능

### 예시 코드:
```java
ReentrantLock lock = new ReentrantLock();

try {
    lock.lock();
    // 공유 자원 접근
} finally {
    lock.unlock(); // 반드시 해제
}
```

## 🔹 AtomicInteger
- `synchronized` 보다 적은 비용으로 동시성 보장
- `CAS(Compare-And-Swap)`를 활용하여 원자적 연산을 제공
- 여러 스레드가 동시에 `read & write`하는 환경에서 안전

### 예시 코드:
```java
AtomicInteger counter = new AtomicInteger(0);

counter.incrementAndGet(); // 1 증가 후 반환
counter.getAndIncrement(); // 현재 값 반환 후 1 증가
```

## 🔹 ConcurrentHashMap
- `Map의 일부만 block/unblock 처리` 되므로 synchronized Map보다 효율적
- `Bucket 단위로 Lock을 걸어` 성능 향상
- 여러 스레드가 동시 읽기/쓰기 가능

### 예시 코드:
```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

map.put("key1", 1);
map.computeIfAbsent("key2", k -> 2); // key가 없으면 추가
        map.merge("key1", 1, Integer::sum); // key가 있으면 값 더하기
```

## 🔹 CountDownLatch
- 하나 이상의 스레드가 `다른 스레드에서 수행 중인 작업이 완료될 때까지 대기`할 수 있도록 하는 동기화 기법
- `초기화된 카운트 값을 0이 될 때까지 감소`시켜 다른 스레드의 진행을 허용

### 예시 코드:
```java
CountDownLatch latch = new CountDownLatch(3);

new Thread(() -> {
        // 작업 수행
        latch.countDown(); // 카운트 감소
}).start();

latch.await(); // 모든 작업이 완료될 때까지 대기
```