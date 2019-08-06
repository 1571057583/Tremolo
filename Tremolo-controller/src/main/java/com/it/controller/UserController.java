package com.it.controller;

import com.it.pojo.Users;
import com.it.pojo.UsersReport;
import com.it.pojo.VO.PublisherVideo;
import com.it.pojo.VO.UsersVO;
import com.it.service.UsersService;
import com.it.utils.MD5Utils;
import com.it.utils.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@Api(value = "用户相关业务的接口",tags = "用户相关业务的Controller")
@RequestMapping("user")
public class UserController extends BaseController{

    @Autowired
    private UsersService usersService;

    @ApiModelProperty(value = "用户上传",notes = "用户上传接口")
    @ApiImplicitParam(value = "用户id",name = "userId",dataType = "String",paramType = "query")
    @RequestMapping(value = "/uploadFace",method = RequestMethod.POST)
    public ResultVO logout(String userId,
                           @RequestParam MultipartFile[] file) throws Exception{
                if(StringUtils.isBlank(userId)){
                    return ResultVO.errorMsg("用户id不能为空");
                }
                  System.out.println(userId+"上传userId");
                String uploadPathDB ="/"+userId+"/face";
                FileOutputStream fileOutputStream=null;
                InputStream inputStream =null;
                try {
                    if(file !=null||FILE_SPACE.length()>0){
                        String fileName = file[0].getOriginalFilename();
                        if(StringUtils.isNoneBlank(fileName)){
                            String finalFacePath = FILE_SPACE+uploadPathDB+"/"+fileName;
                            //数据库路径
                            uploadPathDB+=("/"+fileName);
                            File outfile = new File(finalFacePath);
                            if(outfile.getParentFile()!=null||!outfile.getParentFile().isDirectory()){
                                outfile.getParentFile().mkdirs();
                            }
                            fileOutputStream= new FileOutputStream(outfile);
                            inputStream=file[0].getInputStream();
                            IOUtils.copy(inputStream,fileOutputStream);
                            int num = usersService.updateById(userId, uploadPathDB);
                            if(num<=0){
                                return ResultVO.errorMsg("上传出错");
                            }
                        }
                    }else{
                        return ResultVO.errorMsg("上传出错");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return ResultVO.errorMsg("上传出错");
                }finally {
                    if(fileOutputStream!=null){
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }
                return  ResultVO.ok(uploadPathDB);
            }

    @ApiModelProperty(value = "用户信息查询",notes = "用户信息查询接口")
    @ApiImplicitParam(value = "用户id",name = "userId",dataType = "String",paramType = "query")
    @RequestMapping(value = "/query",method = RequestMethod.POST)
    public ResultVO qurey(String userId )throws Exception{
        if(StringUtils.isBlank(userId)){
            return ResultVO.errorMsg("用户id不能为空");
        }
        Users users = usersService.findById(userId);
        System.out.println(users+"123");
        UsersVO usersVO = new UsersVO();

        BeanUtils.copyProperties(users,usersVO);

        return  ResultVO.ok(usersVO);
    }

    @ApiModelProperty(value = "用户粉丝查询",notes = "用户粉丝查询接口")
    @ApiImplicitParam(value = "用户id",name = "userId",dataType = "String",paramType = "query")
    @RequestMapping(value = "/qureyByUser",method = RequestMethod.POST)
    public ResultVO qureyByUser(String userId,String fanId) throws Exception{
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(fanId)){
            return ResultVO.errorMsg("用户id,粉丝id不能为空");
        }
        Users users = usersService.findById(userId);
        System.out.println(users+"456");
        UsersVO usersVO = new UsersVO();
        usersVO.setFollow(usersService.findById(fanId,userId));
        BeanUtils.copyProperties(users,usersVO);

        return  ResultVO.ok(usersVO);
    }
    @ApiModelProperty(value = "视频详情页面显示",notes = "视频详情页面显示接口")
    @PostMapping("/queryPublisher")
    public ResultVO queryPublisher(@RequestParam(value = "loginUserId") String loginUserId,
                                   @RequestParam(value = "videoId")String videoId,
                                   @RequestParam(value = "publishUserId")String publishUserId)
                                    throws Exception {

        if (StringUtils.isBlank(publishUserId)) {
            return ResultVO.errorMsg("");
        }

        // 1. 查询视频发布者的信息
        Users userInfo = usersService.queryUserInfo(publishUserId);
        System.out.println(userInfo);
        UsersVO publisher = new UsersVO();
        BeanUtils.copyProperties(userInfo, publisher);

        // 2. 查询当前登录者和视频的点赞关系
        boolean userLikeVideo = usersService.isUserLikeVideo(loginUserId, videoId);

        PublisherVideo bean = new PublisherVideo();
        bean.setUsersVO(publisher);
        bean.setUserLikeVideo(userLikeVideo);
        return ResultVO.ok(bean);
    }

    @ApiModelProperty(value = "关注用户",notes = "关注用户接口")
    @PostMapping("/fansLikeUsers")
    public ResultVO fansLikeUsers(@RequestParam(value = "fanId") String fanId,
                                   @RequestParam(value = "userId")String userId){

        usersService.fansLikeUsers(fanId,userId);
        return ResultVO.ok("关注成功");
    }

    @ApiModelProperty(value = "取消关注用户",notes = "关注用户显示")
    @PostMapping("/fansUnLikeUsers")
    public ResultVO fansUnLikeUsers(@RequestParam(value = "fanId") String fanId,
                                  @RequestParam(value = "userId")String userId){

        usersService.fansUnLikeUsers(fanId,userId);
        return ResultVO.ok("取消关注成功");
    }

    @ApiModelProperty(value = "举报用户",notes = "举报用户显示")
    @PostMapping("/reportUser")
    public ResultVO reportUser(@RequestBody UsersReport usersReport){
        Date date = new Date();
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24小时制
        String formatDate = dFormat.format(date);
        usersReport.setDealUserId(usersReport.getDealUserId());
        usersReport.setDealVideoId(usersReport.getDealVideoId());
        usersReport.setContent(usersReport.getContent());
        usersReport.setCreateDate(Timestamp.valueOf(formatDate));
        usersReport.setTitle(usersReport.getTitle());
        usersReport.setUserid(usersReport.getUserid());
       usersService.reportUser(usersReport);
        return ResultVO.ok("举报用户成功");
    }



}
