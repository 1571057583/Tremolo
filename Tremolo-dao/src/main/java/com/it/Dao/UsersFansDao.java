package com.it.Dao;

import com.it.pojo.UsersFans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsersFansDao extends JpaRepository<UsersFans,String> {

    @Modifying
    @Transactional
    @Query(value = "delete from users_fans where fan_id=?1 and user_id=?2",nativeQuery = true)
    void fansUnLikeUsers(String fanId,String userId);

    @Query(value = "select * from users_fans where fan_id=?1 and user_id=?2",nativeQuery = true)
    List<UsersFans> findById(String fanId, String userId);


}
