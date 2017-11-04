package com.enjoyf.platform.service.content;


import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
public class Mood {
    private String code;
    private String imgUrl;
//    private String imgClass="imgcss1";

    public Mood(String code, String imgUrl) {
        this.code = code;
        this.imgUrl = imgUrl;
    }

    public String getCode() {
        return code;
    }

    public String getImgUrl() {
        return imgUrl;
    }

//    public void setImgClass(String imgClass) {
//        this.imgClass = imgClass;
//    }
//
//    public String getImgClass() {
//        return imgClass;
//    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
