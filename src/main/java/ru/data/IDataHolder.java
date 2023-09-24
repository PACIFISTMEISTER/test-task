package ru.data;


import java.util.Iterator;
import java.util.stream.Stream;

public interface IDataHolder<T> {
    void add(T timestamp);
    int size();

    Iterator<T> getIterator();
}
