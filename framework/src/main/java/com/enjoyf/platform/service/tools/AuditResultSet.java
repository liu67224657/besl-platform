package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-10-30
 * Time: 上午11:28
 * Desc:
 */
public class AuditResultSet {
    private Set<AuditResult> results = new LinkedHashSet<AuditResult>();


    public AuditResultSet() {
    }

    public AuditResultSet(Set<AuditResult> set) {
        this.results = set;
    }

    public AuditResultSet(Collection auditResult) {
        this.results.addAll(auditResult);
    }


    public Set<AuditResult> getResults() {
        return results;
    }

    public void setResults(Set<AuditResult> results) {
        this.results = results;
    }


    public void add(AuditResult video) {
        results.add(video);
    }

    public void add(Set<AuditResult> videos) {
        this.results.addAll(videos);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(results);
    }

    public static AuditResultSet parse(String jsonStr) {
        AuditResultSet returnValue = new AuditResultSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<AuditResult> auditResults = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<Set<AuditResult>>() {
                });

                returnValue.add(auditResults);
            } catch (IOException e) {
                GAlerter.lab("AuditResultSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


}
