package com.it.Dao;

import com.it.pojo.Users;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface UsersDao extends JpaRepository<Users,String> {


    @Query(value = "select * from users where username=?1",nativeQuery = true)
    List<Users> findByUsername(String username);

    @Query(value = "select * from users where username=?1 and  password=?2",nativeQuery = true)
    Users findByUsernameAndPassword(String username,String password);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set face_image =?2 where id=?1",nativeQuery = true)
    int  updateById(String userId,String faceImage);

    /**
     * @Description: 用户受喜欢数累加
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set receive_like_counts=receive_like_counts +1 where id=?1",nativeQuery = true)
     void addReceiveLikeCount(String userId);

    /**
     * @Description: 用户受喜欢数累减
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set receive_like_counts =receive_like_counts -1 where id=?1",nativeQuery = true)
     void reduceReceiveLikeCount(String userId);

    /**
     * @Description: 增加粉丝数
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set fans_counts =fans_counts +1 where id=?1",nativeQuery = true)
     void addFansCount(String userId);

    /**
     * @Description: 增加关注数
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set follow_counts =follow_counts +1 where id=?1",nativeQuery = true)
     void addFollersCount(String userId);

    /**
     * @Description: 减少粉丝数
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set fans_counts =fans_counts -1 where id=?1",nativeQuery = true)
     void reduceFansCount(String userId);

   /**
     * @Description: 减少关注数
     */
   @Transactional
   @Modifying(clearAutomatically = true)
   @Query(value = "update users set follow_counts =follow_counts -1 where id=?1",nativeQuery = true)
     void reduceFollersCount(String userId);

}
