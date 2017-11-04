package com.enjoyf.webapps.tools.webpage.controller.task;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/12
 * Description:
 */
public class AbstracToolsTaskController extends ToolsBaseController {
    private static Set<AuthApp> appKeys = new LinkedHashSet<AuthApp>();

    private static Set<Integer> platforms = new HashSet<Integer>();

    static {

    }

    protected Set<AuthApp> getTaskApps() {
        if (CollectionUtil.isEmpty(appKeys)) {
            try {
                AuthApp app = OAuthServiceSngl.get().getApp("17yfn24TFexGybOF0PqjdY");
                AuthApp youku = OAuthServiceSngl.get().getApp("08pkvrWvx5ArJNvhYf19kN");
                appKeys.add(app);
                appKeys.add(youku);
            } catch (Exception e) {
            }
        }
        return appKeys;
    }


    protected Set<Integer> getPlatforms() {
        if (CollectionUtil.isEmpty(platforms)) {
            platforms.add(AppPlatform.ANDROID.getCode());
            platforms.add(AppPlatform.IOS.getCode());
        }
        return platforms;
    }
}
