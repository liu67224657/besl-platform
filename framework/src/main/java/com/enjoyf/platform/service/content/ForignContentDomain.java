/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@fivewh.com">Eric Liu</a>
 * Create time: 11-8-17 下午5:21
 * Description:  外部文章表
 */
public class ForignContentDomain implements Serializable {
    private static Map<Integer, ForignContentDomain> map = new HashMap<Integer, ForignContentDomain>();

    //the original type
    public static final ForignContentDomain DEFAULT = new ForignContentDomain(0);
    public static final ForignContentDomain WIKI = new ForignContentDomain(1);
    public static final ForignContentDomain CMS = new ForignContentDomain(2);

	//3—短评 4-吐槽
	public static final ForignContentDomain SHORT_COMMENTS = new ForignContentDomain(3);
	public static final ForignContentDomain GAG = new ForignContentDomain(4);

    //5--wiki评论文章
    public static final ForignContentDomain WIKI_CONTENT = new ForignContentDomain(5);

    private int code;

    private ForignContentDomain(int c) {
        code = c;

        map.put(code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ForignContentDomain)) {
            return false;
        }

        return code == ((ForignContentDomain) obj).getCode();
    }

    public static ForignContentDomain getByCode(Integer c) {
        if (c == null) {
            return ForignContentDomain.DEFAULT;
        }

        return map.get(c) == null ? ForignContentDomain.DEFAULT : map.get(c);
    }

    public static Collection<ForignContentDomain> getAll() {
        return map.values();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
