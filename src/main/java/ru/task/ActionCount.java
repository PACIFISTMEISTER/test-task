package ru.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.data.DataHolder;
import ru.data.IDataHolder;
import ru.exception.InvalidTimestampException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActionCount implements IActionCount {

    private static final Logger log = LogManager.getLogger(ActionCount.class);

    private static final int MAX_TIME_DELTA = 300;

    private final IDataHolder<Integer> dataHolder = new DataHolder<>(); //не нравится что timestamp это int, так что сделал через generic

    private final AtomicBoolean isReadingData = new AtomicBoolean(false);
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    public ActionCount() {
    }

    @Override
    public void call(int timestamp) throws InvalidTimestampException, InterruptedException {
        if (timestamp <= 0) { // видел что есть ограничение в задании, но проверка лишней не бывает
            throw InvalidTimestampException.createExceptionByTimestamp(timestamp);
        }

        if (isReadingData.get()) {// ставим все потоки, пытающиеся писать в момент чтения на ожидание завершение чтения. Исходя из условия (что вызовы выполняются в системе в хронологическом порядке) можем предположить, что это нам гарантирует консистентность данных в системе
            countDownLatch.await();
        }
        dataHolder.add(timestamp);
    }

    @Override
    public synchronized int getActions(int finish) throws InvalidTimestampException {// если без синхронзайд делать, то при многопоточном чтении может произойти ситуация, что один поток открыл запись для другого (isReadingData.set(false);)
        if (finish <= 0) { // видел что есть ограничение в задании, но проверка лишней не бывает
            throw InvalidTimestampException.createExceptionByTimestamp(finish);
        }

        int start = Math.max(finish - MAX_TIME_DELTA, 0);

        isReadingData.set(true);
        int result = getActions(start, finish);
        countDownLatch.countDown();
        isReadingData.set(false);

        return result;
    }

    private boolean checkIfTimestampInRange(int timestamp, int start, int finish) {
        return start < timestamp && timestamp <= finish;
    }

    private int getActions(int start, int finish) {
        int counter = 0;
        var dataIterator = dataHolder.getIterator();

        while (dataIterator.hasNext()) {
            var timestamp = dataIterator.next();

            if (checkIfTimestampInRange(timestamp, start, finish)) {
                counter++;
            }
        }

        return counter;
    }
}
