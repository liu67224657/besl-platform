package com.enjoyf.platform.util;


import com.enjoyf.platform.util.log.GAlerter;

import java.io.*;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class StreamUtil {

    public static void closeReader(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                GAlerter.lan(StreamUtil.class.getName() + " closeInputStream occured IOException.e:", e);
            }
        }
    }

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

    public static byte[] streamToBytes(InputStream inputStream) throws IOException {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new BufferedInputStream(inputStream);
            out = new ByteArrayOutputStream();
            int size = 0;
            byte[] temp = new byte[8192];
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }

            return out.toByteArray();
        } finally {
            StreamUtil.closeInputStream(in);
            StreamUtil.closeOutputStream(out);
        }
    }


    public static String readByInputStream(InputStream in, String encoding) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(in, encoding);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            return sb.toString();
        } finally {
            if (isr != null) {
                isr.close();
                isr = null;
            }
            if (br != null) {
                br.close();
                br = null;
            }
        }
    }
}
