package com.it.Dao;

import com.it.pojo.Comments;
import com.it.pojo.DTO.CommentsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentsDao extends JpaRepository<Comments,String> {

    @Query(value = "select c.*,u.face_image as face_image,u.nickname,tu.nickname as toNickname from comments c left join users u on c.from_user_id = u.id left join users tu on c.to_user_id = tu.id where c.video_id = ?1 order by c.create_time desc",nativeQuery = true)
    List<CommentsDTO>queryComments(String vodeoId);
}
