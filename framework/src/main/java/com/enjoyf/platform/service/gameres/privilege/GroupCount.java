package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-16
 * Time: 下午5:58
 * To change this template use File | Settings | File Templates.
 */
public class GroupCount implements Serializable{
    private long groupCountId;
    private long groupId;
    private int profileNum;
    private int newProfileNum;
    private int noteNum;
    private int newNoteNum;
    private int visitNum;
    private int newVisitNum;
    private int extInt1;
    private int extInt2;

    public long getGroupCountId() {
        return groupCountId;
    }

    public void setGroupCountId(long groupCountId) {
        this.groupCountId = groupCountId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getProfileNum() {
        return profileNum;
    }

    public void setProfileNum(int profileNum) {
        this.profileNum = profileNum;
    }

    public int getNewProfileNum() {
        return newProfileNum;
    }

    public void setNewProfileNum(int newProfileNum) {
        this.newProfileNum = newProfileNum;
    }

    public int getNoteNum() {
        return noteNum;
    }

    public void setNoteNum(int noteNum) {
        this.noteNum = noteNum;
    }

    public int getNewNoteNum() {
        return newNoteNum;
    }

    public void setNewNoteNum(int newNoteNum) {
        this.newNoteNum = newNoteNum;
    }

    public int getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(int visitNum) {
        this.visitNum = visitNum;
    }

    public int getNewVisitNum() {
        return newVisitNum;
    }

    public void setNewVisitNum(int newVisitNum) {
        this.newVisitNum = newVisitNum;
    }

    public int getExtInt1() {
        return extInt1;
    }

    public void setExtInt1(int extInt1) {
        this.extInt1 = extInt1;
    }

    public int getExtInt2() {
        return extInt2;
    }

    public void setExtInt2(int extInt2) {
        this.extInt2 = extInt2;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
