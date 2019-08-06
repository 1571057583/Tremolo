package com.it.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name ="videos")
public class Videos {

    @Id
    private String id;

    private String userId;//发布者id

    private String audioId;//用户使用音频信息

    private String videoDesc;//视频描述

    private String videoPath;//视频存放路径

    private float videoSeconds;//视频秒数

    private int videoWidth;//视频宽度

    private int videoHeight;//视频高度

    private String coverPath;//视频封面图

    private int likeCounts;//喜欢/赞美数量

    private int status;//视频状态

    private Timestamp createTime;//创建时间
}
