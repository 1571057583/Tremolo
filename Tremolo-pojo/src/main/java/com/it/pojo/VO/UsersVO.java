package com.it.pojo.VO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
public class UsersVO {

    private String id;

    private String userToken;

    private boolean isFollow;

    private String username;//用户名

    @JsonIgnore
    private String password;//密码

    private String faceImage;//头像

    private String nickname;//昵称

    private int fansCounts;//粉丝数量

    private int followCounts;//关注总人数

    private int receiveLikeCounts;//赞美收藏数量


}
