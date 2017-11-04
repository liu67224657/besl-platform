package com.enjoyf.platform.util.htmlparse;


import com.enjoyf.platform.util.log.GAlerter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
class StreamUtil {

    public static void closeInputStream(InputStream inputSream) {
        if (inputSream != null) {
            try {
                inputSream.close();
            } catch (IOException e) {
                GAlerter.lan(StreamUtil.class.getName() + " closeInputStream occured IOException.e:", e);
            }
        }
    }

    public static void closeOutputStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                GAlerter.lan(StreamUtil.class.getName() + " closeOutputStream occured IOException.e:", e);
            }
        }
    }
}
