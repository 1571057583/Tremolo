package com.it.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.it.utils.RedisOperator;


@RestController
public class BaseController {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION = "user-redis-session";

    // 文件保存的命名空间
    public static final String FILE_SPACE = "D:/Tremolo-video-dev";

    // ffmpeg所在目录
    public static final String FFMPEG_EXE = "D:\\ffmpeg\\ffmpeg\\bin\\ffmpeg.exe";

    // 每页分页的记录数
    public static final Integer PAGE_SIZE = 5;



}
