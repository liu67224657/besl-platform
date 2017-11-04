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
public class JoymeDate implements TemplateMethodModel {

    @Override
    public Object exec(List list) throws TemplateModelException {
        Date date = null;
        try {
            date = DateUtil.formatStringToDate(String.valueOf(list.get(0)), "yyyy-MM-dd HH:mm:ss");
            return DateUtil.parseDate(date);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return list.get(0);
    }
}
