package com.it.service;

import com.it.pojo.Users;
import com.it.pojo.UsersFans;
import com.it.pojo.UsersReport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsersService {

    List<Users> findByUsername(String username);

    Users findByUsernameAndPassword(String username ,String password);

    Users save(Users users);

    int  updateById(String userId,String faceImage);

    Users findById(String userId);

    Users queryUserInfo(String userId);

    boolean isUserLikeVideo(String loginUserId,String videoId);

    void fansLikeUsers(String fanId,String userId);

    void fansUnLikeUsers(String fanId,String userId);

    Boolean findById(String fanId, String userId);

    void reportUser(UsersReport usersReport);
}
