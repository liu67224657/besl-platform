package com.enjoyf.platform.service.comment;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户权限类型
 * @author huazhang
 *
 */
public class CommentPermissionType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4610523196116515222L;

	private static Map<Integer, CommentPermissionType> map = new HashMap<Integer, CommentPermissionType>();

    public static final CommentPermissionType LIVE_COMMENT = new CommentPermissionType(1);//图文直播

    //
    private Integer code;

    private CommentPermissionType(Integer c) {
        code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "TokenType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof CommentPermissionType)) {
            return false;
        }

        return code==(((CommentPermissionType) obj).getCode());
    }

    public static CommentPermissionType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<CommentPermissionType> getAll() {
        return map.values();
    }
	
}
