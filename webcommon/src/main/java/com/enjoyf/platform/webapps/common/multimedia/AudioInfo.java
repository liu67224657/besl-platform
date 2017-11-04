package com.enjoyf.platform.webapps.common.multimedia;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.List;

/**
 * <p/>
 * Description:Json格式的返回对象
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class AudioInfo {
    private int total;
    private List<Audio> results;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Audio> getResults() {
        return results;
    }

    public void setResults(List<Audio> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
