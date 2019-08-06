package com.it.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users")
@ApiModel(value = "用户对象")
public class Users {

    @Id
    @ApiModelProperty(hidden = true)
    private String id;

    @ApiModelProperty(value = "用户名",name = "username",required = true)
    private String username;//用户名

    @ApiModelProperty(value = "密码",name = "password",required = true)
    private String password;//密码

    @ApiModelProperty(hidden = true)
    private String faceImage;//头像

    private String nickname;//昵称

    @ApiModelProperty(hidden = true)
    private int fansCounts;//粉丝数量

    @ApiModelProperty(hidden = true)
    private int followCounts;//关注总人数

    @ApiModelProperty(hidden = true)
    private int receiveLikeCounts;//赞美收藏数量





}
