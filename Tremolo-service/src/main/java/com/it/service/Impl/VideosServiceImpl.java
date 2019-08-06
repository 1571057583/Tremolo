package com.it.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.it.Dao.*;
import com.it.pojo.*;
import com.it.pojo.DTO.CommentsDTO;
import com.it.pojo.DTO.VideosDTO;
import com.it.pojo.VO.CommentsVO;
import com.it.pojo.VO.PageResultVO;
import com.it.service.VideosService;
import com.it.utils.TimeAgoUtils;
import org.apache.catalina.User;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VideosServiceImpl implements VideosService{


    @Autowired
    private VideosDao videosDao;

    @Autowired
    private SearchRecordsDao searchRecordsDao;

    @Autowired
    private UsersLikeVideosDao usersLikeVideosDao;

    @Autowired
    private UsersDao UsersDao;

    @Autowired
    private CommentsDao commentsDao;

    @Autowired
    private Sid sid;

    @Override
    public String save(Videos videos) {
        String id=sid.nextShort();
        System.out.println(id);
        videos.setId(id);
        Videos save = videosDao.save(videos);
        return id;
    }

    @Override
    public int updateVideoPath(String videoId, String videoPath) {
        int i = videosDao.updateVideoPath(videoId, videoPath);
        return i;
    }

    @Override
    public PageResultVO findAllVideosBydesc(Integer page, Integer pageSize,String videoDesc, Integer isSaveRecord) {

        if (videoDesc!=null&&isSaveRecord==1){
            SearchRecords searchRecords = new SearchRecords();
            String id = sid.nextShort();
            System.out.println("videoId是"+id);
            searchRecords.setContent(videoDesc);
            searchRecords.setId(id);
            searchRecordsDao.save(searchRecords);
        }
        PageHelper.startPage(page,pageSize);
        List<VideosDTO> videos = videosDao.findAllVideosBydesc(videoDesc);
        PageInfo<VideosDTO> pageInfo = new PageInfo<>(videos);
        PageResultVO pageResultVO = new PageResultVO();
        pageResultVO.setPage(page);
        pageResultVO.setTotal(pageInfo.getPages());
        pageResultVO.setRows(videos);
        pageResultVO.setRecords(pageInfo.getTotal());

        return pageResultVO;
    }

    @Override
    public PageResultVO findAllVideos(Integer page, Integer pageSize) {

        PageHelper.startPage(page,pageSize);
        List<VideosDTO> videos = videosDao.findAllVideos();
        PageInfo<VideosDTO> pageInfo = new PageInfo<>(videos);
        PageResultVO pageResultVO = new PageResultVO();
        pageResultVO.setPage(page);
        pageResultVO.setTotal(pageInfo.getPages());
        pageResultVO.setRows(videos);
        pageResultVO.setRecords(pageInfo.getTotal());

        return pageResultVO;
    }
    @Override
    public PageResultVO findAllByUserId(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page,pageSize);
        List<VideosDTO> videos = videosDao.findAllByUserId(userId);
        PageInfo<VideosDTO> pageInfo = new PageInfo<>(videos);
        PageResultVO pageResultVO = new PageResultVO();
        pageResultVO.setPage(page);
        pageResultVO.setTotal(pageInfo.getPages());
        pageResultVO.setRows(videos);
        pageResultVO.setRecords(pageInfo.getTotal());

        return pageResultVO;
    }

    @Override
    public PageResultVO queryMyFollowVideos(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page,pageSize);
        List<VideosDTO> videos = videosDao.queryMyFollowVideos(userId);
        PageInfo<VideosDTO> pageInfo = new PageInfo<>(videos);
        PageResultVO pageResultVO = new PageResultVO();
        pageResultVO.setPage(page);
        pageResultVO.setTotal(pageInfo.getPages());
        pageResultVO.setRows(videos);
        pageResultVO.setRecords(pageInfo.getTotal());

        return pageResultVO;

    }

    @Override
    public PageResultVO queryMyLikeVideos(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page,pageSize);
        List<VideosDTO> videos = videosDao.queryMyLikeVideos(userId);
        PageInfo<VideosDTO> pageInfo = new PageInfo<>(videos);
        PageResultVO pageResultVO = new PageResultVO();
        pageResultVO.setPage(page);
        pageResultVO.setTotal(pageInfo.getPages());
        pageResultVO.setRows(videos);
        pageResultVO.setRecords(pageInfo.getTotal());

        return pageResultVO;

    }

    @Override
    public List<String> hot() {
        List<String> hot = videosDao.hot();
        return hot;
    }

    @Override
    public PageResultVO queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
        return null;
    }

    @Override
    public PageResultVO queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
        return null;
    }

    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
        UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
        String id = sid.nextShort();
        usersLikeVideos.setId(id);
        usersLikeVideos.setUserId(userId);
        usersLikeVideos.setVideoId(videoId);
        System.out.println(usersLikeVideos);
        usersLikeVideosDao.save(usersLikeVideos);

        videosDao.addVideoLikeCount(videoId);
        UsersDao.addReceiveLikeCount(userId);
    }


    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
        videosDao.usersUnLikeVideos(userId,videoId);
        videosDao.reduceVideoLikeCount(videoId);
        UsersDao.reduceReceiveLikeCount(userId);
    }

    @Override
    public void saveComment(Comments comment) {
            String id = sid.nextShort();
             comment.setId(id);
            commentsDao.save(comment);

    }



    @Override
    public PageResultVO getAllComments(String videoId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<CommentsDTO> comments = commentsDao.queryComments(videoId);
        List<CommentsVO> commentsVOList = new ArrayList<>();
        for(CommentsDTO c:comments){
            CommentsVO commentsVO = new CommentsVO();
            String timeAgoStr = TimeAgoUtils.format(c.getCreateTime());
            System.out.println(c);

            BeanUtils.copyProperties(c,commentsVO);
            commentsVO.setTimeAgoUtils(timeAgoStr);
            System.out.println(commentsVO);
            commentsVOList.add(commentsVO);
        }
        PageInfo<CommentsVO> pageInfo = new PageInfo<>(commentsVOList);
        PageResultVO pageResultVO = new PageResultVO();
        pageResultVO.setPage(page);
        pageResultVO.setTotal(pageInfo.getPages());
        pageResultVO.setRows(commentsVOList);
        pageResultVO.setRecords(pageInfo.getTotal());

        return pageResultVO;
    }
}


