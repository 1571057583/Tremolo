package com.it.pojo.VO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
public class PublisherVideo {
    private UsersVO usersVO;

    private boolean userLikeVideo;


}
