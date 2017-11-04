package com.enjoyf.platform.service.joymeapp;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-12
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppConfig {

    private String appKey;
    private String shakeIsOpen;//手游画报摇一摇开关
    private String gameNames;//摇一摇等待页面游戏名称
    private String gameText;


	private String animeDownLoad;//是否可以离线
	private String animeRuleVersion;//加载规则版本
	private String animeRuleText;//加载规则文本
	private String animeOpenAd;//是否可以打开开屏广告




    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getShakeIsOpen() {
        return shakeIsOpen;
    }

    public void setShakeIsOpen(String shakeIsOpen) {
        this.shakeIsOpen = shakeIsOpen;
    }

    public String getGameNames() {
        return gameNames;
    }

    public void setGameNames(String gameNames) {
        this.gameNames = gameNames;
    }

    public String getGameText() {
        return gameText;
    }

    public void setGameText(String gameText) {
        this.gameText = gameText;
    }

	public String getAnimeDownLoad() {
		return animeDownLoad;
	}

	public void setAnimeDownLoad(String animeDownLoad) {
		this.animeDownLoad = animeDownLoad;
	}

	public String getAnimeRuleVersion() {
		return animeRuleVersion;
	}

	public void setAnimeRuleVersion(String animeRuleVersion) {
		this.animeRuleVersion = animeRuleVersion;
	}

	public String getAnimeRuleText() {
		return animeRuleText;
	}

	public void setAnimeRuleText(String animeRuleText) {
		this.animeRuleText = animeRuleText;
	}

	public String getAnimeOpenAd() {
		return animeOpenAd;
	}

	public void setAnimeOpenAd(String animeOpenAd) {
		this.animeOpenAd = animeOpenAd;
	}
}
