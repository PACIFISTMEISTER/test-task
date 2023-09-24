package ru.task;

import ru.exception.InvalidTimestampException;

public interface IActionCount {
    void call(int timestamp) throws InvalidTimestampException, InterruptedException;

    int getActions(int timestamp) throws InvalidTimestampException;
}
