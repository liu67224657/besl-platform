/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.collection;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.event.user.UserEventType;
import com.enjoyf.platform.service.naming.NamingServiceAddress;
import com.enjoyf.platform.service.naming.NamingServiceMulti;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.Utility;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-12-3 下午8:00
 * Description:
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static FQueueQueue queue = new FQueueQueue("/service", "test");
    private static Integer next = 0;

    public static void main(String[] args) {
        f();
    }

    public static void a() {
        EnvConfig.get();

        // init queue thread
        QueueThreadN eventProcessQueueThreadN = new QueueThreadN(32, new QueueListener() {
            public void process(Object obj) {
                int v = next();

                if ((v % 100) == 0) {
                    System.out.println("@@" + v);
                }
            }
        }, queue);

        //
        long t = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            UserEvent userEvent = new UserEvent(UUID.randomUUID().toString());

            userEvent.setEventType(UserEventType.USER_CONTENT_REPLY_REMOVE);
            userEvent.setCount(i);
            userEvent.setEventDate(new Date());
            userEvent.setEventIp("127.0.0.1");


            eventProcessQueueThreadN.add(userEvent);
        }
        System.out.println(">>>" + (System.currentTimeMillis() - t));
    }

    public static void b() {
        //
        long t = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            UserEvent userEvent = new UserEvent(UUID.randomUUID().toString());

            userEvent.setEventType(UserEventType.USER_CONTENT_REPLY_REMOVE);
            userEvent.setCount(i);
            userEvent.setEventDate(new Date());
            userEvent.setEventIp("127.0.0.1");

            queue.add(userEvent);
        }
        System.out.println(">>>" + (System.currentTimeMillis() - t));

        //logger.is

        t = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Object obj = queue.get();
            if (obj == null) {
                System.out.println("error");
            }
        }
        System.out.println("@@@" + (System.currentTimeMillis() - t));
    }

    private static int next() {
        int returnValue = 0;

        synchronized (next) {
            next++;

            returnValue = next.intValue();
        }

        return returnValue;
    }

    public static void c() {
        FiveProps props = Props.instance().getServProps();

        MemDiskCacheConfig config = new MemDiskCacheConfig("er", props);
        CacheManager cacheManager = new CacheManager(config);

        Cache cache = cacheManager.getCache("test");

        long t = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            UserEvent userEvent = new UserEvent(UUID.randomUUID().toString());

            userEvent.setEventType(UserEventType.USER_CONTENT_REPLY_REMOVE);
            userEvent.setCount(i);
            userEvent.setEventDate(new Date());
            userEvent.setEventIp("127.0.0.1");

            cache.put(new Element(i, userEvent));
        }
        System.out.println(">>>" + (System.currentTimeMillis() - t));

        t = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            System.out.println(cache.get(i));
        }
        System.out.println(">>>" + (System.currentTimeMillis() - t));
    }

    public static void e() {
        NamingServiceAddress namingServiceAddress = new NamingServiceAddress();
        namingServiceAddress.setFromProp(EnvConfig.get().getNamingHostName(), EnvConfig.get().getNamingPort());

        Set<String> serviceTypeSet = new HashSet<String>();
        serviceTypeSet.add("image001");
        serviceTypeSet.add("image002");


        NamingServiceMulti multi = new NamingServiceMulti(namingServiceAddress, serviceTypeSet);

        for (int i = 0; i < 1000; i++) {
            try {
                System.out.println(multi.getServiceTypes());
            } catch (ServiceException e) {
                e.printStackTrace();
            }

            Utility.sleep(1000);
        }
    }

    public static void f() {
        WebHotdeployConfig config = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);

        System.out.println(config.getHotGameList());
    }

    static class CacheManager extends MemDiskCacheManager {

        public CacheManager(MemDiskCacheConfig config) {
            super(config);
        }
    }
}
