/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.collection;

import java.util.Vector;

import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * A generic FIFO circular queue class. The queue is fixed size.
 */
public class CircularQueue implements Queue {
    protected Vector vector;
    protected int putNum;
    protected int getNum;
    protected int size;
    private boolean alertOnOverflow = false;
    private String alertMsg = "";
    private Refresher refresher = new Refresher(30 * 1000);

    /**
     * Construct a circular queue of default size.
     */
    public CircularQueue() {
        p_CircularQueueInit(DEFAULT_SIZE);
    }

    /**
     * Construct a circular queue of the specified size.
     *
     * @param size The fixed size of the queue.
     */
    public CircularQueue(int size) {
        p_CircularQueueInit(size);
    }

    public void setAlertOnOverflow(boolean val, String msg) {
        alertOnOverflow = val;
        alertMsg = msg;
    }

    /**
     * Add an element to the queue.
     */
    public synchronized void add(Object p) {
        //----------------------------------------------------------
        // First, get the element stored at the put
        // location.
        //----------------------------------------------------------

        Object obj = null;
        try {
            obj = vector.elementAt(putNum);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        //----------------------------------------------------------
        // Next, set the element.
        //----------------------------------------------------------
        vector.setElementAt(p, putNum);
        putNum = p_inc(putNum);

        //----------------------------------------------------------
        // The following code is for the case where we keep
        // putting something on there and we wrap all the way
        // around. If obj is not null, then we've started wrapping.
        // The oldest element in the queue should then be the
        // very next put location.
        //----------------------------------------------------------

        if (obj != null) {
            getNum = putNum;
            if (alertOnOverflow && refresher.shouldRefresh()) {
                GAlerter.lab(alertMsg + ":CircularQueue overflow. "
                        + " Reached capacity of: " + vector.capacity());
            }
        }
        //----------------------------------------------------------
        // Keep track of the no. of elements in the queue.
        //----------------------------------------------------------

        if (size < vector.capacity()) {
            size++;
        }
    }

    /**
     * Return an array of the current objects.
     */
    public synchronized Object[] getObjects() {
        Object obj[] = new Object[size];

        for (int i = 0, j = getNum; i < size; i++, j = p_inc(j)) {
            obj[i] = vector.elementAt(j);
        }
        return obj;
    }

    /**
     * Retrieve an element from the queue. This removes the element.
     */
    public synchronized Object get() {
        Object obj = top();
        if (obj == null) {
            return null;
        }

        vector.setElementAt(null, getNum);
        getNum = p_inc(getNum);
        size--;
        return obj;
    }

    /**
     * Get the next element in the queue without removing it.
     */
    public synchronized Object top() {
        Object obj = null;
        try {
            obj = vector.elementAt(getNum);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return obj;
    }

    /**
     * Return the capacity of the queue.
     */
    public int capacity() {
        return vector.capacity();
    }

    /**
     * return the no. of elements in the queue.
     */
    public int size() {
        return size;
    }

    /**
     * Reset to queue
     */
    public synchronized void clear() {
        vector.removeAllElements();
        int size = vector.capacity();

        for (int i = 0; i < size; i++) {
            vector.addElement(null);
        }
        putNum = 0;
        getNum = 0;
        this.size = 0;
    }

    private static final int DEFAULT_SIZE = 100;

    /**
     * Increment an integer, wrapping around. Note that it is not
     * synchronized since the caller is.
     */
    protected int p_inc(int var) {
        var++;
        if (var == vector.capacity()) {
            var = 0;
        }
        return var;
    }

    private void p_CircularQueueInit(int size) {
        vector = new Vector(size, size / 10);

        //----------------------------------------------------------
        // Populate the vector with all null elements.
        // An invariant is that the vector will always
        // be fully populated.
        //----------------------------------------------------------

        for (int i = 0; i < size; i++) {
            vector.addElement(null);
        }
        putNum = 0;
        getNum = 0;
    }
}
