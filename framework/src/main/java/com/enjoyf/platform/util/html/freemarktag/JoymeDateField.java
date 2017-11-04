package com.enjoyf.platform.util.html.freemarktag;

import com.enjoyf.platform.util.DateUtil;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class JoymeDateField implements TemplateMethodModel {

    @Override
    public Object exec(List list) throws TemplateModelException {
        Date date = null;
        try {
            date = DateUtil.formatStringToDate(String.valueOf(list.get(0)), "yyyy-MM-dd HH:mm:ss");
            List<Date> dayList = DateUtil.getDayList(date, new Date());

            if (dayList.size() <= 2) {
                return "今天";
            }

            if (dayList.size() <= 3) {
                return "三天内";
            }

            if (dayList.size() <= 4) {
                return "四天内";
            }

            if (dayList.size() <= 5) {
                return "五天内";
            }

            if (dayList.size() <= 6) {
                return "六天内";
            }

            if (dayList.size() <= 7) {
                return "七天内";
            }

            if (dayList.size() <= 60) {
                return "两周内";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "一月内";
    }
}
