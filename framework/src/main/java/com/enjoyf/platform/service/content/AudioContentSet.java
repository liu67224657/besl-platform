package com.enjoyf.platform.service.content;

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
 * User: zhaoxin
 * Date: 11-8-22
 * Time: 下午5:28
 * Desc: 音乐实体类
 */
public class AudioContentSet implements Serializable {
    //
    private Set<AudioContent> audios = new LinkedHashSet<AudioContent>();

    public AudioContentSet() {
    }

    public AudioContentSet(Set audios) {
        this.audios = audios;
    }

    public AudioContentSet(Collection audios) {
        this.audios.addAll(audios);
    }

    public AudioContentSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                audios = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<AudioContent>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("AudioContentSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<AudioContent> getAudios() {
        return audios;
    }

    public void add(AudioContent audio) {
        audios.add(audio);
    }

    public void add(Set<AudioContent> audios) {
        this.audios.addAll(audios);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(audios);
    }

    public static AudioContentSet parse(String jsonStr) {
        AudioContentSet returnValue = new AudioContentSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<AudioContent> audioContents = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<Set<AudioContent>>() {
                });

                returnValue.add(audioContents);
            } catch (IOException e) {
                GAlerter.lab("AudioContentSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
