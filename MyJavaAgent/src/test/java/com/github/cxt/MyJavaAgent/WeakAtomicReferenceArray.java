package com.github.cxt.MyJavaAgent;

import java.util.concurrent.atomic.AtomicReferenceArray;

public final class WeakAtomicReferenceArray<T> {

    private final int length;
    private final AtomicReferenceArray<T> atomicArray;

    public WeakAtomicReferenceArray(int length, Class<? extends T> clazz) {
        this.length = length;
        this.atomicArray = new AtomicReferenceArray<T>(length);
    }

    public void set(int index, T newValue) {
        this.atomicArray.set(index, newValue);
    }

    public int length() {
        return length;
    }


    public T get(int index) {
        return this.atomicArray.get(index);
    }

}

