/**
 *
 */
package com.enjoyf.webapps.joyme.weblogic.sys;

import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.misc.Region;
import com.enjoyf.platform.service.service.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xinzhao
 */
@Service(value = "sysCommWebLogic" +
        "")
public class SysCommWebLogic {

    /*
      * @see
      * com.enjoyf.webapps.service.weblogic.sys.ISysComm#getRegionAllList()
      */
    public List<Region> getRegionAllList() throws ServiceException {
        return MiscServiceSngl.get().getAllRegions();
    }

}
