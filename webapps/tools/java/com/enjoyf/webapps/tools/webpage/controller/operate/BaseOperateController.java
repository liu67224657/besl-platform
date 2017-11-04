package com.enjoyf.webapps.tools.webpage.controller.operate;

import com.enjoyf.platform.service.misc.JoymeOperate;
import com.enjoyf.platform.service.misc.JoymeOperateType;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-4 下午1:47
 * Description:
 */
@Controller
@RequestMapping(value = "/operate")
public abstract class BaseOperateController extends ToolsBaseController {


    protected Map<String, Object> getSuccessResult(JoymeOperate operate, Map<String, Object> map) {

        map.put("key", "operate.success." + JoymeOperateType.REFRESH_INDEX.getCode());
        return map;
    }
}
