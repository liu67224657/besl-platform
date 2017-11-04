package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2016/9/18 0018.
 */
public class JoymewikiJt implements Serializable {
    private static Map<Integer, JoymewikiJt> map = new HashMap<Integer, JoymewikiJt>();

    private static Map<Integer, JoymewikiJt> toolsmap = new HashMap<Integer, JoymewikiJt>();

    public static final JoymewikiJt WAP = new JoymewikiJt(-2, "推送通知到webview页面(导航栏为默认)", true);    //wap

    //跳转到wiki页面
    public static final JoymewikiJt WAP_WIKI = new JoymewikiJt(-3, "wiki页面", true);

    private int code;
    private String name;
    private boolean toolsShow;

    public JoymewikiJt(int code, String name, boolean toolsShow) {
        this.code = code;
        this.name = name;
        this.toolsShow = toolsShow;
        if (toolsShow) {
            toolsmap.put(code, this);
        }
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((JoymewikiJt) o).code) return false;

        return true;
    }


    public boolean getToolsShow() {
        return toolsShow;
    }

    public void setToolsShow(boolean toolsShow) {
        this.toolsShow = toolsShow;
    }

    public static JoymewikiJt getByCode(int c) {
        return map.get(c);
    }

    public static Collection<JoymewikiJt> getAll() {
        return map.values();
    }


    public static Collection<JoymewikiJt> getToolsAll() {
        return toolsmap.values();
    }

    public static void main(String[] args) {
        Collection<JoymewikiJt> all = JoymewikiJt.getAll();
        for (JoymewikiJt jt : all) {
            System.out.println(jt);
        }

    }


}
