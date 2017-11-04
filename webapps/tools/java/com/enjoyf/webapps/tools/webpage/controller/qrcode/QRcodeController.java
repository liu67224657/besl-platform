package com.enjoyf.webapps.tools.webpage.controller.qrcode;

import com.enjoyf.platform.util.QRcodeGenerator;
import com.enjoyf.platform.util.StreamUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-21 下午6:24
 * Description:
 */
@Controller
@RequestMapping(value = "/qrcode")
public class QRcodeController {

    @RequestMapping(value = "/page")
    public ModelAndView page() {
        return new ModelAndView("/qrcode/page");
    }

    @RequestMapping(value = "/generator")
    public void page(@RequestParam(value = "height", required = false, defaultValue = "200") Integer height,
                     @RequestParam(value = "width", required = false, defaultValue = "200") Integer width,
                     @RequestParam(value = "content", required = false) String content,
                     HttpServletResponse response) {

        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            QRcodeGenerator.genCode(content, width, height, stream);
            stream.flush();
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        } finally {
            StreamUtil.closeOutputStream(stream);
        }
    }
}
