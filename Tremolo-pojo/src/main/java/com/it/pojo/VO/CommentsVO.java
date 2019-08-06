package com.it.pojo.VO;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 课程评论表
 */
@Data
public class CommentsVO {
     private String id;

     private String fatherCommentId;

     private String toUserId;

     private String videoId;//视频id

     private String fromUserId;//留言者id

     private String comment;//评论内容

     private Timestamp createTime;//评论时间

     private String nickname;

     private String faceImage;

     private String totNickname;

     private String timeAgoUtils;



}
