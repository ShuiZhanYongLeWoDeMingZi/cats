package com.kingyee.common.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhl on 2017/4/17.
 * 线程安全的多对象引用计数
 */
public class ReferenceCountSet<E> extends AbstractSet<E> implements Serializable{
    private volatile ConcurrentMap<E, AtomicInteger> data;

    public ReferenceCountSet() {
        initData();
    }

    @Override
    public int size() {
        return data.size();
    }

    public int getRefCount(E element) {
        AtomicInteger ref = data.get(element);
        return ref == null ? 0 : ref.get();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object obj) {
        return data.containsKey(obj);
    }

    @Override
    public Iterator<E> iterator() {
        return data.keySet().iterator();
    }

    @Override
    public boolean add(E element) {
        AtomicInteger refCount = data.get(element);
        if (refCount == null) {
            // Avoid to put AtomicInteger(0) as during remove we need this value to compare
            AtomicInteger newRefCount = new AtomicInteger(1);
            refCount = data.putIfAbsent(element, newRefCount);
            if (refCount == null) {
                return true;
            }
        }
        refCount.incrementAndGet();
        return true;
    }

    public boolean decrement(E element) {
        AtomicInteger refCount = data.get(element);
        if (refCount == null) {
            return false;
        }
        refCount.decrementAndGet();
        return true;
    }

    /**
     * 初始化,线程不安全
     *
     * @param element
     * @param ai
     */
    public void init(E element, AtomicInteger ai) {
        data.put(element, ai);
    }

    @Override
    public boolean remove(Object obj) {
        AtomicInteger refCount = data.get(obj);
        if (refCount == null) {
            return false;
        }
        if (refCount.decrementAndGet() <= 0) {
            return data.remove(obj, refCount);
        }
        return false;
    }

    @Override
    public void clear() {
        // Instead of call it's clear method, I simply discard the previous map
        // and create a new one, then the old one will be garbage collected.
        // This way is we can make it thread safe with copy-on-write mechanism,
        // and is faster than the clear method especially when the items a large in the previous map
        initData();
    }

    private void initData() {
        data = new ConcurrentHashMap<E, AtomicInteger>();
    }
}

