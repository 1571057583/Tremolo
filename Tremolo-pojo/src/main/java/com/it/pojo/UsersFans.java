package com.it.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户粉丝关联关系表
 */
@Data
@Entity
@Table(name = "users_fans")
public class UsersFans {
    @Id
    private String id;

    private String userId;//用户;

    private String fanId;//粉丝
}
