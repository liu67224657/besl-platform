package com.enjoyf.platform.util.oauth.weibo4j;

import com.enjoyf.platform.props.SinaWeiboConfig;
import com.enjoyf.platform.util.oauth.weibo4j.model.*;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONException;
import java.util.List;

public class Trend {

	/*----------------------------话题接口----------------------------------------*/
	/**
	 * 获取某人的话题列表
	 * 
	 * @param uid
	 *            需要获取话题的用户的UID
	 * @return list of the userTrend
	 * @throws WeiboException
	 *             when Weibo service or network is unavailable
	 * @version weibo4j-V2 1.0.0
	 * @see <a href="http://open.weibo.com/wiki/2/trends">trends</a>
	 * @since JDK 1.5
	 */
	public List<UserTrend> getTrends(String uid) throws WeiboException {
		return UserTrend
				.constructTrendList(Weibo.client.get(
						SinaWeiboConfig.get().getBaseUrl() + "trends.json",
						new PostParameter[] { new PostParameter("uid", uid
								.toString()) }));
	}

	/**
	 * 获取某人的话题列表
	 * 
	 * @param uid
	 *            需要获取话题的用户的UID
	 * @param page
	 *            返回结果的页码，默认为1
	 * @return list of the userTrend
	 * @throws WeiboException
	 *             when Weibo service or network is unavailable
	 * @version weibo4j-V2 1.0.0
	 * @see <a href="http://open.weibo.com/wiki/2/trends">trends</a>
	 * @since JDK 1.5
	 */
	public List<UserTrend> getTrends(Integer uid, Paging page)
			throws WeiboException {
		return UserTrend
				.constructTrendList(Weibo.client.get(
						SinaWeiboConfig.get().getBaseUrl() + "trends.json",
						new PostParameter[] { new PostParameter("uid", uid
								.toString()) }, page));
	}

	/**
	 * 判断当前用户是否关注某话题
	 * 
	 * @param trend_name
	 *            话题关键字，必须做URLencode
	 * @return
	 * @throws WeiboException
	 *             when Weibo service or network is unavailable
	 * @version weibo4j-V2 1.0.0
	 * @throws JSONException
	 * @see <a
	 *      href="http://open.weibo.com/wiki/2/trends/is_follow">trends/is_follow</a>
	 * @since JDK 1.5
	 */
	public boolean isFollow(String trend_name) throws WeiboException {
		try {
			return Weibo.client
					.get(SinaWeiboConfig.get().getBaseUrl()
							+ "trends/is_follow.json",
							new PostParameter[] { new PostParameter(
									"trend_name", trend_name) }).asJSONObject()
					.getBoolean("is_follow");
		} catch (JSONException e) {
			throw new WeiboException(e);
		}
	}

	/**
	 * 返回最近一小时内的热门话题
	 * 
	 * @param base_app
	 *            是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0
	 * @return list of trends
	 * @throws WeiboException
	 *             when Weibo service or network is unavailable
	 * @version weibo4j-V2 1.0.0
	 * @throws JSONException
	 * @see <a
	 *      href="http://open.weibo.com/wiki/2/trends/hourly">trends/hourly</a>
	 * @since JDK 1.5
	 */
	public List<Trends> getTrendsHourly(Integer base_app) throws WeiboException {
		return Trends.constructTrendsList(Weibo.client.get(
				SinaWeiboConfig.get().getBaseUrl() + "trends/hourly.json",
				new PostParameter[] { new PostParameter("base_app", base_app
						.toString()) }));
	}

	/**
	 * 返回最近一天内的热门话题
	 * 
	 * @param base_app
	 *            是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0
	 * @return list of trends
	 * @throws WeiboException
	 *             when Weibo service or network is unavailable
	 * @version weibo4j-V2 1.0.0
	 * @throws JSONException
	 * @see <a href="http://open.weibo.com/wiki/2/trends/daily">trends/daily</a>
	 * @since JDK 1.5
	 */
	public List<Trends> getTrendsDaily(Integer base_app) throws WeiboException {
		return Trends.constructTrendsList(Weibo.client.get(
				SinaWeiboConfig.get().getBaseUrl() + "trends/daily.json",
				new PostParameter[] { new PostParameter("base_app", base_app
						.toString()) }));
	}

	/**
	 * 返回最近一周内的热门话题
	 * 
	 * @param base_app
	 *            是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0
	 * @return list of trends
	 * @throws WeiboException
	 *             when Weibo service or network is unavailable
	 * @version weibo4j-V2 1.0.0
	 * @throws JSONException
	 * @see <a
	 *      href="http://open.weibo.com/wiki/2/trends/weekly">trends/weekly</a>
	 * @since JDK 1.5
	 */
	public List<Trends> getTrendsWeekly(Integer base_app) throws WeiboException {
		return Trends.constructTrendsList(Weibo.client.get(
				SinaWeiboConfig.get().getBaseUrl() + "trends/weekly.json",
				new PostParameter[] { new PostParameter("base_app", base_app
						.toString()) }));
	}

	/**
	 * 关注某话题
	 * 
	 * @param trend_name
	 *            要关注的话题关键词。
	 * @return UserTrend
	 * @throws WeiboException
	 *             when Weibo service or network is unavailable
	 * @version weibo4j-V2 1.0.0
	 * @throws JSONException
	 * @see <a
	 *      href="http://open.weibo.com/wiki/2/trends/follow">trends/follow</a>
	 * @since JDK 1.5
	 */
	public UserTrend trendsFollow(String trend_name) throws WeiboException {
		return new UserTrend(Weibo.client.post(SinaWeiboConfig.get().getBaseUrl()
				+ "trends/follow.json",
				new PostParameter[] { new PostParameter("trend_name",
						trend_name) }));
	}

	/**
	 * 取消对某话题的关注
	 * 
	 * @param trend_id
	 *            要取消关注的话题ID
	 * @return boolean
	 * @throws WeiboException
	 *             when Weibo service or network is unavailable
	 * @version weibo4j-V2 1.0.0
	 * @throws JSONException
	 * @see <a
	 *      href="http://open.weibo.com/wiki/2/trends/destroy">trends/destroy</a>
	 * @since JDK 1.5
	 */
	public boolean trendsDestroy(Integer trend_id) throws WeiboException {
		try {
			return Weibo.client
					.delete(SinaWeiboConfig.get().getBaseUrl()
							+ "trends/destroy.json",
							new PostParameter[] { new PostParameter("trend_id",
									trend_id.toString()) }).asJSONObject()
					.getBoolean("result");
		} catch (JSONException e) {
			throw new WeiboException(e);
		}
	}

}
