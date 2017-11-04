package com.enjoyf.webapps.joyme.weblogic.blogwebsite;


import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.sql.ObjectField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p/>
 * Description:用户个人博客站点服务实现类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Service(value = "blogWebSiteWebLogic")
public class BlogWebSiteWebLogic {
    private Logger logger = LoggerFactory.getLogger(BlogWebSiteWebLogic.class);

    //网博客站点插入数据
    public ProfileBlog addBlogWebSite(ProfileBlog profileBlog) throws ServiceException {
        logger.info("addProfileBlog site." + profileBlog);
        profileBlog = ProfileServiceSngl.get().createProfileBlog(profileBlog);

        return profileBlog;
    }

    
    public Boolean updateBlogWebSiteProps(String uno, Map<ObjectField, Object> propsMap) throws ServiceException {
        return ProfileServiceSngl.get().updateProfileBlog(uno, propsMap);

    }

    //通过域名得到博客信息
    
    public ProfileBlog findBlogWebSiteByBlogDomain(String blogDomain) throws ServiceException {
        ProfileBlog profileBlog = ProfileServiceSngl.get().getProfileBlogByDomain(blogDomain);
        return profileBlog;
    }


    //通过uno得到博客信息
    
    public ProfileBlog findBlogWebSiteByUno(String uno) throws ServiceException {
        ProfileBlog profileBlog = ProfileServiceSngl.get().getProfileBlogByUno(uno);
        return profileBlog;
    }

    //通过博客名得到博客信息
    
    public ProfileBlog findBlogWebSiteByBlogname(String blogname) throws ServiceException {
        ProfileBlog profileBlog = ProfileServiceSngl.get().getProfileBlogByScreenName(blogname);
        return profileBlog;
    }
}
