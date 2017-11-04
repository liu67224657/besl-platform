package com.enjoyf.platform.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {

    public static void zoomImg(int $destW, int $destH, String $srcFileName, int $rgbColor) throws IOException {
        BufferedImage _srcImg = javax.imageio.ImageIO.read(new File($srcFileName));

        int _srcW = _srcImg.getWidth(null);
        int _srcH = _srcImg.getHeight(null);
        int _zoomW = 0;
        int _zoomH = 0;
        int _zoomT = 0;
        int _zoomL = 0;

        if (_srcW / $destW > _srcH / $destH) {
            _zoomW = $destW;
            _zoomH = _srcH * $destW / _srcW;
            _zoomT = ($destH - _zoomH) / 2;
        } else {
            _zoomH = $destH;
            _zoomW = _srcW * $destH / _srcH;
            _zoomL = ($destW - _zoomW) / 2;
        }

        BufferedImage _destImg = new BufferedImage($destW, $destH, BufferedImage.TYPE_INT_RGB);

        _destImg.getGraphics().setColor(new Color($rgbColor));
        _destImg.getGraphics().fillRect(0, 0, $destW, $destH);

        _destImg.getGraphics().drawImage(_srcImg, _zoomL, _zoomT, _zoomW,
                _zoomH, null);

        FileOutputStream out = new FileOutputStream($srcFileName);
        ImageIO.write(_destImg, "JPEG", out);
    }

    /**
     * 旋转图片为指定角度
     *
     * @param imageFileName 目标图像
     * @param times         旋转90的次数
     * @return
     */
    public static boolean rotateImage(String imageFileName, int times) throws IOException {
        if (times % 4 == 0) {
            return true;
        }

        BufferedImage bufferedimage = javax.imageio.ImageIO.read(new File(imageFileName));

        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();

        BufferedImage img;
        Graphics2D graphics2d;

        if (times % 4 == 1 || times % 4 == 3) {
            img = new BufferedImage(h, w, type);
        } else {
            img = new BufferedImage(w, h, type);
        }

        graphics2d = img.createGraphics();
        graphics2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        graphics2d.rotate(Math.toRadians(90 * (times % 4)), w / 2, h / 2);

        if (times % 4 == 1) {
            graphics2d.drawImage(bufferedimage, (w - h) / 2, (w - h) / 2, null);
        } else if (times % 4 == 3) {
            graphics2d.drawImage(bufferedimage, (h - w) / 2, (h - w) / 2, null);
        } else {
            graphics2d.drawImage(bufferedimage, 0, 0, null);
        }

        graphics2d.dispose();

        FileOutputStream out = new FileOutputStream(imageFileName);
        ImageIO.write(img, "JPEG", out);

        return true;
    }
}
