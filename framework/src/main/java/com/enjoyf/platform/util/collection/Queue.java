package com.enjoyf.platform.util.collection;

public interface Queue {
    /**
     * Return the current size of the queue.
     */
    public int size();

    /**
     * Add an element to the queue.
     */
    public void add(Object obj);

    /**
     * Retrieve an element from the queue. This removes the "next"
     * element from the queue, "next" being defined by the implementation.
     */
    public Object get();

    /**
     * Clear out the queue.
     */
    public void clear();
}
