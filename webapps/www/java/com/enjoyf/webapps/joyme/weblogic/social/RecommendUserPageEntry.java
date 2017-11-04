package com.enjoyf.webapps.joyme.weblogic.social;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.webapps.common.dto.profile.ProfileSimpleDTO;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">ericliu</a>
 * Create time: 12-9-2
 * Description:
 */
public class RecommendUserPageEntry {
    private boolean continueQuery;
    private PageRows<ProfileSimpleDTO> result;

    public RecommendUserPageEntry(boolean continueQuery, PageRows<ProfileSimpleDTO> result) {
        this.continueQuery = continueQuery;
        this.result = result;
    }

    public boolean isContinueQuery() {
        return continueQuery;
    }

    public void setContinueQuery(boolean continueQuery) {
        this.continueQuery = continueQuery;
    }

    public PageRows<ProfileSimpleDTO> getResult() {
        return result;
    }

    public void setResult(PageRows<ProfileSimpleDTO> result) {
        this.result = result;
    }
}
