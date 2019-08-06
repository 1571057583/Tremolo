package com.it.controller;

import com.it.pojo.Bgm;
import com.it.pojo.Users;
import com.it.pojo.VO.UsersVO;
import com.it.service.BgmService;
import com.it.service.UsersService;
import com.it.utils.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;


@RestController
@Api(value = "音乐的接口",tags = "音乐的Controller")
@RequestMapping("bgm")
public class BgmController extends BaseController{

    @Autowired
    private BgmService bgmService;

    @ApiModelProperty(value = "查询音乐",notes = "查询音乐接口")
    @RequestMapping(value = "/queryBgm",method = RequestMethod.POST)
    public ResultVO qureyBgm() throws Exception{
        List<Bgm> bgmList = bgmService.queryBgm();

        return  ResultVO.ok(bgmList);
    }
}
