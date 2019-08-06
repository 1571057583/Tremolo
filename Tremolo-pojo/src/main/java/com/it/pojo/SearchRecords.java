package com.it.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 视频搜索的记录表
 */
@Data
@Entity
@Table(name = "search_records")
public class SearchRecords {

        @Id
        private String id;

        private String content;//搜索内容
}
