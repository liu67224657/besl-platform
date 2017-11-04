/**
 * CopyRight 2012 joyme.com
 */
package com.enjoyf.webapps.joyme.webpage.controller.example;

import com.enjoyf.webapps.joyme.weblogic.example.ExampleWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 12-3-27
 * Time: 下午4:06
 * Desc: 示例类，展示controller 中各种调用的方法
 */
@Controller
@RequestMapping(value = "/example")
public class ExampleController extends BaseRestSpringController {

    private Logger logger = LoggerFactory.getLogger(BaseRestSpringController.class);

    @Resource(name = "exampleWebLogic")
    private ExampleWebLogic exampleWebLogic;

    /*
     * cache example
     * 1. ehcache.xml add <cache name="exampleCache" ...
     * 2. serv/webapps/www/xxx/  ehcache.disk.store.dir=/opt/servicedata/cache
     */
//    @Cacheable(value = "exampleCache", key = "#name")
    @RequestMapping(value = "/cache")
    public ModelAndView queryListByNameAsCache(@RequestParam(value = "name", required = false) String name) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        logger.info("request param name: " + name);

        //no cache
        List<String> cList = new ArrayList<String>();
        cList.add("aaa");
        cList.add("bbb");
        cList.add("ccc");

        mapMessage.put("name", cList);

        logger.info("query example list as no cache ...");

        return new ModelAndView("/views/jsp/example/examplelist", mapMessage);
    }


    /*
     *  query list example
     * */
    @RequestMapping(value = "/list")
    public ModelAndView queryList() {

        //返回页面的map对象
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("one", "value1");
        mapMessage.put("two", "value2");


        //根据逻辑调用不同的webLogic中的不同方法
        //exampleWebLogic.clickExample(0L);
        return new ModelAndView("/views/jsp/example/examplelist", mapMessage);
    }

    /*
     *
     * */
    @RequestMapping(value = "/get")
    public ModelAndView getExampleById(@RequestParam(value = "id", required = false) String id) {

        //根据逻辑调用不同的webLogic中的不同方法
        //exampleWebLogic.clickExample(0L);
        return null;
    }

    /*
     *
     * */
    @RequestMapping(value = "/modify")
    public ModelAndView ModifyExample() {
        return null;
    }


    /*
     *
     * */
    @RequestMapping(value = "/del")
    public ModelAndView delExampleById() {
        return null;
    }

    /*
     *  path param example
     *
     * */
    @RequestMapping(value = "/p/{key}/")
    public ModelAndView getExampleByPathId(@PathVariable("key") String key) throws Exception {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("key", key);//返回页面显示

        return new ModelAndView("/views/jsp/example/example", mapMessage);
    }


}
