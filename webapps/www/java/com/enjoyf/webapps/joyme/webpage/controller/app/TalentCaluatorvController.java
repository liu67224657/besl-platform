package com.enjoyf.webapps.joyme.webpage.controller.app;


import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
@Controller
@RequestMapping("/app/sjcs/talentcaluatorv")
public class TalentCaluatorvController extends BaseRestSpringController {
    String SPLIT_TALENT = " ";
    String SPLIT_TALENTNAME_VALUE = "_";

    @RequestMapping
    public ModelAndView talentcaluatorv(@RequestParam(value = "c", required = false) String talentClass,
                                        @RequestParam(value = "t", required = false) String t) {
        Map<String, Object> messageMap = new HashMap<String, Object>();

        if (StringUtil.isEmpty(talentClass)) {
           talentClass="zs";
        }

        String group=SJCSTalentClassMapper.getGroupByTalentClass(talentClass);
        //todo
        if(StringUtil.isEmpty(group)){
            talentClass="zs";
            group=SJCSTalentClassMapper.getGroupByTalentClass(talentClass);
        }
        messageMap.put("talentClass", talentClass);
        messageMap.put("group", group);

        if (!StringUtil.isEmpty(t)) {
            String[] talentKeyValue = t.split(SPLIT_TALENT);
            Map<String, String> talentMap = new HashMap<String, String>();
            for (String talentStr : talentKeyValue) {
                String[] talent = talentStr.split(SPLIT_TALENTNAME_VALUE);
                if (talent.length == 2) {
                    talentMap.put(talent[0], talent[1]);
                }
            }
            messageMap.put("talent", talentMap);
        }

        return new ModelAndView("/views/jsp/app/sjcs-talent", messageMap);
    }
}
