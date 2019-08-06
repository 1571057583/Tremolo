package com.it.service;

import com.it.pojo.Comments;
import com.it.pojo.VO.PageResultVO;
import com.it.pojo.Videos;

import java.util.List;


public interface VideosService {

    /**
     *保存视频
     */
    String save(Videos videos);

    /**
     * 修改视频存放路径
     */
    int updateVideoPath(String videoId,String videoPath);

    /**
     * 首页查询全部视频(分页)
     */
    PageResultVO findAllVideos(Integer page, Integer pageSize);

    /**
     * 根据搜索条件查询视频
     */
    PageResultVO findAllVideosBydesc(Integer page, Integer pageSize,String videoDesc,Integer isSaveRecord);

    /**
     * 根据搜索条件查询视频
     */
    PageResultVO findAllByUserId(Integer page, Integer pageSize,String userId);

    /**
     * 查询关注的视频
     */
    PageResultVO queryMyFollowVideos(Integer page, Integer pageSize,String userId);

    /**
     * 查询点赞视频
     */
    PageResultVO queryMyLikeVideos(Integer page, Integer pageSize,String userId);

    /**
     * @Description: 获取热搜词列表
     */
    List<String> hot();

    /**
     * @Description: 查询我喜欢的视频列表
     */
    public PageResultVO queryMyLikeVideos(String userId, Integer page, Integer pageSize);

    /**
     * @Description: 查询我关注的人的视频列表
     */
    public PageResultVO queryMyFollowVideos(String userId, Integer page, Integer pageSize);

    /**
     * @Description: 用户喜欢/点赞视频
     */
    public void userLikeVideo(String userId, String videoId, String videoCreaterId);

    /**
     * @Description: 用户不喜欢/取消点赞视频
     */
    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId);

    /**
     * @Description: 用户留言
     */
    public void saveComment(Comments comment);

    /**
     * @Description: 留言分页
     */
    public PageResultVO getAllComments(String videoId, Integer page, Integer pageSize);
}
