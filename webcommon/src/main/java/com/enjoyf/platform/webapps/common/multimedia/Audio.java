package com.enjoyf.platform.webapps.common.multimedia;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class Audio {
    private int song_id;
    private String song_name;
    private int artist_id;
    private String artist_name;
    private int album_id;
    private String album_name;
    private String album_logo;

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public String getSong_name() {
        try {
            return URLDecoder.decode(song_name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_name() {
        try {
            return URLDecoder.decode(artist_name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        try {
            return URLDecoder.decode(album_name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_logo() {
        return album_logo;
    }

    public void setAlbum_logo(String album_logo) {
        this.album_logo = album_logo;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
