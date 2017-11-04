package com.enjoyf.platform.serv.content.processor;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentInteraction;
import com.enjoyf.platform.service.content.processor.ContentProcessUtil;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.TimeLineInsertEvent;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.social.RelationType;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.timeline.TimeLineContentType;
import com.enjoyf.platform.service.timeline.TimeLineDomain;
import com.enjoyf.platform.service.timeline.TimeLineFilterType;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-10-12
 * Time: 上午11:15
 * To change this template use File | Settings | File Templates.
 */
public class AtProcessor implements ContentProcessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AtProcessor.class);

    //
    public void process(Object obj) {
        if (obj instanceof Content) {
        } else if (obj instanceof ContentInteraction) {
        } else {
            GAlerter.lab("In contextProcessQueueThreadN, there is a unknown obj.");
        }
    }





}
