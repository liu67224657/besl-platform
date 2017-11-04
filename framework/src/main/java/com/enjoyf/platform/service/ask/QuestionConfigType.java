package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhimingli on 2016/9/12 0012.
 */
public class QuestionConfigType implements Serializable {

    /**
     * 5分钟-100分
     * 10分钟-60分
     * 20分钟-40分
     * 30分钟-20分
     */
    private static List<QuestionConfig> list = new ArrayList<QuestionConfig>();

    public static QuestionConfig THIRTY_MIN = new QuestionConfig(30 * 60 * 1000, 20, "30分钟");
    public static QuestionConfig TWENTY_MIN = new QuestionConfig(20 * 60 * 1000, 40, "20分钟");
    public static QuestionConfig TEN_MIN = new QuestionConfig(10 * 60 * 1000, 60, "10分钟");
    public static QuestionConfig FIV_MIN = new QuestionConfig(5 * 60 * 1000, 100, "5分钟");
    public static QuestionConfig HOUR_24 = new QuestionConfig(24 * 60 * 60 * 1000, 30, "24小时");
    public static QuestionConfig HOUR_48 = new QuestionConfig(48 * 60 * 60 * 1000, 0, "48小时");



//    public static QuestionConfig THIRTY_ONE_TEST1 = new QuestionConfig(1 * 60 * 1000, 0);
//    public static QuestionConfig THIRTY_FIVE_TEST1 = new QuestionConfig(5 * 60 * 1000, 0);
//    public static QuestionConfig THIRTY_MIN_TEST1 = new QuestionConfig(11 * 60 * 1000, 0);
//    public static QuestionConfig THIRTY_MIN_TEST2 = new QuestionConfig(21 * 60 * 1000, 0);
//    public static QuestionConfig THIRTY_MIN_TEST3 = new QuestionConfig(61 * 60 * 1000, 0);

    static {
        list.add(THIRTY_MIN);
        list.add(TWENTY_MIN);
        list.add(TEN_MIN);
        list.add(FIV_MIN);
        list.add(HOUR_24);
        list.add(HOUR_48);

//        list.add(THIRTY_ONE_TEST1);
//        list.add(THIRTY_FIVE_TEST1);
//        list.add(THIRTY_MIN_TEST1);
//        list.add(THIRTY_MIN_TEST2);
//        list.add(THIRTY_MIN_TEST3);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public static List<QuestionConfig> getAll() {
        return list;
    }


    public static QuestionConfig getByCode(Long time) {
        if (time < 0) {
            return null;
        }
        QuestionConfig returnConfig = null;
        for (QuestionConfig config : list) {
            if (config.getTimeLimit() == time) {
                returnConfig = config;
                break;
            }
        }
        return returnConfig;
    }

}
