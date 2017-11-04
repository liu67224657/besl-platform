package com.enjoyf.webapps.joyme.dto.joymeapp.client;

import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/2/10
 * Description:
 */
public class OtherInfoModuleDTO {

    private OtherInfoModuleDateDTO qqun;
    private OtherInfoModuleDateDTO tieba;
    private OtherInfoModuleDateDTO luntan;

    public OtherInfoModuleDateDTO getQqun() {
        return qqun;
    }

    public void setQqun(OtherInfoModuleDateDTO qqun) {
        this.qqun = qqun;
    }

    public OtherInfoModuleDateDTO getTieba() {
        return tieba;
    }

    public void setTieba(OtherInfoModuleDateDTO tieba) {
        this.tieba = tieba;
    }

    public OtherInfoModuleDateDTO getLuntan() {
        return luntan;
    }

    public void setLuntan(OtherInfoModuleDateDTO luntan) {
        this.luntan = luntan;
    }
}
