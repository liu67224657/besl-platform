package com.enjoyf.platform.webapps.common.html.freemarktag;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class SubStringChinese implements TemplateMethodModel {
    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 2) {
            return null;
        }

        String text = String.valueOf(list.get(0));
        if (StringUtil.isEmpty(text)) {
            return text;
        }

        int length = 0;
        try {
            length = Integer.parseInt(String.valueOf(list.get(1)));
            return StringUtil.subString(text, length);
        } catch (NumberFormatException e) {
            GAlerter.lab(getClass().getName() + " occured NumberFormatException,", e);
            throw new TemplateModelException("length is not number", e);
        }
    }

}
