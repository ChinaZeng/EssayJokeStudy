package com.zzw.essayjokestudy.update;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zzw on 2017/6/14.
 * Version:
 * Des:
 */

public class DownloadHelper {

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int DATA_BUFFER = 1024 * 1024;//1M
    private  boolean cancelFlag;

    private  int currentProgress;

    public interface DownloadListener {
        public void downloading(int progress);

        public void downloadOk(File file);

        public void downloadError(Exception e);
    }

    /**
     * @param urlStr
     * @param dest
     * @param downloadListener -1表示出错
     * @return
     * @throws Exception
     */
    public  int download(String urlStr, File dest, DownloadListener downloadListener) {

        if (cancelFlag)
            return -1;

        InputStream is = null;
        FileOutputStream fos = null;
        int count = 0;
        try {
            cancelFlag = false;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.connect();
            long length = conn.getContentLength();
            is = conn.getInputStream();
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            if (dest.exists())
                dest.delete();

            dest.createNewFile();
            fos = new FileOutputStream(dest);
            byte buf[] = new byte[DATA_BUFFER];
            do {
                int numread = is.read(buf);
                if (numread <= 0) {
                    downloadListener.downloadOk(dest);
                    break;
                }
                count += numread;
                fos.write(buf, 0, numread);
                int progress = (int) (((float) count / length) * 100);

                if (currentProgress != progress) {
                    currentProgress = progress;
                    downloadListener.downloading(progress);
                }


            } while (!cancelFlag); //取消就停止下载.
        } catch (Exception e) {
            e.printStackTrace();
            downloadListener.downloadError(e);
        } finally {
            try {
                if (is != null)
                    is.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public  void cancelDown() {
        cancelFlag = true;
    }


    /**
     * 返回缓存文件夹
     */
    public  File getCacheFile(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = null;
            file = context.getExternalCacheDir();//获取系统管理的sd卡缓存文件
            if (file == null) {//如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
                file = new File(getCacheFilePath(context));
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            return file;
        } else {
            return context.getCacheDir();
        }
    }

    /**
     * 获取自定义缓存文件地址
     *
     * @param context
     * @return
     */
    public  String getCacheFilePath(Context context) {
        String packageName = context.getPackageName();
        return "/mnt/sdcard/" + packageName;
    }

}
