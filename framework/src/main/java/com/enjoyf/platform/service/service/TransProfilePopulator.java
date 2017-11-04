/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.enjoyf.platform.util.log.GAlerter;

public class TransProfilePopulator {

    public static void p_populate(Class sourceClass, TransProfileContainer container) {
        try {
            Field[] fields = sourceClass.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                int mod = f.getModifiers();

                // must be public and static...
                if (!Modifier.isPublic(mod) || !Modifier.isStatic(mod) || !f.getType().equals(Byte.TYPE)) {
                    continue;
                }

                //GAlerterLogger.lm("Adding "+f.getByte(f)+" "+f.getBoardName());
                container.put(new TransProfile(f.getByte(f), f.getName()));
            }
        } catch (Exception ex) {
            GAlerter.lab("TransProfilePopulator: Problem filling TransProfileContainer !", ex);
        }
    }


    public static void populate(Class sourceClass, TransProfileContainer container) {
        ServiceConstants serviceConstants = new ServiceConstants();

        p_populate(serviceConstants.getClass(), container);
        p_populate(sourceClass, container);
    }
}


