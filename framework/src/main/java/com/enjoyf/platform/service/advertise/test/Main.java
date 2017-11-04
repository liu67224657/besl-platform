/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.advertise.test;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertiseAgent;
import com.enjoyf.platform.service.advertise.AdvertiseAgentField;
import com.enjoyf.platform.service.advertise.AdvertiseProject;
import com.enjoyf.platform.service.advertise.AdvertiseProjectField;
import com.enjoyf.platform.service.advertise.AdvertisePublish;
import com.enjoyf.platform.service.advertise.AdvertisePublishField;
import com.enjoyf.platform.service.advertise.AdvertisePublishLocation;
import com.enjoyf.platform.service.advertise.AdvertisePublishLocationField;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-24 下午4:30
 * Description:
 */
public class Main {
    public static void main(String[] args) {
        //
        WebHotdeployConfig config = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);

        for (int i = 0; i < 100; i++) {
            System.out.println("221.123.189.2:" + config.getContentViewTimes("221.123.189.2"));
            System.out.println("221.123.189.4:" + config.getContentViewTimes("221.123.189.4"));
            System.out.println("221.123.189.24:" + config.getContentViewTimes("221.123.189.24"));
        }

        //
        Utility.sleep(10000);
    }

    public static AdvertiseAgent agent(String[] args) {
        AdvertiseAgent agent = new AdvertiseAgent();

        agent.setAgentName("test adv agent");
        agent.setCreateUserid("ypy");
        agent.setCreateIp("127.0.0.1");

        try {
            agent = AdvertiseServiceSngl.get().createAgent(agent);
            System.out.println(agent);

            System.out.println(AdvertiseServiceSngl.get().getAgent(agent.getAgentId()));

            UpdateExpress updateExpress = new UpdateExpress();

            updateExpress.set(AdvertiseAgentField.AGENTDESC, "updated desc");
            updateExpress.set(AdvertiseAgentField.VALIDSTATUS, ValidStatus.REMOVED.getCode());

            updateExpress.set(AdvertiseAgentField.UPDATEIP, "127.0.0.2");
            updateExpress.set(AdvertiseAgentField.UPDATEDATE, new Date());
            updateExpress.set(AdvertiseAgentField.UPDATEUSERID, "ypy2");

            System.out.println(AdvertiseServiceSngl.get().modifyAgent(updateExpress, agent.getAgentId()));

            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(AdvertiseAgentField.AGENTNAME, agent.getAgentName()));

            System.out.println(AdvertiseServiceSngl.get().queryAgents(queryExpress));

        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return agent;
    }

    public static AdvertiseProject project(String[] args) {
        AdvertiseProject project = new AdvertiseProject();

        project.setProjectName("test adv project");
        project.setCreateUserid("ypy");
        project.setCreateIp("127.0.0.1");


        try {
            project = AdvertiseServiceSngl.get().createProject(project);
            System.out.println(project);

            System.out.println(AdvertiseServiceSngl.get().getProject(project.getProjectId()));

            UpdateExpress updateExpress = new UpdateExpress();

            updateExpress.set(AdvertiseProjectField.PROJECTDESC, "updated project desc");
            updateExpress.set(AdvertiseProjectField.VALIDSTATUS, ValidStatus.REMOVED.getCode());

            updateExpress.set(AdvertiseProjectField.UPDATEIP, "127.0.0.2");
            updateExpress.set(AdvertiseProjectField.UPDATEDATE, new Date());
            updateExpress.set(AdvertiseProjectField.UPDATEUSERID, "ypy2");

            System.out.println(AdvertiseServiceSngl.get().modifyProject(updateExpress, project.getProjectId()));

            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(AdvertiseProjectField.PROJECTNAME, project.getProjectName()));

            System.out.println(AdvertiseServiceSngl.get().queryProjects(queryExpress));

        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return project;
    }

    public static AdvertisePublish publish(AdvertiseProject project, AdvertiseAgent agent) {
        AdvertisePublish publish = new AdvertisePublish();

        publish.setAgentId(agent.getAgentId());
        publish.setProjectId(project.getProjectId());
        publish.setRedirectUrl("http://www.joyme.com/home");

        publish.setPublishName("test adv publish");
        publish.setCreateUserid("ypy");
        publish.setCreateIp("127.0.0.1");


        try {
            publish = AdvertiseServiceSngl.get().createPublish(publish);
            System.out.println(publish);

            System.out.println(AdvertiseServiceSngl.get().getPublish(publish.getPublishId()));

            UpdateExpress updateExpress = new UpdateExpress();

            updateExpress.set(AdvertisePublishField.PUBLISHDESC, "updated publish desc");
            updateExpress.set(AdvertisePublishField.VALIDSTATUS, ValidStatus.REMOVED.getCode());

            updateExpress.set(AdvertisePublishField.UPDATEIP, "127.0.0.2");
            updateExpress.set(AdvertisePublishField.UPDATEDATE, new Date());
            updateExpress.set(AdvertisePublishField.UPDATEUSERID, "ypy2");

            System.out.println(AdvertiseServiceSngl.get().modifyPublish(updateExpress, publish.getPublishId()));

            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(AdvertisePublishField.PROJECTID, publish.getProjectId()));

            System.out.println(AdvertiseServiceSngl.get().queryPublishs(queryExpress));

        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return publish;
    }

    public static void loaction(AdvertisePublish publish) {
        AdvertisePublishLocation location = new AdvertisePublishLocation();

        location.setPublishId(publish.getPublishId());

        location.setLocationName("test adv publish location");
        location.setLocationCode("xyz");

        location.setCreateUserid("ypy");
        location.setCreateIp("127.0.0.1");


        try {
            location = AdvertiseServiceSngl.get().createPublishLocation(location);
            System.out.println(location);

            System.out.println(AdvertiseServiceSngl.get().getPublishLocation(location.getPublishId(), location.getLocationCode()));

            UpdateExpress updateExpress = new UpdateExpress();

            updateExpress.set(AdvertisePublishLocationField.LOCATIONDESC, "updated publish location desc");
            updateExpress.set(AdvertisePublishLocationField.VALIDSTATUS, ValidStatus.REMOVED.getCode());


            System.out.println(AdvertiseServiceSngl.get().modifyPublishLocation(updateExpress, location.getPublishId(), location.getLocationCode()));

            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(AdvertisePublishLocationField.PUBLISHID, location.getPublishId()));

            System.out.println(AdvertiseServiceSngl.get().queryPublishLocations(queryExpress));

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
