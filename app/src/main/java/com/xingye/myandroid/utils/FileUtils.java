package com.xingye.myandroid.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by sunxiquan on 2017/3/21.
 */

public class FileUtils {
    /**
     * 获取文件名
     * @param pathandname
     * @return
     */
    public static String getFileName(String pathandname){

        int start=pathandname.lastIndexOf("/");
        if(start!=-1){
            return pathandname.substring(start+1);
        }else{
            return null;
        }

    }

    public static String getFileType(String path){
        int start = path.lastIndexOf(".");
        if(start!=-1){
            return path.substring(start);
        }else{
            return "";
        }
    }

    /**
     * 判断sd卡是否挂载
     *
     * @return
     */
    public static boolean hasSDcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**判断sd卡状态*/
    public static boolean existSDCard(){
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }
    /**获取sd卡剩余空间大小*/
    public static long getSDFreeSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize)/1024 /1024; //单位MB
    }

    /**获取sd卡总容量*/
    public static long getSDAllSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize)/1024/1024; //单位MB
    }

}
