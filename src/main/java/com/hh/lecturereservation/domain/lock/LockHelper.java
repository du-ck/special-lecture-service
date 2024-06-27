package com.hh.lecturereservation.domain.lock;

import com.hh.lecturereservation.domain.LectureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.StampedLock;

@Component
public class LockHelper {
    private final StampedLock lock = new StampedLock();
    private static final Logger log = LoggerFactory.getLogger(LockHelper.class);

    public long getWriteLock() {
        log.info("lockWrite!!");
        return lock.writeLock();
    }

    public void loseWriteLock(long stamp) {
        log.info("unlockWrite!!");
        lock.unlockWrite(stamp);
    }

    public long tryOptimisticRead() {
        return lock.tryOptimisticRead();
    }

    public boolean validate(long stamp) {
        log.info("validate!!");
        return lock.validate(stamp);
    }

    public Long getReadLock() {
        log.info("lockRead!!");
        return lock.readLock();
    }

    public void loseReadLock(long stamp) {
        lock.unlockRead(stamp);
    }
}
