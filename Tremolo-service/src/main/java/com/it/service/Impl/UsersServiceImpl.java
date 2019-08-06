package com.it.service.Impl;

import com.it.Dao.UserReportDao;
import com.it.Dao.UsersDao;
import com.it.Dao.UsersFansDao;
import com.it.Dao.VideosDao;
import com.it.pojo.Users;
import com.it.pojo.UsersFans;
import com.it.pojo.UsersLikeVideos;
import com.it.pojo.UsersReport;
import com.it.service.UsersService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service

public class UsersServiceImpl implements UsersService {


    @Autowired
    private UsersDao usersDao;

    @Autowired
    private Sid sid;

    @Autowired
    private VideosDao videosDao;

    @Autowired
    private UsersFansDao usersFansDao;

    @Autowired
    private UserReportDao userReportDao;

    @Override
    public Users findByUsernameAndPassword(String username, String password) {
        Users users = usersDao.findByUsernameAndPassword(username, password);
        return users;
    }

    @Override
    public int updateById(String userId, String faceImage) {
        int num = usersDao.updateById(userId, faceImage);
        return num;
    }

    @Override
    public Users findById(String userId) {
        Users user = usersDao.findOne(userId);
        return user;
    }

    @Override
    public Users save(Users users) {
        users.setId(sid.nextShort());
        Users save = usersDao.save(users);
        return save;
    }

    @Override
    public List<Users> findByUsername(String username) {
        List<Users> byUsername = usersDao.findByUsername(username);
        return byUsername;
    }

    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersDao.findOne(userId);
        return users;
    }

    @Override
    public boolean isUserLikeVideo(String loginUserId, String videoId) {
        String video = videosDao.isUserLikeVideo(loginUserId, videoId);
        System.out.println(video);
        if(video!=null){
            return true;
        }
        return false;
    }

    @Override
    public void fansLikeUsers(String fanId, String userId) {
        String id = sid.nextShort();
        UsersFans usersFans= new UsersFans();
        usersFans.setId(id);
        usersFans.setFanId(fanId);
        usersFans.setUserId(userId);
        usersFansDao.save(usersFans);

        usersDao.addFansCount(userId);
        usersDao.addFollersCount(fanId);
    }

    @Override
    public void fansUnLikeUsers(String fanId, String userId) {
        usersDao.reduceFansCount(userId);
        usersDao.reduceFollersCount(fanId);
        usersFansDao.fansUnLikeUsers(fanId,userId);
    }

    @Override
    public Boolean findById(String fanId, String userId) {
        List<UsersFans> fans = usersFansDao.findById(fanId, userId);
        if(fans.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public void reportUser(UsersReport usersReport) {
        String id = sid.nextShort();
        usersReport.setId(id);
        userReportDao.save(usersReport);
    }
}
