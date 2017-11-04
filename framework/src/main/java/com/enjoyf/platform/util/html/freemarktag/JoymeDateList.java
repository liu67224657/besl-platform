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
public class JoymeDateList implements TemplateMethodModel {

    @Override
    public Object exec(List list) throws TemplateModelException {
        Date date = null;
        try {
            date = DateUtil.formatStringToDate(String.valueOf(list.get(0)), "yyyy-MM-dd HH:mm:ss");
            List<Date> dayList = DateUtil.getDayList(date, new Date());

            return dayList.size();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
