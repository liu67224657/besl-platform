package com.enjoyf.webapps.joyme.dto.comment;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.comment.CommentExpandParam;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-12
 * Time: 下午7:45
 * To change this template use File | Settings | File Templates.
 */
public class ScoreEntity {

    private String uri;  //链接地址
    private String title; //标题
    private String pic; // 图片
    private String description;   //描述
    private Date post_date; //评论时间
    private long post_time;   //评论时间

    private int times_sum;   //评分总次数

    private double score_sum;    //总分数
    private double average_score;  //评分平均分数
    private String five_percent;   //5星百分数
    private String four_percent;   //4星百分数
    private String three_percent;   //3星百分数
    private String two_percent;   //2星百分数
    private String one_percent;   //1星百分数

    private CommentExpandParam menu_list;  //九宫格菜单

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPost_date() {
        return post_date;
    }

    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }

    public long getPost_time() {
        return post_time;
    }

    public void setPost_time(long post_time) {
        this.post_time = post_time;
    }

    public int getTimes_sum() {
        return times_sum;
    }

    public void setTimes_sum(int times_sum) {
        this.times_sum = times_sum;
    }

    public double getScore_sum() {
        return score_sum;
    }

    public void setScore_sum(double score_sum) {
        this.score_sum = score_sum;
    }

    public double getAverage_score() {
        return average_score;
    }

    public void setAverage_score(double average_score) {
        this.average_score = average_score;
    }

    public String getFive_percent() {
        return five_percent;
    }

    public void setFive_percent(String five_percent) {
        this.five_percent = five_percent;
    }

    public String getFour_percent() {
        return four_percent;
    }

    public void setFour_percent(String four_percent) {
        this.four_percent = four_percent;
    }

    public String getThree_percent() {
        return three_percent;
    }

    public void setThree_percent(String three_percent) {
        this.three_percent = three_percent;
    }

    public String getTwo_percent() {
        return two_percent;
    }

    public void setTwo_percent(String two_percent) {
        this.two_percent = two_percent;
    }

    public String getOne_percent() {
        return one_percent;
    }

    public void setOne_percent(String one_percent) {
        this.one_percent = one_percent;
    }

    public CommentExpandParam getMenu_list() {
        return menu_list;
    }

    public void setMenu_list(CommentExpandParam menu_list) {
        this.menu_list = menu_list;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.Md5("ms|91110.shtml" + 5));

        double num = 3.1415926;
        DecimalFormat df = new DecimalFormat("#.000");
        System.out.println(df.format(num));
    }
}
