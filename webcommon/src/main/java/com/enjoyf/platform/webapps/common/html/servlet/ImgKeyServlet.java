package com.enjoyf.platform.webapps.common.html.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class ImgKeyServlet extends HttpServlet {
	
    public final static String KEY_SESSION_IMG_KEY = "ImgKeyServlet";

    private String bg;
    private String ft;

    // 验证码图片的宽度。
    private int width = 70;

    // 验证码图片的高度。
    private int height = 26;

    // 验证码字符个数
    private int codeCount = 4;

    // 随机码
    char[] codeSequence = {'a', 'b', 'c', 'd', 'e', 'f', 'h', 'i', 'k', 'm', 'n', 'p', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '2', '3', '4',
            '5', '6', '7', '8', '9'};

    // 设置字体样式
    // private String fontStr = "Times New Roman";
    private String fontStr = "Georgia";
    // private String fontStr = "Verdana";
    // private int fontStyle = Font.PLAIN;
    private int fontStyle = Font.PLAIN;
    private int fontSize = 18;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置p3p
        response.setHeader("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

        // 设置页面不缓存
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        bg = request.getParameter("bg");
        ft = request.getParameter("ft");

        // 在内存中创建图象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics g = image.getGraphics();

        // 生成随机类
        Random random = new Random();

        // 设定背景色
        if (Strings.isNullOrEmpty(bg)) {
            g.setColor(Color.WHITE);
        } else {
            try {
                String[] b = bg.split("-");
                // rgb颜色模式
                g.setColor(new Color(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2])));
            }
            catch (Exception e) {
                g.setColor(Color.WHITE);
            }
        }

        g.fillRect(0, 0, width, height);

        // 设定字体
        g.setFont(new Font(fontStr, fontStyle, fontSize));

        // 画边框
        // g.setColor(Color.black);
        // g.drawRect(0, 0, width - 1, height - 1);

        // 设置干扰线和字体颜色
        if (Strings.isNullOrEmpty(ft)) {
            g.setColor(Color.BLACK);
        } else {
            try {
                String[] b = ft.split("-");
                // rgb颜色模式
                g.setColor(new Color(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2])));
            }
            catch (Exception e) {
                g.setColor(Color.BLACK);
            }
        }

        // 随机产生黑色干扰线，使图象中的认证码不易被其它程序探测到
        for (int i = 0; i < 1; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }
        // 随机点
        for (int i = 0; i < 50; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(1);
            int y2 = random.nextInt(1);
            g.drawLine(x1, y1, x1 + x2, y1 + y2);
        }

        // 取随机产生的认证码
        String code = "";
        for (int i = 0; i < codeCount; i++) {
            String rand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            code += rand;
            // 将认证码显示到图象中
            g.drawString(rand, 14 * i + 6, 14 + random.nextInt(11));// 高度不对齐
        }

        // 将认证码存入SESSION
        request.getSession().setAttribute(KEY_SESSION_IMG_KEY, code);

        // 图象生效
        g.dispose();

        // 输出图象到页面
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * 给定范围获得随机颜色
     *
     * @param fc int
     * @param bc int
     * @return Color
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}