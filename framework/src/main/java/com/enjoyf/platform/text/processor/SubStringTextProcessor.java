package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.regex.RegexUtil;

import java.util.regex.Matcher;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SubStringTextProcessor implements TextProcessor {
    private int length;
    private String suspensionPoints;
    private int line;

    public SubStringTextProcessor(int length, String suspensionPoints, int line) {
        this.length = length;
        this.suspensionPoints = suspensionPoints;
        this.line = line;
    }

    @Override
    public ResolveContent process(ResolveContent content) {

        boolean isNeedSubString = StringUtil.length(content.getContent()) > length;
        String srcText = StringUtil.subString(content.getContent(), length);

        Matcher m = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getLineBreaksFetchRegex().matcher(srcText);

        int idxTextEnd = 0;

        int matchTimes = 0;
        while (m.find()) {
            matchTimes++;
            idxTextEnd = m.end() - 1;
            if (line > 0 && matchTimes >= line) {
                srcText = srcText.substring(0, idxTextEnd);
                isNeedSubString = true;
                break;
            }
        }
        srcText = removeInComplete(srcText);
        if (!StringUtil.isEmpty(suspensionPoints)) {
            srcText += (isNeedSubString ? suspensionPoints : "");
        }

        content.setContent(srcText);
        return content;
    }

    private static String removeInComplete(String content) {
        content = RegexUtil.replace(content, HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getInCompleteFetchRegex(), "", -1);
        return content;
    }
}
