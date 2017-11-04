package com.enjoyf.platform.props.hotdeploy;


import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.content.Mood;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
public class MoodHotdeployConfig extends HotdeployConfig {
    private static Logger logger = LoggerFactory.getLogger(MoodHotdeployConfig.class);


    //表情列表
    private static final String KEY_MOOD_GROUP_LIST = "mood.group.list";

    //表情代码对应的图片地址
    private static final String SUFFIX_MOOOD_LIST = ".code.list";
    private static final String SUFFIX_CODE_IMAGE = ".code.img.url";


    private MoodCache cache;

    public MoodHotdeployConfig() {
        super(EnvConfig.get().getMoodHotdeployConfigureFile());
    }

    public void init() {
        logger.debug("MoodHotdeployConfig Props init start......");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        Map<String, Mood> moodCodeMap = new LinkedHashMap<String, Mood>();
        Map<String,List<Mood>> moodMap = new LinkedHashMap<String, List<Mood>>();

        List<String> groupList = getList(KEY_MOOD_GROUP_LIST);
        for (String group : groupList) {

            List<String> codeList = getList(group + SUFFIX_MOOOD_LIST);
            List<Mood> moodList=new ArrayList<Mood>();
            for (String code : codeList) {
                String url = getString(code + SUFFIX_CODE_IMAGE);
                Mood mood = new Mood(code, url);
                moodCodeMap.put(code,mood);

                moodList.add(mood);
            }

           moodMap.put(group,moodList);
        }

        MoodCache tmp = new MoodCache();
        tmp.setMoodMap(moodMap);
        tmp.setMoodCodeMap(moodCodeMap);

        cache = tmp;

        logger.info("MoodHotdeployConfig template Props init finished.");
    }

    /**
     * 通过图片的代码得到code如果代码无效返回空
     *
     * @param code
     * @return
     */
    public Mood getImageUrlByCode(String code) {
        return cache.getMoodCodeMap().get(code);
    }

    public Map<String,List<Mood>> getImageMap() {
        return cache.getImageMap();
    }

    private class MoodCache {
        private Map<String, Mood> moodCodeMap = new LinkedHashMap<String, Mood>();

        private Map<String, List<Mood>> moodMap = new LinkedHashMap<String, List<Mood>>();

        private MoodCache() {
        }

        public Map<String, List<Mood>> getMoodMap() {
            return moodMap;
        }

        public void setMoodMap(Map<String, List<Mood>> moodMap) {
            this.moodMap = moodMap;
        }

        private Map<String, List<Mood>> getImageMap() {
            return moodMap;
        }

        public Map<String, Mood> getMoodCodeMap() {
            return moodCodeMap;
        }

        public void setMoodCodeMap(Map<String, Mood> moodCodeMap) {
            this.moodCodeMap = moodCodeMap;
        }
    }
}
