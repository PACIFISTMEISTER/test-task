package ru.data;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataHolder<T> implements IDataHolder<T> {

    private final ConcurrentLinkedQueue<T> dataHolder;

    public DataHolder() {
        this.dataHolder = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void add(T timestamp) {
        dataHolder.add(timestamp);
    }

    @Override
    public int size() {
        return dataHolder.size();
    }

    @Override
    public Iterator<T> getIterator() {
        return dataHolder.iterator();
    }
}
