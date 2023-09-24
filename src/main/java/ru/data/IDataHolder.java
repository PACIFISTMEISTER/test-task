package ru.data;


import java.util.Iterator;

public interface IDataHolder<T> {
    void add(T timestamp);

    int size();

    Iterator<T> getIterator();
}
