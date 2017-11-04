/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.collection;

import com.enjoyf.platform.io.RPacketBase;
import com.enjoyf.platform.io.WPacketBase;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.code.fqueue.FQueue;

import java.io.Serializable;

/**
 *
 */
public class FQueueQueue implements Queue {
    //
    private static final String PATH_ROOT = "/opt/servicedata/queue";
    //
    private FQueue fQueue = null;

    //
    public FQueueQueue(String serviceName, String queueName) {
        try {
            fQueue = new FQueue(PATH_ROOT + "/" + serviceName + "/" + queueName);
        } catch (Exception e) {
            //
            GAlerter.lab("FQueueQueue constructor error.", e);
        }
    }

    /**
     * Add an element to the queue.
     */
    public synchronized void add(Object obj) {
        fQueue.offer(objectToByte(obj));
    }

    /**
     * Retrieve the next element in the queue.
     */
    public synchronized Object get() {
        if (size() == 0) {
            return null;
        }

        return byteToObject(fQueue.poll());
    }

    public int size() {
        return fQueue.size();
    }

    public void clear() {
        fQueue.clear();
    }

    private byte[] objectToByte(Object obj) {
        byte[] returnValue = null;

        //
        if (obj == null || !(obj instanceof Serializable)) {
            return returnValue;
        }

        //
        WPacketBase wPacket = new WPacketBase();

        wPacket.writeSerializable((Serializable) obj);
        returnValue = wPacket.getOutBuffer();

        try {
            wPacket.close();
        } catch (Exception e) {
            //
        }

        return returnValue;
    }

    private Object byteToObject(byte[] bytes) {
        Object returnValue = null;

        //
        if (bytes == null) {
            return returnValue;
        }

        //
        RPacketBase rPacket = new RPacketBase(bytes);

        returnValue = rPacket.readSerializable();

        try {
            rPacket.close();
        } catch (Exception e) {
            //
        }

        return returnValue;
    }
}

