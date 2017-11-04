/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db;

import com.enjoyf.platform.util.log.GAlerter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class TableAccessorFactory {
    //
    private static volatile TableAccessorFactory instance;

    //
    private static Map<String, Object> accessorMap = new HashMap<String, Object>();

    //
    public static TableAccessorFactory get() {
        synchronized (TableAccessorFactory.class) {
            if (instance == null) {
                instance = new TableAccessorFactory();
            }
        }

        return instance;
    }

    //
    public synchronized <T> T factoryAccessor(Class<T> clazz, DataBaseType dataBaseType) throws DbTypeException {
        //
        String className = clazz.getName() + dataBaseType.getSuffix();

        //
        if (!accessorMap.containsKey(className)) {
            try {
                T accessor = (T) (Class.forName(className).newInstance());

                accessorMap.put(className, accessor);
            } catch (InstantiationException e) {
                GAlerter.lab("Can't initialize accessor: " + clazz);

                throw new IllegalArgumentException("Class not supported: " + clazz + dataBaseType.getSuffix());
            } catch (IllegalAccessException e) {
                GAlerter.lab("Can't initialize accessor: " + clazz);

                throw new IllegalArgumentException("Class not supported: " + clazz + dataBaseType.getSuffix());
            } catch (ClassNotFoundException e) {
                GAlerter.lab("Can't initialize accessor: " + clazz);

                throw new IllegalArgumentException("Class not found: " + clazz + dataBaseType.getSuffix());
            }
        }

        //
        return (T) accessorMap.get(className);
    }
}
