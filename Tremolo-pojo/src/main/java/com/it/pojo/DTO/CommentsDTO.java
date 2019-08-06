package com.it.pojo.DTO;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 课程评论表
 */
public interface CommentsDTO {
     String getId();

     String getFatherCommentId();

     String getToUserId();

     String getVideoId();//视频id

     String getFromUserId();//留言者id

     String getComment();//评论内容

     Timestamp getCreateTime();//评论时间

     String getFaceImage();

     String getNickname();

     String getTotNickname();

}
