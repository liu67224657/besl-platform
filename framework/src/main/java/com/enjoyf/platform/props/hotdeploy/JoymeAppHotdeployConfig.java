package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.joymeapp.JoymeAppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-2-21
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppHotdeployConfig extends HotdeployConfig {
	private static Logger logger = LoggerFactory.getLogger(ContextHotdeployConfig.class);

	private static final String KEY_CLIENT_APP_KEY = "joymeapp.client.appkey";
	private static final String KEY_JOYMEAPP_NEWSCLIENT_ISOPEN = "joymeapp.newsclient.isopen";
	private static final String KEY_JOYMEAPP_NEWSCLIENT_GAMENAME = "joymeapp.newsclient.gamename";
	private static final String KEY_JOYMEAPP_NEWSCLIENT_TEXT = "joymeapp.newsclient.text";


	private static final String JOYMEAPP_ANIME_DOWNLOAD = "joymeapp.anime.download";
	private static final String JOYMEAPP_ANIME_RULE_VERSION = "joymeapp.anime.rule.version";
	private static final String JOYMEAPP_ANIME_RULE_TEXT = "joymeapp.anime.rule.text";
	private static final String JOYMEAPP_ANIME_OPEN_AD = "joymeapp.anime.open.ad";

	private JoymeAppCache cache;

	public JoymeAppHotdeployConfig() {
		super(EnvConfig.get().getJoymeAppHotdeployConfig());
	}

	@Override
	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("JoymeAppHotdeployConfig Props init start......");
		}

		reload();

		if (logger.isDebugEnabled()) {
			logger.debug("JoymeAppHotdeployConfig Props init end......");
		}

	}

	@Override
	public void reload() {
		super.reload();

		JoymeAppCache tempCache = new JoymeAppCache();

		String appKey = getString(KEY_CLIENT_APP_KEY);
		String isOpen = getString(KEY_JOYMEAPP_NEWSCLIENT_ISOPEN);
		String gameName = getString(KEY_JOYMEAPP_NEWSCLIENT_GAMENAME);
		String gameText = getString(KEY_JOYMEAPP_NEWSCLIENT_TEXT);

		String anime_download = getString(JOYMEAPP_ANIME_DOWNLOAD);
		String anime_rule_version = getString(JOYMEAPP_ANIME_RULE_VERSION);
		String anime_rule_text = getString(JOYMEAPP_ANIME_RULE_TEXT);
		String anime_open_ad = getString(JOYMEAPP_ANIME_OPEN_AD);


		Map<String, JoymeAppConfig> joymeAppConfigMap = new HashMap<String, JoymeAppConfig>();

		JoymeAppConfig joymeAppConfig = new JoymeAppConfig();
		joymeAppConfig.setAppKey(appKey);
		joymeAppConfig.setShakeIsOpen(isOpen);
		joymeAppConfig.setGameNames(gameName);
		joymeAppConfig.setGameText(gameText);

		joymeAppConfig.setAnimeDownLoad(anime_download);
		joymeAppConfig.setAnimeRuleVersion(anime_rule_version);
		joymeAppConfig.setAnimeRuleText(anime_rule_text);
		joymeAppConfig.setAnimeOpenAd(anime_open_ad);

		tempCache.setJoymeAppConfig(joymeAppConfig);

		this.cache = tempCache;
	}

	private class JoymeAppCache {
		private JoymeAppConfig joymeAppConfig = new JoymeAppConfig();

		public JoymeAppConfig getJoymeAppConfig() {
			return joymeAppConfig;
		}

		public void setJoymeAppConfig(JoymeAppConfig joymeAppConfig) {
			this.joymeAppConfig = joymeAppConfig;
		}
	}

	/**
	 * don't put anything
	 *
	 * @return
	 */
	public JoymeAppConfig getJoymeAppConfig() {
		return cache.getJoymeAppConfig();
	}

}
