package com.enjoyf.webapps.joyme.weblogic.discovery;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-27
 * Time: 上午11:51
 * To change this template use File | Settings | File Templates.
 */
@Component
public class WallLayoutEngine {
    private static final Logger logger = LoggerFactory.getLogger(WallLayoutEngine.class);

    @Resource(name = "freemarkerConfig")
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public void setFreeMarkerConfigurer(
            FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    /**
     * 通过模板产生block html
     *
     * @param templateName 模板名称
     * @param map          模板中要填充的对象
     * @return block（HTML）
     */
    public String generateWallBlockHtml(String templateName, Map map) {

        //使用FreeMaker模板
        try {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template t = configuration.getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
        } catch (TemplateException e) {
            logger.error("Error while processing FreeMarker template ", e);
        } catch (FileNotFoundException e) {
            logger.error("Error while open template file ", e);
        } catch (IOException e) {
            logger.error("Error while generate wall layout block template ", e);
        } catch (Exception e) {
            logger.error("Error while generate wall layout template ", e);
        }
        return null;

    }
}
