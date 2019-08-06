package com.it.service.Impl;

import com.it.Dao.BgmDao;
import com.it.pojo.Bgm;
import com.it.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BgmServiceImpl implements BgmService {
    @Autowired
    private BgmDao bgmDao;
    @Override
    public List<Bgm> queryBgm() {
        List<Bgm> bgmList = bgmDao.findAll();
        return bgmList;
    }

    @Override
    public Bgm queryById(String bgmId) {
        Bgm bgm = bgmDao.findOne(bgmId);
        return bgm;
    }
}
