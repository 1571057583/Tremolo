package com.it.controller;

import com.it.pojo.Bgm;
import com.it.pojo.Comments;
import com.it.pojo.VO.PageResultVO;
import com.it.pojo.Videos;
import com.it.service.BgmService;
import com.it.service.VideosService;
import com.it.utils.FetchVideoCover;
import com.it.utils.MergeVideoMp3;
import com.it.utils.ResultVO;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
@Api(value = "视频上传的接口",tags = "视频上传的Controller")
@RequestMapping("video")
public class VideoController extends BaseController{


    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideosService videosService;

    @ApiModelProperty(value = "视频上传",notes = "视频接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="bgmId", value="背景音乐id", required=false,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoSeconds", value="背景音乐播放长度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoWidth", value="视频宽度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoHeight", value="视频高度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="desc", value="视频描述", required=false,
                    dataType="String", paramType="form")
    })
    @RequestMapping(value = "/upload",method = RequestMethod.POST,headers = "content-type=multipart/form-data")
    public ResultVO upload(String userId,
                           String bgmId, double videoSeconds,
                           int videoWidth, int videoHeight,
                           String desc,
                           @ApiParam(value="短视频", required=true)
                                   MultipartFile file) throws Exception{
        if(StringUtils.isBlank(userId)){
            return ResultVO.errorMsg("用户id不能为空");
        }
        System.out.println(userId+"视频userID");
        String uploadPathDB ="/"+userId+"/video";
        String uploadCoverPathDB ="/"+userId+"/video";
        FileOutputStream fileOutputStream=null;
        InputStream inputStream =null;
        String finalFacePath="";
        String finalCoverPath="";

        try {
            if(file !=null){
                String fileName = file.getOriginalFilename();
                String coverName =fileName.split("\\.")[0];
                if(StringUtils.isNoneBlank(fileName)){
                    finalFacePath = FILE_SPACE+uploadPathDB+"/"+fileName;
                    finalCoverPath=FILE_SPACE+uploadCoverPathDB+"/"+coverName+".jpg";
                    //图片数据库路径
                    uploadCoverPathDB+="/"+coverName+".jpg";
                    //数据库路径
                    uploadPathDB+=("/"+fileName);
                    File outfile = new File(finalFacePath);
                    if(outfile.getParentFile()!=null||!outfile.getParentFile().isDirectory()){
                        outfile.getParentFile().mkdirs();
                    }
                    fileOutputStream= new FileOutputStream(outfile);
                    inputStream=file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                    if(StringUtils.isNotBlank(bgmId)){
                        Bgm bgm = bgmService.queryById(bgmId);
                        String mp3InputPath = FILE_SPACE+bgm.getPath();         //mp3绝对路径
                        String videoInputPath =finalFacePath;                   //视频绝对路径
                        String videoName=UUID.randomUUID().toString()+".mp4";    //新生成MP4名
                        uploadPathDB ="/"+userId+"/video"+"/"+videoName;        //数据库存储mp4路径
                        String newInputPath =FILE_SPACE+uploadPathDB;           //mp4绝对路径
                        MergeVideoMp3 tool = new MergeVideoMp3(FFMPEG_EXE);
                        tool.convertor(videoInputPath,mp3InputPath,10,newInputPath);
                        FetchVideoCover fetchVideoCover = new FetchVideoCover(FFMPEG_EXE);
                        fetchVideoCover.getCover(newInputPath,finalCoverPath);

                        Date date = new Date();
                        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24小时制
                        String formatDate = dFormat.format(date);
                        Videos videos = new Videos();
                        videos.setUserId(userId);
                        videos.setVideoDesc(desc);
                        videos.setVideoPath(uploadPathDB);
                        videos.setCoverPath(uploadCoverPathDB);
                        videos.setAudioId(bgmId);
                        videos.setVideoSeconds(Float.valueOf(String.valueOf(videoSeconds)));
                        videos.setStatus(1);
                        videos.setVideoHeight(videoHeight);
                        videos.setVideoWidth(videoWidth);
                        videos.setCreateTime(Timestamp.valueOf(formatDate));
                        String videoId = videosService.save(videos);
                        return ResultVO.ok(videoId);
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
        return  ResultVO.ok();
    }

    @ApiModelProperty(value = "视频封面上传",notes = "视频封面接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoId", value="视频id", required=true,
                    dataType="String", paramType="form"),

    })
    @RequestMapping(value = "/uploadCover",method = RequestMethod.POST,headers = "content-type=multipart/form-data")
    public ResultVO uploadCover(String userId,
                           String videoId,
                           @ApiParam(value="短视频封面", required=true)
                                   MultipartFile file) throws Exception{
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(videoId)){
            return ResultVO.errorMsg("用户id和视频id不能为空");
        }
        String uploadPathDB ="/"+userId+"/video";
        FileOutputStream fileOutputStream=null;
        InputStream inputStream =null;
        String finalVideoPaht="";

        try {
            if(file !=null){
                String fileName = file.getOriginalFilename();

                if(StringUtils.isNoneBlank(fileName)){
                    finalVideoPaht = FILE_SPACE+uploadPathDB+"/"+fileName;


                    //视频数据库路径
                    uploadPathDB+=("/"+fileName);

                    File outfile = new File(finalVideoPaht);
                    if(outfile.getParentFile()!=null||!outfile.getParentFile().isDirectory()){
                        outfile.getParentFile().mkdirs();
                    }
                    fileOutputStream= new FileOutputStream(outfile);
                    inputStream=file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                    videosService.updateVideoPath(videoId,uploadPathDB);

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
        return  ResultVO.ok();
    }

    @ResponseBody
    @ApiModelProperty(value = "视频分页显示",notes = "视频分页显示接口")
    @RequestMapping(value = "query",method = RequestMethod.POST)
    public ResultVO queryVideos(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                @RequestParam(value = "pageSize",defaultValue = "2")Integer pageSize
                                ){

        PageResultVO pageResultVO = videosService.findAllVideos(page, pageSize);

        return  ResultVO.ok(pageResultVO);

    }

    @ResponseBody
    @ApiModelProperty(value = "视频查询分页显示",notes = "视频查询分页显示接口")
    @RequestMapping(value = "queryByDesc",method = RequestMethod.POST)
    public ResultVO queryByDesc(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                @RequestParam(value = "videoDesc") String videoDesc,
                                @RequestParam(value = "isSaveRecord") Integer isSaveRecord
    ){
        if (videoDesc==null){
            queryVideos(page,pageSize);
        }
        PageResultVO pageResultVO = videosService.findAllVideosBydesc(page,pageSize,videoDesc,isSaveRecord);
        return  ResultVO.ok(pageResultVO);

    }

    @ResponseBody
    @ApiModelProperty(value = "自己发布视频分页显示",notes = "自己发布视频分页显示接口")
    @RequestMapping(value = "queryByUserId",method = RequestMethod.POST)
    public ResultVO queryByUserId(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                @RequestParam(value = "userId") String userId
    ){
        if(userId==null){
            return ResultVO.errorMsg("用户id不能为空");
        }
        PageResultVO pageResultVO = videosService.findAllByUserId(page,pageSize,userId);
        return  ResultVO.ok(pageResultVO);

    }
    @ResponseBody
    @ApiModelProperty(value = "查询我关注的视频分页显示",notes = "查询我关注的视频分页显示接口")
    @RequestMapping(value = "queryMyFollowVideos",method = RequestMethod.POST)
    public ResultVO queryMyFollowVideos(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                        @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                        @RequestParam(value = "userId") String userId
    ){
        if(userId==null){
            return ResultVO.errorMsg("用户id不能为空");
        }
        PageResultVO pageResultVO = videosService.queryMyFollowVideos(page,pageSize,userId);
        return  ResultVO.ok(pageResultVO);

    }
    @ResponseBody
    @ApiModelProperty(value = "查询我喜欢的视频分页显示",notes = "查询我喜欢的视频分页显示接口")
    @RequestMapping(value = "queryMyLikeVideos",method = RequestMethod.POST)
    public ResultVO queryMyLikeVideos(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                @RequestParam(value = "userId") String userId
    ){
        if(userId==null){
            return ResultVO.errorMsg("用户id不能为空");
        }
        PageResultVO pageResultVO = videosService.queryMyLikeVideos(page,pageSize,userId);
        return  ResultVO.ok(pageResultVO);

    }

    @ResponseBody
    @ApiModelProperty(value = "热词",notes = "热词接口")
    @RequestMapping(value = "hot",method = RequestMethod.POST)
    public ResultVO hot(){
        List<String> hot = videosService.hot();
        return  ResultVO.ok(hot);

    }

    @ResponseBody
    @ApiModelProperty(value = "喜欢视频",notes = "点赞接口")
    @RequestMapping(value = "userLikeVideo",method = RequestMethod.POST)
    public ResultVO userLikeVideo(@RequestParam(value = "userId")String userId,
                                  @RequestParam(value = "videoId")String videoId,
                                  @RequestParam(value = "videoCreaterId")String videoCreaterId){
            videosService.userLikeVideo(userId,videoId,videoCreaterId);
            return ResultVO.ok();
    }

    @ResponseBody
    @ApiModelProperty(value = "不喜欢视频",notes = "取消点赞接口")
    @RequestMapping(value = "userUnLikeVideo",method = RequestMethod.POST)
    public ResultVO userUnLikeVideo(@RequestParam(value = "userId")String userId,
                                    @RequestParam(value = "videoId")String videoId,
                                    @RequestParam(value = "videoCreaterId")String videoCreaterId){
        videosService.userUnLikeVideo(userId,videoId,videoCreaterId);
        return ResultVO.ok();
    }

    @ResponseBody
    @ApiModelProperty(value = "不喜欢视频",notes = "取消点赞接口")
    @RequestMapping(value = "saveComment",method = RequestMethod.POST)
    public ResultVO saveComment(@RequestBody Comments comments){
        Date date = new Date();
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24小时制
        String formatDate = dFormat.format(date);
        comments.setComment(comments.getComment());
        comments.setVideoId(comments.getVideoId());
        comments.setToUserId(comments.getToUserId());
        comments.setCreateTime(Timestamp.valueOf(formatDate));
        comments.setFatherCommentId(comments.getFatherCommentId());
        comments.setFromUserId(comments.getFromUserId());
        videosService.saveComment(comments);
        return ResultVO.ok();
    }

    @ResponseBody
    @ApiModelProperty(value = "查询留言分页",notes = "查询留言分页接口")
    @RequestMapping(value = "getAllComments",method = RequestMethod.POST)
    public ResultVO getAllComments(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                   @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                   @RequestParam(value = "videoId") String videoId){


        PageResultVO Comments = videosService.getAllComments(videoId, page, pageSize);
        System.out.println(Comments);
        return ResultVO.ok(Comments);
    }


}
