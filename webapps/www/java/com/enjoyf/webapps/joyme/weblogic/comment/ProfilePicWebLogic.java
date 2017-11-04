package com.enjoyf.webapps.joyme.weblogic.comment;

import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.webapps.joyme.dto.comment.ProfilePicDTO;
import org.springframework.stereotype.Service;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/19
 * Description:
 */
@Service(value = "profilePicWebLogic")
public class ProfilePicWebLogic {

    public ProfilePicDTO buildProfilePicDTO(CommentBean commentBean) {
        ProfilePicDTO picDTO = new ProfilePicDTO();

        picDTO.setPicid(commentBean.getCommentId());
        picDTO.setPicurl(URLUtils.getJoymeDnUrl(commentBean.getPic()));

        return picDTO;
    }
}
