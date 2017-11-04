package com.enjoyf.platform.webapps.common.html;

import com.enjoyf.platform.util.log.GAlerter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ValidateImageGenerator {

    private static ValidateImageGenerator instance = null;

    private ValidateImageGenerator() {
    }

    public static ValidateImageGenerator get() {
        if (instance == null) {
            synchronized (ValidateImageGenerator.class) {
                if (instance == null) {
                    instance = new ValidateImageGenerator();
                }
            }
        }
        return instance;
    }


    public static final String RANDOMCODEKEY = "JOYMEROMDANKEY";// 放到session中的key
    private Random random = new Random();
    private String[] randStr = new String[]{"刀山火海", "百发百中", "日久天长", "里应外合", "虎头蛇尾",
            "空前绝后", "顶天立地", "一心一意", "三心二意", "闭月羞花", "沉鱼落雁", "国色天香", "如花似玉",
            "七上八下", "九死一生", "三长两短", "百发百中", "人山人海", "无中生有", "小题大做", "日久天长",
            "自由自在", "斤斤计较", "井井有条", "自言自语", "无边无际 ", "风雨同舟", "同甘共苦", "五颜六色",
            "三三两两", "无穷无尽", "车水马龙", "守株待兔", "画蛇添足", "亡羊补牢", "形形色色", "对牛弹琴",
            "画龙点睛", "千山万水"};
    private char[] randString = new char[]{'a', 'b', 'c', 'c', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z'};

    private int width = 100;// 图片宽
    private int height = 26;// 图片高
    private int lineSize = 30;// 干扰线数量

    /*
     * 获得字体
     */
//    private Font getFont() {
//        return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
//    }

    /*
     * 获得颜色
     */
    private Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /**
     * 生成随机图片
     */
    public void getRandcode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("黑体", Font.ROMAN_BASELINE, 18));
        g.setColor(getRandColor(15, 100));
        // 绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            drowLine(g);
        }
        // 绘制随机字符
        String[] ramdonString = this.getRandomString();
        Graphics2D g2d = (Graphics2D) g;

        String displayString = ramdonString[1];
        // 正数还是负数
        int a = random.nextInt(2);
        // 偏的角度
        int b = random.nextInt(10);
        // 最后偏的角度
        int c = a == 0 ? b : 360 - b;
        g2d.rotate(c * Math.PI / 180);
        g2d.setColor(Color.RED);
        g2d.drawString(displayString, 10, 20);

        session.setAttribute(RANDOMCODEKEY, ramdonString[0]);
        g.dispose();
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (Exception e) {
        }
    }

    /**
     * 生成数字图片
     */
    public void getSimplebercode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("黑体", Font.ROMAN_BASELINE, 18));
        g.setColor(Color.LIGHT_GRAY);
        // 绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            drowLine(g);
        }
        // 绘制随机字符
        String[] chars = this.getSimpleRandomString(4);
        Graphics2D g2d = (Graphics2D) g;

        // 正数还是负数
        int a = random.nextInt(2);
        // 偏的角度
        int b = random.nextInt(10);
        // 最后偏的角度
        int i = 0;
        String s = "";
        for (String charct : chars) {
            g2d.setColor(Color.RED);
            g2d.drawString(charct, 10 + i, 20);
            s += charct;
            i = i + 20;
        }


        session.setAttribute(RANDOMCODEKEY, s);
        g.dispose();
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (Exception e) {
        }
    }

    /*
     * 绘制字符串
     */
    // private String drowString(Graphics g, String randomString, int i) {
    // g.setFont(getFont());
    // g.setColor(new Color(random.nextInt(101), random.nextInt(111),
    // random.nextInt(121)));
    // String rand =
    // String.valueOf(getRandomString(random.nextInt(randString.length())));
    // randomString += rand;
    // g.translate(random.nextInt(3), random.nextInt(3));
    // g.drawString(rand, 13 * i, 16);
    // return randomString;
    // }

    /*
     * 绘制干扰线
     */
    private void drowLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /*
     * 获取随机的字符
     */
    // public String getRandomString(int num) {
    // return String.valueOf(randString.charAt(num));
    // }

    public String[] getRandomString() {
        String str = randStr[random.nextInt(randStr.length)];
        int length = str.length();
        int num = random.nextInt(length);
        String replaceStr = new String(new char[]{str.charAt(num)});
        String remainStr = str.replaceFirst(String.valueOf(str.charAt(num)), "?");
        return new String[]{replaceStr, remainStr};
    }

    public String[] getSimpleRandomString(int length) {
        String[] str = new String[length];
        for (int i = 0; i < length; i++) {
            str[i] = String.valueOf(randString[random.nextInt(randString.length)]);
        }

        return str;
    }


    public void getSimplebercode(FileOutputStream file) {
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("黑体", Font.ROMAN_BASELINE, 18));
        g.setColor(Color.LIGHT_GRAY);
        // 绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            drowLine(g);
        }
        // 绘制随机字符
        String[] chars = this.getSimpleRandomString(4);
        Graphics2D g2d = (Graphics2D) g;

        // 正数还是负数
        int a = random.nextInt(2);
        // 偏的角度
        int b = random.nextInt(10);
        // 最后偏的角度
        int i = 0;
        String s = "";
        for (String charct : chars) {
//            int c = a == 0 ? b : 360 - b;
//            g2d.rotate(c * Math.PI / 180);
            g2d.setColor(Color.RED);
            g2d.drawString(charct, 10 + i, 20);
            s += charct;
            i = i + 20;
        }


        g.dispose();
        try {
            ImageIO.write(image, "JPEG",file );
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
     ValidateImageGenerator.get().getSimplebercode(new FileOutputStream("e:/1.jpg"));
    }
}