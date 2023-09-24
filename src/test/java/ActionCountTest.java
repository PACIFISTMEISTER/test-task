import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.exception.InvalidTimestampException;
import ru.task.ActionCount;
import ru.task.IActionCount;

public class ActionCountTest {

    @Test
    @DisplayName("action count sync version v1")
    public void actionCountSyncTestOne_success() throws InvalidTimestampException, InterruptedException {
        IActionCount actionCount = new ActionCount();

        actionCount.call(1);
        actionCount.call(2);
        actionCount.call(3);
        actionCount.call(300);

        Assertions.assertEquals(4, actionCount.getActions(300));
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
        Assertions.assertEquals("invalid timestamp cant be less then zero or equal zero -1", exception.getMessage());
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
        Assertions.assertEquals("invalid timestamp cant be less then zero or equal zero -301", exception.getMessage());
    }
}
