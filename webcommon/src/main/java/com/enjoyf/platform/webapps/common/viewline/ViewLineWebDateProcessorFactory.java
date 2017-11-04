/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.viewline;

import com.enjoyf.platform.service.viewline.ViewItemType;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:30
 * Description:
 */
public class ViewLineWebDateProcessorFactory {
    //
    private Map<ViewItemType, ViewLineWebDataProcessor> processorMap = new HashMap<ViewItemType, ViewLineWebDataProcessor>();

    //
    private static ViewLineWebDateProcessorFactory instance;

    public static ViewLineWebDateProcessorFactory get() {
        //
        if (instance == null) {
            synchronized (ViewLineWebDateProcessorFactory.class) {
                if (instance == null) {
                    instance = new ViewLineWebDateProcessorFactory();
                }
            }
        }

        return instance;
    }

    public ViewLineWebDataProcessor factory(ViewItemType lineItemType) {
        //
        ViewLineWebDataProcessor returnValue;

        //
        synchronized (processorMap) {
            returnValue = processorMap.get(lineItemType);

            //
            if (returnValue == null) {
                //
                if (ViewItemType.CONTENT.equals(lineItemType)) {

                    returnValue = new ViewLineContentWebDataProcessor();
                } else if (ViewItemType.GAME.equals(lineItemType)) {

                    returnValue = new ViewLineGameWebDataProcessor();
                } else if (ViewItemType.GROUP.equals(lineItemType)) {

                    returnValue = new ViewLineGroupWebDataProcessor();
                } else if (ViewItemType.PROFILE.equals(lineItemType)) {

                    returnValue = new ViewLineProfileWebDataProcessor();
                } else if (ViewItemType.CUSTOM.equals(lineItemType)) {

                    returnValue = new ViewLineCustomWebDataProcessor();
                } else if (ViewItemType.WIKI.equals(lineItemType)) {

                    returnValue = new ViewLineWikiWebDataProcessor();
                } else if (ViewItemType.ACTIVITY.equals(lineItemType)) {

                    returnValue = new ViewLineActivityWebDataProcessor();
                } else {
                    GAlerter.lab("ViewLineWebDataProcessor found an unknown line item type.");
                }

                if (returnValue != null) {
                    processorMap.put(lineItemType, returnValue);
                }
            }
        }

        //
        return returnValue;
    }
}
