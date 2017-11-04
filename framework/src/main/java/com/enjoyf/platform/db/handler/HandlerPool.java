/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.handler;

import com.enjoyf.platform.db.DbConnException;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-21 下午5:03
 * Description:
 */
public class HandlerPool<T> {
    //
    private static int next = 0;

    //
    private Set<T> handlerPools = new HashSet<T>();

    public HandlerPool() {
    }

    public synchronized void add(T t) {
        handlerPools.add(t);
    }

    public synchronized T getHandler() throws DbConnException {
        if (handlerPools.isEmpty()) {
            throw new DbConnException(DbConnException.CONN_POOL_GET_FAILED, "There's not handler in the handler pool.");
        }

        int size = handlerPools.size();
        int idx = next() % size;
        T handler = null;

        int i = 0;
        for (T t : handlerPools) {
            if (i == idx) {
                handler = t;
                break;
            }
            i++;
        }

        return handler;
    }

    private int next() {
        if (next == Integer.MAX_VALUE) {
            next = 0;
        } else {
            ++next;
        }

        return next;
    }
}
