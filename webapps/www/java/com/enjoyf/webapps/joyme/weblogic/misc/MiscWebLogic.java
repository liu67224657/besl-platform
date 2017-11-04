/**
 * CopyRight 2012 joyme.com
 */
package com.enjoyf.webapps.joyme.weblogic.misc;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.misc.IpForbidden;
import com.enjoyf.platform.service.misc.IpForbiddenField;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import org.springframework.cache.annotation.Cacheable;

/**
 * Author: zhaoxin
 * Date: 12-4-13
 * Time: 上午11:51
 * Desc:
 */

@Service(value = "miscWebLogic")
public class MiscWebLogic {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //  cache
//    @Cacheable("ipForbiddenCache")
    public List<IpForbidden> queryIpForbiddens(String id) {

        logger.debug("in queryIpForbiddens para id (null)"+ id);
        List<IpForbidden> returnList = null;
        try {
            returnList = MiscServiceSngl.get().queryIpForbiddensNoPagination(
                    new QueryExpress()
                            .add(QueryCriterions.ne(IpForbiddenField.VALIDSTATUS, ValidStatus.REMOVED.getCode()))
                            .add(QueryCriterions.geq(IpForbiddenField.UTILLDATE, new Date())));

        } catch (ServiceException e) {
            logger.debug("query IpForbidden caught an exception : " + e);
        }
        if(returnList != null){
            return returnList;
        }

        return new ArrayList<IpForbidden>();
    }
}
