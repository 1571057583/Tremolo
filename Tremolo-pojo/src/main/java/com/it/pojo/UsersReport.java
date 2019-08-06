package com.it.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * 举报用户表
 */
@Data
@Entity
@Table(name = "users_report")
public class UsersReport {

    @Id
    private String id;

    private String dealUserId;//被举报用户id

    private String dealVideoId;//禁播视频id

    private String title;//类型标题

    private String content;//内容

    private String userid;//举报人id

    private Timestamp createDate;//举报时间
}
