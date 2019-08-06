package com.it.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 背景音乐
 */
@Data
@Entity
@Table(name = "bgm")
public class Bgm {

    @Id
    private String id;

    private String author;//歌曲

    private String name;//歌曲名称

    private String path;//播放地址
}
