package com.xingye.myandroid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xingye.myandroid.bean.DaoMaster;
import com.xingye.myandroid.bean.DaoSession;
import com.xingye.myandroid.bean.Video;
import com.xingye.myandroid.bean.VideoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by sunxiquan on 2017/5/19.
 */

public class DbManager {
    private final static String dbName = "video_db";
    private static DbManager manager;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DbManager(Context context){
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context,dbName,null);

    }

    public static synchronized DbManager getInstance(Context context){
        if(manager == null){
            manager = new DbManager(context);
        }
        return manager;
    }

    private SQLiteDatabase getReadbleDatabase(){
        if(openHelper == null){
            openHelper = new DaoMaster.DevOpenHelper(context,dbName,null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }


    private SQLiteDatabase getWriteableDatabase(){
        if(openHelper == null){
            openHelper = new DaoMaster.DevOpenHelper(context,dbName,null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入一条数据
     * @param video
     */
    public void insertVideo(Video video){
        DaoMaster daoMaster = new DaoMaster(getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();
        videoDao.insert(video);
    }

    /**
     * 插入多条数据的集合
     * @param list
     */
    public void insertVideoList(List<Video> list){
        if(list == null || list.isEmpty()){
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();
        videoDao.insertInTx(list);
    }

    public void deleteVideo(Video video){
        DaoMaster daoMaster = new DaoMaster(getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();
        videoDao.delete(video);
    }

    public void updateVideo(Video video){
        DaoMaster daoMaster = new DaoMaster(getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();
        videoDao.update(video);
    }

    public List<Video> queryVideoList(){
        DaoMaster daoMaster = new DaoMaster(getReadbleDatabase());
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();
        QueryBuilder<Video> qb = videoDao.queryBuilder();
         return qb.list();
    }

    public List<Video> queryVideoList(String userName){
        DaoMaster daoMaster = new DaoMaster(getReadbleDatabase());
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();
        QueryBuilder<Video> qb = videoDao.queryBuilder();
        qb.where(VideoDao.Properties.UserName.ge(userName)).orderAsc(VideoDao.Properties.UserName);
        return qb.list();
    }
}
