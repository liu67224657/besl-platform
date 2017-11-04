package com.enjoyf.webapps.joyme.webpage.controller.giftmarket;

import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.zxing.EncodeHintType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-9-21
 * Time: 下午1:38
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/acitivty/qrcode")
public class ActivityQRcodeController extends AbstractGiftMarketBaseController {


    @RequestMapping(value = "/generator")
    public void page(@RequestParam(value = "height", required = false, defaultValue = "200") Integer height,
                     @RequestParam(value = "width", required = false, defaultValue = "200") Integer width,
                     @RequestParam(value = "url", required = false) String url,
                     HttpServletResponse response) {

        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            QRcodeGenerator.genCode(url, width, height, stream);
            stream.flush();
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        } finally {
            StreamUtil.closeOutputStream(stream);
        }
    }

    /**
     * 无边框
     * @param height
     * @param width
     * @param url
     * @param response
     */
    @RequestMapping(value = "/removal")
    public void removal(@RequestParam(value = "height", required = false, defaultValue = "200") Integer height,
                     @RequestParam(value = "width", required = false, defaultValue = "200") Integer width,
                     @RequestParam(value = "url", required = false) String url,
                     HttpServletResponse response) {

        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            QRcodeGenerator.genRemovalCode(url, width, height, stream);
            stream.flush();
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        } finally {
            StreamUtil.closeOutputStream(stream);
        }
    }

}
