package com.it.Dao;

import com.it.pojo.DTO.VideosDTO;
import com.it.pojo.UsersLikeVideos;
import com.it.pojo.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VideosDao extends JpaRepository<Videos,String> {

    @Modifying
    @Transactional
    @Query(value = "update videos set video_path=?2 where id=?1",nativeQuery = true)
    int updateVideoPath(String videoId,String videoPath);


    @Query(value = "select v.*,u.face_image,u.nickname from videos v left join users u on v.user_id = u.id where 1=1 and v.`status`=1 group by v.create_time desc",nativeQuery = true)
    List<VideosDTO> findAllVideos();

    @Query(value = "select v.*,u.face_image,u.nickname from videos v left join users u on v.user_id = u.id where 1=1 and v.`status`=1 and v.video_desc like %?1% group by v.create_time desc",nativeQuery = true)
    List<VideosDTO> findAllVideosBydesc(String desc);

    @Query(value = "select v.*,u.face_image,u.nickname from videos v left join users u on v.user_id = u.id where 1=1 and v.`status`=1 and u.id =?1 group by v.create_time desc",nativeQuery = true)
    List<VideosDTO> findAllByUserId(String userId);

    @Query(value = "select content from search_records group by content order by count(content) desc",nativeQuery = true)
    List<String> hot();

    @Query(value = "select * from users_like_videos where user_id=?1 and video_id=?2",nativeQuery = true)
    String isUserLikeVideo(String loginUserId, String videoId);


    /**
     * @Description: 查询关注的视频
     */
    @Query(value = "select v.*,u.face_image as face_image,u.nickname as nickname from videos v left join users u on v.user_id = u.id where v.user_id in (select uf.user_id from users_fans uf where uf.fan_id = ?1) and v.status = 1 order by v.create_time desc",nativeQuery = true)
     List<VideosDTO> queryMyFollowVideos(String userId);

    /**
     * @Description: 查询点赞视频
     */
    @Query(value = "select v.*,u.face_image as face_image,u.nickname as nickname from videos v left join users u on v.user_id = u.id where v.id in (select ulv.video_id from users_like_videos ulv where ulv.user_id = ?1) and v.status = 1 order by v.create_time desc",nativeQuery = true)
    List<VideosDTO> queryMyLikeVideos(String userId);

    /**
     * @Description: 对视频喜欢的数量进行累加
     */
    @Modifying
    @Transactional
    @Query(value = "update videos set like_counts = like_counts+1 where id=?1",nativeQuery = true)
     void addVideoLikeCount(String videoId);

    /**
     * @Description: 对视频喜欢的数量进行累减
     */
    @Modifying
    @Transactional
    @Query(value = "update videos set like_counts = like_counts-1 where id=?1",nativeQuery = true)
     void reduceVideoLikeCount(String videoId);

    @Modifying
    @Transactional
    @Query(value = "delete from users_like_videos where user_id=?1 and video_id=?2",nativeQuery = true)
     void usersUnLikeVideos(String userId,String videoId);

}
