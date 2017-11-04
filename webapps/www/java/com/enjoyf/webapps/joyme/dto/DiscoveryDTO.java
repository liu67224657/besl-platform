package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.content.DiscoveryWallContent;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-8
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
public class DiscoveryDTO implements Serializable{
    //版面版式
    private String layout;
    //版面版块
    private String block;
    private List<DiscoveryWallContent> wallContents;
}
