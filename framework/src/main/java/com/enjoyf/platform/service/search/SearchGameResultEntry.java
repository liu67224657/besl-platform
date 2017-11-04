package com.enjoyf.platform.service.search;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SearchGameResultEntry implements Serializable{
    private String gtId;
    private String gtName;
    private String gtimg;
    private String gtcontent;
    private String gturl;

    private String gtgametype;
    private String gameplatform;
    private String operationcomp;
    private String productioncomp;
    private String gameperson;
    private String gametime;
    private String operationstatus;
    private String distributorcomp;

    public String getGtId() {
        return gtId;
    }

    public void setGtId(String gtId) {
        this.gtId = gtId;
    }

    public String getGtName() {
        return gtName;
    }

    public void setGtName(String gtName) {
        this.gtName = gtName;
    }

    public String getGtimg() {
        return gtimg;
    }

    public void setGtimg(String gtimg) {
        this.gtimg = gtimg;
    }

    public String getGtcontent() {
        return gtcontent;
    }

    public void setGtcontent(String gtcontent) {
        this.gtcontent = gtcontent;
    }

    public String getGturl() {
        return gturl;
    }

    public void setGturl(String gturl) {
        this.gturl = gturl;
    }

    public String getGameplatform() {
        return gameplatform;
    }

    public void setGameplatform(String gameplatform) {
        this.gameplatform = gameplatform;
    }

    public String getOperationcomp() {
        return operationcomp;
    }

    public void setOperationcomp(String operationcomp) {
        this.operationcomp = operationcomp;
    }

    public String getProductioncomp() {
        return productioncomp;
    }

    public void setProductioncomp(String productioncomp) {
        this.productioncomp = productioncomp;
    }

    public String getGameperson() {
        return gameperson;
    }

    public void setGameperson(String gameperson) {
        this.gameperson = gameperson;
    }

    public String getGametime() {
        return gametime;
    }

    public void setGametime(String gametime) {
        this.gametime = gametime;
    }

    public String getOperationstatus() {
        return operationstatus;
    }

    public void setOperationstatus(String operationstatus) {
        this.operationstatus = operationstatus;
    }

    public String getDistributorcomp() {
        return distributorcomp;
    }

    public void setDistributorcomp(String distributorcomp) {
        this.distributorcomp = distributorcomp;
    }

    public String getGtgametype() {
        return gtgametype;
    }

    public void setGtgametype(String gtgametype) {
        this.gtgametype = gtgametype;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
