package com.enjoyf.platform.serv.sync;

import com.enjoyf.platform.service.sync.ShareBody;
import com.enjoyf.platform.service.sync.ShareInfo;
import com.enjoyf.platform.text.ImageResolveUtil;
import com.enjoyf.platform.util.StringUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-7
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
public class ShareContextProcessor {

    public SyncContent generatorContext(SyncContent syncContent) {

        syncContent.setSyncText(generatorShareContent(syncContent));

        return syncContent;
    }

    private String generatorShareContent(SyncContent syncContent) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!StringUtil.isEmpty(syncContent.getSyncTopic())) {
            stringBuilder.append("#").append(syncContent.getSyncTopic()).append("# ");
        }
        stringBuilder.append(syncContent.getSyncText()).append(" ");
        if(!syncContent.getSyncContentUrl().startsWith("http://") && !syncContent.getSyncContentUrl().startsWith("https://")){
         stringBuilder.append("http://");
        }
        stringBuilder.append(syncContent.getSyncContentUrl());
        return stringBuilder.toString();
    }





}
