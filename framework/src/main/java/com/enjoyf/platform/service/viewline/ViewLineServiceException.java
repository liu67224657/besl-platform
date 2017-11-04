package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ViewLineServiceException extends ServiceException {
    public static final ViewLineServiceException CATEGORY_NOT_EXISTS = new ViewLineServiceException(BASE_VIEWLINE + 1, "view category not exists");
    public static final ViewLineServiceException LINE_NOT_EXISTS = new ViewLineServiceException(BASE_VIEWLINE + 2, "view line not exists");


    public ViewLineServiceException(int i, String s) {
        super(i, s);
    }
}
