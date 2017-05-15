package com.zzw.framelibray.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;



/**
 * Created by zzw on 2017/5/14.
 * Version: 1.0
 * Des:
 */
public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;


    // 持有外部数据库的引用
    private SQLiteDatabase mSqLiteDatabase;

    private DaoSupportFactory() {

        // 把数据库放到内存卡里面  判断是否有存储卡 6.0要动态申请权限
        File dbRoot = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "zzwstudy" + File.separator + "database");
        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }
        File dbFile = new File(dbRoot, "zzwstudy.db");

        // 打开或者创建一个数据库
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public static DaoSupportFactory getFactory() {
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactory == null) {
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        IDaoSupport<T> daoSupport =new DaoSupport<T>();//new DaoSupport();
        daoSupport.init(mSqLiteDatabase,clazz);
        return daoSupport;
    }
}
