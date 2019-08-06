package com.it.service;

import com.it.pojo.Bgm;

import java.util.List;

public interface BgmService {

    public List<Bgm> queryBgm();

    public Bgm queryById(String bgmId);
}
