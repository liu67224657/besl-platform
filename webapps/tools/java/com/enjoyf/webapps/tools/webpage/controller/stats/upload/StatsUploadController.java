package com.enjoyf.webapps.tools.webpage.controller.stats.upload;

import com.enjoyf.platform.service.stats.*;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-10 上午11:35
 * Description:
 */
@Controller
@RequestMapping(value = "/stats/upload")
public class StatsUploadController extends ToolsBaseController {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    @RequestMapping(value = "/page")
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response) {
        //
        return new ModelAndView("/stats/upload/page");
    }

    //
    @RequestMapping(value = "/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "uploadfile", required = false) MultipartFile uploadFile) {
        //
        try {
            String line;
            int lineNum = 0;
            Reader reader = new InputStreamReader(uploadFile.getInputStream(), "gb18030");
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((line = bufferedReader.readLine()) != null) {
                if (lineNum == 0) {
                    System.out.println("This line is head.");
                } else {
                    StatItem item = parse(line);
                    //
                    if (item != null) {
                        //
                        if (logger.isDebugEnabled()) {
                            logger.debug("The stat ites is " + item);
                        }

                        //
                        StatServiceSngl.get().reportStat(item);
                    } else {
                        logger.warn("The line in the file is error.");
                    }
                }
                lineNum++;
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (Exception e) {
            GAlerter.lab("UploadController upload file error.", e);
        }

        //
        return new ModelAndView("redirect:/stats/upload/page");
    }

    private StatItem parse(String line) {
        StatItem returnValue = null;
        try {
            line = StringUtil.regReplace(line, ",", ", ", 2);
            StringTokenizer tokenizer = new StringTokenizer(line, ",");
            if (tokenizer.countTokens() >= 9) {
                String domain = tokenizer.nextToken().trim();
                String section = tokenizer.nextToken().trim();
                String dateType = tokenizer.nextToken().trim();
                String date = tokenizer.nextToken().trim();
                String value = tokenizer.nextToken().trim();
                String extS01 = tokenizer.nextToken().trim();
                String extS02 = tokenizer.nextToken().trim();
                String extN01 = tokenizer.nextToken().trim();
                String extN02 = tokenizer.nextToken().trim();

                returnValue = new StatItem();
                returnValue.setStatDomain(new StatDomainDefault("ops|" + domain));
                returnValue.setStatSection(new StatSectionDefault(section));
                returnValue.setDateType(StatDateType.getByCode(dateType));
                returnValue.setStatDate(returnValue.getDateType().getStartDateByType(DateUtil.formatStringToDate(date, "yyyy-MM-dd")));
                returnValue.setStatValue(Long.valueOf(value));

                StatItemExtData extData = new StatItemExtData();
                extData.setExtValue01(extS01);
                extData.setExtValue02(extS02);
                extData.setExtValue03(extN01);
                extData.setExtValue04(extN02);

                returnValue.setExtData(extData);

                returnValue.setReportDate(new Date());
            }
        } catch (ParseException e) {
            e.printStackTrace();
            returnValue = null;
        }

        return returnValue;
    }
}

