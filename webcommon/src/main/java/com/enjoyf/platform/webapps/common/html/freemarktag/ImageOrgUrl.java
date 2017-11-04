package com.enjoyf.platform.webapps.common.html.freemarktag;

import com.enjoyf.platform.service.naming.BackupResourceDomain;
import com.enjoyf.platform.service.naming.ResourceServerMonitor;
import com.enjoyf.platform.util.StringUtil;

import com.enjoyf.platform.webapps.common.util.WebUtil;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.List;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class ImageOrgUrl implements TemplateMethodModel {
     private static String defaultURL = WebUtil.getUrlLib() + "/static/theme/default/img/default.jpg";

    @Override
    public Object exec(List list) throws TemplateModelException {
         String sReURL = defaultURL;
        String furl =String.valueOf(list.get(0));
        if (!StringUtil.isEmpty(furl)) {
            String sR = furl.substring(1, 5);
            sReURL = "http://" + getRxxx(sR) + "." + WebUtil.getDomain() + furl;
        }

        return sReURL;
    }

    private String getRxxx(String rxxx) {
        Map<String, BackupResourceDomain> downResourceDomainMap = ResourceServerMonitor.get().getDownResourceDomainMap();
        if (downResourceDomainMap.get(rxxx) != null) {
            rxxx = downResourceDomainMap.get(rxxx).getReRd();
        }
        return rxxx;
    }
}
