package com.xingye.myandroid.bean;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xingye.myandroid.bean.Video;

import com.xingye.myandroid.bean.VideoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig videoDaoConfig;

    private final VideoDao videoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        videoDaoConfig = daoConfigMap.get(VideoDao.class).clone();
        videoDaoConfig.initIdentityScope(type);

        videoDao = new VideoDao(videoDaoConfig, this);

        registerDao(Video.class, videoDao);
    }
    
    public void clear() {
        videoDaoConfig.clearIdentityScope();
    }

    public VideoDao getVideoDao() {
        return videoDao;
    }

}
