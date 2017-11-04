package com.enjoyf.platform.serv.social;

import com.enjoyf.platform.serv.social.SocialLogic;
import com.enjoyf.platform.service.social.SocialRecommend;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">Eric Liu</a>
 * Create time: 12-8-30
 * Description:
 */
public interface RecommendCalStrategy {

    public SocialRecommend calculate(String srcUno);
}
