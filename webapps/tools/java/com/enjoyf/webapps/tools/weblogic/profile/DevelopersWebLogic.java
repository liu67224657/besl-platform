package com.enjoyf.webapps.tools.weblogic.profile;

import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.webapps.tools.weblogic.dto.developers.ProfileDeveloperDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-23
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "developersWebLogic")
public class DevelopersWebLogic {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    public PageRows<ProfileDeveloperDTO> queryProfileDeveloperDTOs(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        PageRows<ProfileDeveloper> profileDeveloperPageRows = ProfileServiceSngl.get().queryProfileDeveloperByPage(queryExpress, pagination);
        if (profileDeveloperPageRows == null || CollectionUtil.isEmpty(profileDeveloperPageRows.getRows())) {
            return null;
        }
        List<ProfileDeveloperDTO> list = new LinkedList<ProfileDeveloperDTO>();
        for (ProfileDeveloper developer : profileDeveloperPageRows.getRows()) {
            ProfileBlog blog = ProfileServiceSngl.get().getProfileBlogByUno(developer.getUno());
            ProfileDetail detail = ProfileServiceSngl.get().getProfileDetail(developer.getUno());
            ProfileDeveloperDTO dto = new ProfileDeveloperDTO();
            dto.setBlog(blog);
            dto.setDetail(detail);
            dto.setDeveloper(developer);
            list.add(dto);
        }
        PageRows<ProfileDeveloperDTO> pageRows = new PageRows<ProfileDeveloperDTO>();
        pageRows.setPage(profileDeveloperPageRows.getPage());
        pageRows.setRows(list);
        return pageRows;
    }

    public ProfileDeveloperDTO getProfileDeveloperDTO(String uno) throws ServiceException {
        ProfileDeveloperDTO dto = new ProfileDeveloperDTO();
        ProfileDeveloper developer = ProfileServiceSngl.get().getProfileDeveloper(new QueryExpress().add(QueryCriterions.eq(ProfileDeveloperField.UNO, uno)));
        ProfileBlog blog = ProfileServiceSngl.get().getProfileBlogByUno(uno);
        ProfileDetail detail = ProfileServiceSngl.get().getProfileDetail(uno);
        dto.setBlog(blog);
        dto.setDetail(detail);
        dto.setDeveloper(developer);
        return dto;
    }
}
