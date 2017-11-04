/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.service.content.ContentType;
import com.google.common.base.Strings;
import org.apache.openjpa.event.NoneOrphanedKeyAction;

import java.io.Serializable;
import java.util.*;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ShareRewardType implements Serializable {

    public static final Integer NONE = 0;
    //我关注的人
    public static final Integer POINT = 1;
    //抽奖
    public static final Integer LOTTERY = 2;

    public static final Integer ALL = 3;


    private Integer value = 0;

    //
    public ShareRewardType() {
    }

    private ShareRewardType(Integer v) {
        value = v;
    }

    public ShareRewardType has(Integer v) {
        value += v;
        return this;
    }

    public Integer getValue() {
        return value;
    }

    public boolean isNone() {
        return value == NONE;
    }

    public boolean hasPoint() {
        return (value & POINT) > 0;
    }

    public boolean hasLottery() {
        return (value & LOTTERY) > 0;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "ShareRewardType: value=" + value;
    }
        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (value != ((ShareRewardType) o).value) return false;

        return true;
    }

    public static ShareRewardType getByValue(Integer v) {
        return new ShareRewardType(v);
    }
    public static List<ShareRewardType> getAll(){
        List<ShareRewardType> list = new ArrayList<ShareRewardType>();
        for(int i=0;i<4;i++){
            list.add(ShareRewardType.getByValue(i));
        }
        return list;
    }
}
