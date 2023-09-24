import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.exception.InvalidTimestampException;
import ru.task.ActionCount;
import ru.task.IActionCount;

import java.util.ArrayList;
import java.util.List;

public class ActionCountTest {

    private static final Logger log = LogManager.getLogger(ActionCountTest.class);

    private final int MAX_THREADS = 10;
    private final int STEP = 20;

    @Test
    @DisplayName("action count sync version v1")
    public void actionCountSyncTestOne_success() throws InvalidTimestampException, InterruptedException {
        IActionCount actionCount = new ActionCount();

        actionCount.call(1);
        actionCount.call(2);
        actionCount.call(3);

        Assertions.assertEquals(3, actionCount.getActions(300));
    }

    @Test
    @DisplayName("action count sync version v2")
    public void actionCountSyncTestTwo_success() throws InvalidTimestampException, InterruptedException {
        IActionCount actionCount = new ActionCount();

        actionCount.call(1);
        actionCount.call(2);
        actionCount.call(3);

        Assertions.assertEquals(2, actionCount.getActions(301));
    }

    @Test
    @DisplayName("action count sync expected ru.exception")
    public void actionCountConcurrentTestOne_error() {
        InvalidTimestampException exception = Assertions.assertThrows(InvalidTimestampException.class, () -> {
            IActionCount actionCount = new ActionCount();

            actionCount.call(-1);
            actionCount.call(2);
            actionCount.call(3);
        });
        Assertions.assertEquals("invalid timestamp cant be less or equal zero -1", exception.getMessage());
    }

    @Test
    @DisplayName("action count sync expected ru.exception")
    public void actionCountConcurrentTestTwo_error() {
        InvalidTimestampException exception = Assertions.assertThrows(InvalidTimestampException.class, () -> {
            IActionCount actionCount = new ActionCount();

            actionCount.call(1);
            actionCount.call(2);
            actionCount.call(3);

            actionCount.getActions(-301);
        });
        Assertions.assertEquals("invalid timestamp cant be less or equal zero -301", exception.getMessage());
    }

    @Test
    @DisplayName("action count concurrent version v1")
    public void actionCountConcurrentTestOne_success() throws InvalidTimestampException, InterruptedException {
        IActionCount actionCount = new ActionCount();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < MAX_THREADS; i++) {
            threads.add(new Thread() {
                @Override
                public void run() {
                    try {
                        int start = 0;
                        int finish = 50;
                        for (int j = start; j < finish; j++) {
                            actionCount.call(j);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            });
        }

        for (var thread : threads) {
            thread.start();
        }

        for (int i = 1; i < 10; i++) {
            log.info("getting this action {} {}", i, actionCount.getActions(i * STEP));
        }
    }
}
