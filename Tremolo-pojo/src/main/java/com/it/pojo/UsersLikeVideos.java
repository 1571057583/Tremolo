package com.it.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户喜欢的/赞过的视频
 */
@Data
@Entity
@Table(name = "users_like_videos")
public class UsersLikeVideos {
    @Id
    private String id;

    private String userId;//用户

    private String videoId;//视频
}
