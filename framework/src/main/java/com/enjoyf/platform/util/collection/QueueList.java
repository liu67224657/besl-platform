/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.collection;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A Queue backed by a list. This means that the queue can keep growing
 * in size as elements are added to it. The policy is FIFO.
 */
public class QueueList implements Queue {
    LinkedList linkList = new LinkedList();

    public QueueList() {
    }

    /**
     * Add an element to the queue.
     */
    public synchronized void add(Object obj) {
        linkList.addLast(obj);
    }

    /**
     * Add an element to the queue.
     */
    public synchronized void addAll(Collection collection) {
        linkList.addAll(collection);
    }

    /**
     * Retrieve the next element in the queue.
     */
    public synchronized Object get() {
        if (size() == 0) {
            return null;
        }

        return linkList.removeFirst();
    }

    public int size() {
        return linkList.size();
    }

    public void clear() {
        linkList.clear();
    }
}

