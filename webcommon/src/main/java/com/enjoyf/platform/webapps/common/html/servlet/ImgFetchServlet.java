package com.enjoyf.platform.webapps.common.html.servlet;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
public class ImgFetchServlet extends HttpServlet {
	
    private final static Map<String, BufferedImage> cache = new HashMap<String, BufferedImage>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //
        String src = request.getParameter("src");

        if (!Strings.isNullOrEmpty(src)) {
            BufferedImage destBufImg = cache.get(src);

            if (destBufImg == null) {
                //fetch the jpg from url
                Image srcImg = javax.imageio.ImageIO.read(new URL(src));

                int srcW = srcImg.getWidth(null);
                int srcH = srcImg.getHeight(null);

                destBufImg = new BufferedImage(srcW, srcH, BufferedImage.TYPE_INT_RGB);
                destBufImg.getGraphics().drawImage(srcImg, 0, 0, srcW, srcH, null);

                cache.put(src, destBufImg);
            }

            // 输出图象到页面
            ImageIO.write(destBufImg, "JPEG", response.getOutputStream());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
