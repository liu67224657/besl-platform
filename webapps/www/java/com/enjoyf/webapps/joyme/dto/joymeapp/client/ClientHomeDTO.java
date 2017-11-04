package com.enjoyf.webapps.joyme.dto.joymeapp.client;

import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/2/10
 * Description:
 */
public class ClientHomeDTO {
    private ClientModuleDTO headlist;
    private NewsModuleDTO newslist;
    private ClientModuleDTO module1;
    private ClientModuleDTO module2;
    private ClientModuleDTO module3;
    private List<Map<String, OtherInfoModuleDateDTO>> otherinfo;

    public ClientModuleDTO getHeadlist() {
        return headlist;
    }

    public void setHeadlist(ClientModuleDTO headlist) {
        this.headlist = headlist;
    }

    public NewsModuleDTO getNewslist() {
        return newslist;
    }

    public void setNewslist(NewsModuleDTO newslist) {
        this.newslist = newslist;
    }

    public ClientModuleDTO getModule1() {
        return module1;
    }

    public void setModule1(ClientModuleDTO module1) {
        this.module1 = module1;
    }

    public ClientModuleDTO getModule2() {
        return module2;
    }

    public void setModule2(ClientModuleDTO module2) {
        this.module2 = module2;
    }

    public ClientModuleDTO getModule3() {
        return module3;
    }

    public void setModule3(ClientModuleDTO module3) {
        this.module3 = module3;
    }

    public List<Map<String, OtherInfoModuleDateDTO>> getOtherinfo() {
        return otherinfo;
    }

    public void setOtherinfo(List<Map<String, OtherInfoModuleDateDTO>> otherinfo) {
        this.otherinfo = otherinfo;
    }
}
