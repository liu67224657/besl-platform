package com.enjoyf.platform.tools.image;

import com.enjoyf.platform.util.regex.RegexUtil;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class CompressPNG {
    public static BufferedImage compressImage(BufferedImage sourceImage, String destDir) throws IOException {
        if (sourceImage == null)
            throw new NullPointerException("��ͼƬ");
        BufferedImage cutedImage = null;
        BufferedImage tempImage = null;
        BufferedImage compressedImage = null;
        Graphics2D g2D = null;
        //ͼƬ�Զ��ü�
        cutedImage = cutImageAuto(sourceImage);
        int width = cutedImage.getWidth();
        int height = cutedImage.getHeight();
        //ͼƬ��ʽΪ555��ʽ
        tempImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB);
        g2D = (Graphics2D) tempImage.getGraphics();
        g2D.drawImage(sourceImage, 0, 0, null);
        compressedImage = getConvertedImage(tempImage);
        //��������ת�����ͼƬ
        compressedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        g2D = (Graphics2D) compressedImage.getGraphics();
        g2D.drawImage(tempImage, 0, 0, null);
        int pixel[] = new int[width * height];
        int sourcePixel[] = new int[width * height];
        int currentPixel[] = new int[width * height];
        sourcePixel = cutedImage.getRGB(0, 0, width, height, sourcePixel, 0, width);
        currentPixel = tempImage.getRGB(0, 0, width, height, currentPixel, 0, width);
        for (int i = 0; i < currentPixel.length; i++) {
            if (i == 0 || i == currentPixel.length - 1) {
                pixel[i] = 0;
                //�ڲ�����
            } else if (i > width && i < currentPixel.length - width) {
                int bef = currentPixel[i - 1];
                int now = currentPixel[i];
                int aft = currentPixel[i + 1];
                int up = currentPixel[i - width];
                int down = currentPixel[i + width];
                if (isBackPixel(now)) {
                    pixel[i] = 0;
                } else if ((!isBackPixel(now) && isBackPixel(bef)) || (!isBackPixel(now) && isBackPixel(aft))
                        || (!isBackPixel(now) && isBackPixel(up)) || (!isBackPixel(now) && isBackPixel(down))) {
                    pixel[i] = sourcePixel[i];
                } else {
                    pixel[i] = now;
                }
            } else {
                int bef = currentPixel[i - 1];
                int now = currentPixel[i];
                int aft = currentPixel[i + 1];
                if (isBackPixel(now)) {
                    pixel[i] = 0;
                } else if ((!isBackPixel(now) && isBackPixel(bef)) || (!isBackPixel(now) && isBackPixel(aft))) {
                    pixel[i] = sourcePixel[i];
                } else {
                    pixel[i] = now;
                }
            }
        }
        compressedImage.setRGB(0, 0, width, height, pixel, 0, width);
        g2D.drawImage(compressedImage, 0, 0, null);
//        ImageIO.write(cutedImage, "png", new File("D:/png/a_cut.png"));
        ImageIO.write(tempImage, "png", new File(destDir));
//        ImageIO.write(compressedImage, "png", new File("D:/png/c_com.png"));
        return compressedImage;
    }

    private static boolean isBackPixel(int pixel) {
        int back[] = {-16777216};
        for (int i = 0; i < back.length; i++) {
            if (back[i] == pixel)
                return true;
        }
        return false;
    }

    public static BufferedImage cutImageAuto(BufferedImage image) {
        Rectangle area = getCutAreaAuto(image);
        return image.getSubimage(area.x, area.y, area.width, area.height);
    }

    private static Rectangle getCutAreaAuto(BufferedImage image) {
        if (image == null)
            throw new NullPointerException("ͼƬΪ��");
        int width = image.getWidth();
        int height = image.getHeight();
        int startX = width;
        int startY = height;
        int endX = 0;
        int endY = 0;
        int[] pixel = new int[width * height];

        pixel = image.getRGB(0, 0, width, height, pixel, 0, width);
        for (int i = 0; i < pixel.length; i++) {
            if (isCutBackPixel(pixel[i]))
                continue;
            else {
                int w = i % width;
                int h = i / width;
                startX = (w < startX) ? w : startX;
                startY = (h < startY) ? h : startY;
                endX = (w > endX) ? w : endX;
                endY = (h > endY) ? h : endY;
            }
        }
        if (startX > endX || startY > endY) {
            startX = startY = 0;
            endX = width;
            endY = height;
        }
        return new Rectangle(startX, startY, endX - startX, endY - startY);
    }

    private static boolean isCutBackPixel(int pixel) {
        int back[] = {0, 8224125, 16777215, 8947848, 460551, 4141853, 8289918};
        for (int i = 0; i < back.length; i++) {
            if (back[i] == pixel)
                return true;
        }
        return false;
    }

    private static BufferedImage getConvertedImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage convertedImage = null;
        Graphics2D g2D = null;
        convertedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        g2D = (Graphics2D) convertedImage.getGraphics();
        g2D.drawImage(image, 0, 0, null);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = convertedImage.getRGB(i, j);
                if (isBackPixel(rgb)) {
                    convertedImage.setRGB(i, j, 0);
                }
            }
        }
        g2D.drawImage(convertedImage, 0, 0, null);
        return convertedImage;
    }


    public static void main(String[] args) throws IOException {
        compressPng("C:\\Users\\ericliu.EF\\Desktop\\test\\test");

//        BufferedImage bi = ImageIO.read(new File("D:/png/290px-Thumbnail_chara_5149.png"));
//        CompressPNG.compressImage(bi,"dd");
    }



    private static void compressPng(String fileDir) throws IOException {


        File imageRoot = new File(fileDir);
        File[] files = imageRoot.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                compressPng(fileDir + "\\" + file.getName());
            } else if(file.getName().endsWith("png")||file.getName().endsWith("PNG")){
                BufferedImage bi = ImageIO.read(file);
                 CompressPNG.compressImage(bi,fileDir + "\\" + file.getName());
            }
        }
    }
}
