package com.enjoyf.platform.util.barcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

/**
 * @Auther: <a mailto:yinpengyi@gmail.com>Yin Pengyi</a>
 */
public class QRCodeImageUtil {
    private static final int QR_LENGHT = 45;
    private static final int SEG_SIZE = 2;

    //create the bar code encoder image
    public static void createQRImageFile(String src, int w, int h, String fileName) {
        //check the param.
        if (w < QR_LENGHT * SEG_SIZE) {
            w = QR_LENGHT * SEG_SIZE;
        }

        if (h < QR_LENGHT * SEG_SIZE) {
            h = QR_LENGHT * SEG_SIZE;
        }

        //
        try {
            //
            RenderedImage bi = createQRImage(src, w, h);

            //create the image
            File f = new File(fileName);
            if (!f.exists()) {
                f.createNewFile();
            }

            //创建图片
            ImageIO.write(bi, "JPEG", f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //create the bar code encoder image
    public static RenderedImage createQRImage(String src, int w, int h) throws UnsupportedEncodingException {
        BufferedImage bi = null;

        //the qrcode
        Qrcode qrcode = new Qrcode();

        qrcode.setQrcodeErrorCorrect('M');
        qrcode.setQrcodeEncodeMode('B');
        qrcode.setQrcodeVersion(7);

        //the input string
        byte[] d = src.getBytes("UTF8");
        // 限制最大字节数为120
        if (d.length <= 0 || d.length >= 120) {
            throw new RuntimeException("The QRCode src string is' too long or is empty.");
        }

        //initialize the image data.
        bi = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

        //
        Graphics2D g = bi.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, w, h);
        g.setColor(Color.BLACK);

        // 限制最大字节数为120
        if (d.length > 0 && d.length < 120) {
            //
            boolean[][] s = qrcode.calQrcode(d);

            //
            for (int i = 0; i < s.length; i++) {
                for (int j = 0; j < s.length; j++) {
                    if (s[j][i]) {
                        g.fillRect(j * SEG_SIZE + (w - SEG_SIZE * 45) / 2, i * SEG_SIZE + (h - SEG_SIZE * 45) / 2, SEG_SIZE, SEG_SIZE);
                    }
                }
            }
        }

        //
        g.dispose();

        //
        bi.flush();

        return bi;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Date());

        for (int i = 0; i < 1; i++) {
            QRCodeImageUtil.createQRImageFile(args[0] + i, 132, 132, "D:\\QRCodeTest\\" + i + ".jpg");
        }

        System.out.println(new Date());
    }
}
