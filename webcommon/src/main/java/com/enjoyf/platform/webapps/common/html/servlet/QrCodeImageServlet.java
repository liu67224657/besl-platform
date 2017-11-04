package com.enjoyf.platform.webapps.common.html.servlet;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enjoyf.platform.util.barcode.QRCodeImageUtil;

import com.google.common.base.Strings;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class QrCodeImageServlet extends HttpServlet {
	
    //the constants
    private final static int IMAGE_W = 114;
    private final static int IMAGE_H = 114;

    //the image obj cache
    private final static Map<String, RenderedImage> cache = new HashMap<String, RenderedImage>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String src = request.getParameter("src");

        //
        if (!Strings.isNullOrEmpty(src)) {
            //get the image from the cache
            RenderedImage image = cache.get(src);
            if (image == null) {
                //create the image of the src
                image = QRCodeImageUtil.createQRImage(src, IMAGE_W, IMAGE_H);

                cache.put(src, image);
            }

            //output to response.
            ImageIO.write(image, "JPEG", response.getOutputStream());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
