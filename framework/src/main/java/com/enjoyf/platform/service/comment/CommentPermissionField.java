/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * 图文直播发布权限
 * @author huazhang
 *
 */
public class CommentPermissionField extends AbstractObjectField {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	
	public static final CommentPermissionField PERMISSIONID = new CommentPermissionField("permission_id", ObjectFieldDBType.STRING, true, false);
    public static final CommentPermissionField PROFILEID = new CommentPermissionField("profile_id", ObjectFieldDBType.STRING, true, false);
    public static final CommentPermissionField STATUS = new CommentPermissionField("status", ObjectFieldDBType.STRING, true, false);
    public static final CommentPermissionField NICK = new CommentPermissionField("nick", ObjectFieldDBType.STRING, true, false);
    public static final CommentPermissionField PERMISSIONTYPE = new CommentPermissionField("permission_type", ObjectFieldDBType.INT, true, false);

    public CommentPermissionField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public CommentPermissionField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
