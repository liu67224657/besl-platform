package com.enjoyf.platform.util.template;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.CollectionUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class FreemarkerTemplateGenerator {

    private static FreemarkerTemplateGenerator instance = null;

    private Configuration configuration = new Configuration();

    public synchronized static FreemarkerTemplateGenerator get() throws IOException {
        if (instance == null) {
            instance = new FreemarkerTemplateGenerator();
        }
        return instance;
    }

    private FreemarkerTemplateGenerator() throws IOException {

        configuration.setDirectoryForTemplateLoading(new File(getTemplateRootRealPath()).getParentFile().getParentFile());
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateUpdateDelay(5);
        configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        configuration.setTimeFormat("HH:mm:ss");
        configuration.setNumberFormat("0.######");
        configuration.setBooleanFormat("true,false");
        configuration.setWhitespaceStripping(true);
        configuration.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
        configuration.setURLEscapingCharset("UTF-8");
    }

    private String getTemplateRootRealPath() {
        return FreemarkerTemplateGenerator.class.getClassLoader().getResource("hotdeploy/joymetemplate").getPath();
    }

    /**
     * 产生模板
     *
     * @param templateName 模板URL
     * @param params          模板中要填充的对象
     * @return 邮件正文（HTML）
     */
    public String generateTemplate(String templateName, Map<String, Object> params) throws IOException, TemplateException {
        Map<String, Object> map = new HashMap<String, Object>();

        //使用FreeMaker模板
        Template t = configuration.getTemplate(templateName);
        map.put("URL_WWW", WebappConfig.get().getUrlWww());
        map.put("URL_LIB", WebappConfig.get().getUrlLib());
        map.put("URL_STATIC", WebappConfig.get().getUrlStatic());
        map.put("DOMAIN", WebappConfig.get().getDomain());
        if (!CollectionUtil.isEmpty(params)) {
            map.putAll(params);
        }

        return FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
    }
}
