/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:29
 * Description:
 */
public class ProfileSetting implements Serializable {
    //
    private String uno;

    //是否允许回复信息
    private AllowType allowContentReplyType = AllowType.A_ALLOW;
    //是否允许他人发私信
    private AllowType allowLetterType = AllowType.A_ALLOW;

    private String letterhead;   //私信标题，64字符
    //是否允许他人投稿
    private AllowType allowSubmit = AllowType.A_ALLOW;

    private ContentType allowsubmittype = new ContentType(); //允许投稿范围,和发文共用类型对象。
    private String allowtag; //投稿者可以选择的标签；\n希望直接使用汉字标签词汇来存储，方便查看使用
    private ActStatus hintmyfans = ActStatus.ACTED;
    private ActStatus hintmyfeedback = ActStatus.ACTED;
    private ActStatus hintmyletter = ActStatus.ACTED;
    private ActStatus hintmynotice = ActStatus.ACTED;
    private ActStatus hintatme = ActStatus.ACTED;

    private ProfileSettingAt atSet = new ProfileSettingAt();

    private AllowType allowExpSchool = AllowType.A_ALLOW;
    private AllowType allowExpComp = AllowType.A_ALLOW;

    private AllowType allowAgreeSocial = AllowType.A_ALLOW;//社交端--赞
    private AllowType allowReplySocial = AllowType.A_ALLOW;//社交端--评论
    private AllowType allowFocusSocial = AllowType.A_ALLOW;//社交端--关注
    private AllowType allowSoundSocial = AllowType.A_ALLOW;//提示音

    private Integer pagetitlenum = 20; //设置每页文章数目;

    public ProfileSetting() {
    }

    public ProfileSetting(String uno) {
        this.uno = uno;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public AllowType getAllowContentReplyType() {
        return allowContentReplyType;
    }

    public void setAllowContentReplyType(AllowType allowContentReplyType) {
        this.allowContentReplyType = allowContentReplyType;
    }

    public AllowType getAllowLetterType() {
        return allowLetterType;
    }

    public void setAllowLetterType(AllowType allowLetterType) {
        this.allowLetterType = allowLetterType;
    }

    public String getLetterhead() {
        return letterhead;
    }

    public void setLetterhead(String letterhead) {
        this.letterhead = letterhead;
    }

    public AllowType getAllowSubmit() {
        return allowSubmit;
    }

    public void setAllowSubmit(AllowType allowSubmit) {
        this.allowSubmit = allowSubmit;
    }

    public ContentType getAllowsubmittype() {
        return allowsubmittype;
    }

    public void setAllowsubmittype(ContentType allowsubmittype) {
        this.allowsubmittype = allowsubmittype;
    }

    public String getAllowtag() {
        return allowtag;
    }

    public void setAllowtag(String allowtag) {
        this.allowtag = allowtag;
    }

    public ActStatus getHintmyfans() {
        return hintmyfans;
    }

    public void setHintmyfans(ActStatus hintmyfans) {
        this.hintmyfans = hintmyfans;
    }

    public ActStatus getHintmyfeedback() {
        return hintmyfeedback;
    }

    public void setHintmyfeedback(ActStatus hintmyfeedback) {
        this.hintmyfeedback = hintmyfeedback;
    }

    public ActStatus getHintmyletter() {
        return hintmyletter;
    }

    public void setHintmyletter(ActStatus hintmyletter) {
        this.hintmyletter = hintmyletter;
    }

    public ActStatus getHintmynotice() {
        return hintmynotice;
    }

    public void setHintmynotice(ActStatus hintmynotice) {
        this.hintmynotice = hintmynotice;
    }

    public ActStatus getHintatme() {
        return hintatme;
    }

    public void setHintatme(ActStatus hintatme) {
        this.hintatme = hintatme;
    }

    public Integer getPagetitlenum() {
        return pagetitlenum;
    }

    public void setPagetitlenum(Integer pagetitlenum) {
        this.pagetitlenum = pagetitlenum;
    }

    public ProfileSettingAt getAtSet() {
        return atSet;
    }

    public void setAtSet(ProfileSettingAt atSet) {
        this.atSet = atSet;
    }

    public AllowType getAllowExpSchool() {
        return allowExpSchool;
    }

    public void setAllowExpSchool(AllowType allowExpSchool) {
        this.allowExpSchool = allowExpSchool;
    }

    public AllowType getAllowExpComp() {
        return allowExpComp;
    }

    public void setAllowExpComp(AllowType allowExpComp) {
        this.allowExpComp = allowExpComp;
    }

    public AllowType getAllowAgreeSocial() {
        return allowAgreeSocial;
    }

    public void setAllowAgreeSocial(AllowType allowAgreeSocial) {
        this.allowAgreeSocial = allowAgreeSocial;
    }

    public AllowType getAllowReplySocial() {
        return allowReplySocial;
    }

    public void setAllowReplySocial(AllowType allowReplySocial) {
        this.allowReplySocial = allowReplySocial;
    }

    public AllowType getAllowFocusSocial() {
        return allowFocusSocial;
    }

    public void setAllowFocusSocial(AllowType allowFocusSocial) {
        this.allowFocusSocial = allowFocusSocial;
    }

    public AllowType getAllowSoundSocial() {
        return allowSoundSocial;
    }

    public void setAllowSoundSocial(AllowType allowSoundSocial) {
        this.allowSoundSocial = allowSoundSocial;
    }

    //
    @Override
    public int hashCode() {
        return uno.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
