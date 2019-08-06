package com.it.pojo.DTO;


import java.sql.Timestamp;

public interface VideosDTO {

     String getId();

     String getUserId();//发布者id

     String getAudioId();//用户使用音频信息

     String getVideoDesc();//视频描述

     String getVideoPath();//视频存放路径

     float getVideoSeconds();//视频秒数

     int getVideoWidth();//视频宽度

     int getVideoHeight();//视频高度

     String getCoverPath();//视频封面图

     int getLikeCounts();//喜欢/赞美数量

     int getStatus();//视频状态

     Timestamp getCreateTime();//创建时间

     String getFaceImage();//头像

     String getNickname();//用户名
}
