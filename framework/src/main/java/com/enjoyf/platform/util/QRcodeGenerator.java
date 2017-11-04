package com.enjoyf.platform.util;

import com.enjoyf.platform.crypto.Base64;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-21 下午4:05
 * Description:
 */
public class QRcodeGenerator {

    public static String genCode(String path, String content, int width, int height) {
        try {
            BitMatrix byteMatrix;
            byteMatrix = new MultiFormatWriter().encode(new String(content.getBytes("UTF-8"), "iso-8859-1"),
                    BarcodeFormat.QR_CODE, width, height);
            File file = new File(path);

            MatrixToImageWriter.writeToFile(byteMatrix, "png", file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static OutputStream genCode(String content, int width, int height, OutputStream outputStream) {
        try {
            BitMatrix byteMatrix;
            byteMatrix = new MultiFormatWriter().encode(new String(content.getBytes("UTF-8"), "iso-8859-1"),
                    BarcodeFormat.QR_CODE, width, height);

            MatrixToImageWriter.writeToStream(byteMatrix, "png", outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    public static OutputStream genRemovalCode(String content, Integer width, Integer height, OutputStream stream) {
        try {
            BitMatrix byteMatrix;
            byteMatrix = new MultiFormatWriter().encode(new String(content.getBytes("UTF-8"), "iso-8859-1"),
                    BarcodeFormat.QR_CODE, width, height);

            //去白边
            int[] rec = byteMatrix.getEnclosingRectangle();
            int resWidth = rec[2] + 1;
            int resHeight = rec[3] + 1;
            BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
            resMatrix.clear();
            for (int i = 0; i < resWidth; i++) {
                for (int j = 0; j < resHeight; j++) {
                    if (byteMatrix.get(i + rec[0], j + rec[1])) {
                        resMatrix.set(i, j);
                    }
                }
            }

            MatrixToImageWriter.writeToStream(resMatrix, "png", stream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }
}
