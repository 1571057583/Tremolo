package com.it.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * 课程评论表
 */
@Data
@Entity
@Table(name ="comments")
public class Comments {
    @Id
    private String id;

    private String fatherCommentId;

    private String toUserId;

    private String videoId;//视频id

    private String fromUserId;//留言者id

    private String comment;//评论内容

    private Timestamp createTime;//评论时间
}
