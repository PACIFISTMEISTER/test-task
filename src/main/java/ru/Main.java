package ru;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.exception.InvalidTimestampException;
import ru.task.ActionCount;
import ru.task.IActionCount;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InvalidTimestampException, InterruptedException {
        IActionCount actionCount = new ActionCount();

        actionCount.call(1);
        actionCount.call(2);
        actionCount.call(3);

        actionCount.call(300);

        log.info(actionCount.getActions(300));
        log.info(actionCount.getActions(301));

    }
}
