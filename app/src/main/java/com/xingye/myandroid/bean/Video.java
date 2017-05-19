package com.xingye.myandroid.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by sunxiquan on 2017/5/19.
 */
@Entity
public class Video extends com.xingye.myandroid.adapter.Entity{
    @Id
    private Long id;
    @Property(nameInDb = "username")
    private String userName;
    @Property(nameInDb = "content")
    private String content;
    @Property(nameInDb = "videopath")
    private String videoPath;
    public String getVideoPath() {
        return this.videoPath;
    }
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 898866939)
    public Video(Long id, String userName, String content, String videoPath) {
        this.id = id;
        this.userName = userName;
        this.content = content;
        this.videoPath = videoPath;
    }
    @Generated(hash = 237528154)
    public Video() {
    }

}
