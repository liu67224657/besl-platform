package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 下午2:26
 * Description: 投票记录 <--> json
 */
public class VoteRecordSet implements Serializable {
    private Set<VoteRecord> records = new LinkedHashSet<VoteRecord>();

    public VoteRecordSet() {
    }

    public VoteRecordSet(Set<VoteRecord> set) {
        records = set;
    }

    public VoteRecordSet(Collection records) {
        this.records.addAll(records);
    }

    public VoteRecordSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                records = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<VoteRecord>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("VoteRecordSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<VoteRecord> getRecords() {
        return records;
    }

    public void add(VoteRecord record) {
        records.add(record);
    }

    public void add(Set<VoteRecord> records) {
        this.records.addAll(records);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(records);
    }

    public static VoteRecordSet parse(String jsonStr) {
        VoteRecordSet returnValue = new VoteRecordSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<VoteRecord> VoteRecords = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<VoteRecord>>() {
                });

                returnValue.add(VoteRecords);
            } catch (IOException e) {
                GAlerter.lab("VoteRecordSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return records.hashCode();
    }
}
