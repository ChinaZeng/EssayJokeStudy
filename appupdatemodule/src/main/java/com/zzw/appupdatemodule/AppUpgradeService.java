package com.zzw.appupdatemodule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;

/**
 * Created by zzw on 2017/6/14.
 * Version:
 * Des:
 */

public class AppUpgradeService extends Service {

    private static final String DOWN_URL = "downUrl";
    private static final String DOWN_FILE_NAME = "downFileName";

    private NotificationManager mNotificationManager = null;
    private Notification mNotification = null;
    //    private PendingIntent mPendingIntent = null;
    private RemoteViews mRemoteView;

    private String mDownloadUrl;
    private File destFile = null;

    public static final int mNotificationId = 111;
    private static final int DOWNLOAD_FAIL = -1;
    private static final int DOWNLOAD_SUCCESS = 0;

    public static void newInstance(Context context, String downloadUrl, String fileName) {
        Intent intent = new Intent(context, AppUpgradeService.class);
        Bundle bundle = new Bundle();
        bundle.putString(DOWN_URL, downloadUrl);
        bundle.putString(DOWN_FILE_NAME, fileName);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    private Handler mHandler;

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(
                android.content.Context.NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mRemoteView = new RemoteViews(this.getPackageName(), R.layout.layout_update_notification);

        mNotification.icon = R.drawable.ic_launcher_round;
//        mNotification.when = System.currentTimeMillis();
//        mNotification.tickerText = "准备下载...";

        mRemoteView.setTextViewText(R.id.tvName, "准备下载...");

        // 放置在"正在运行"栏目中
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        mNotification.contentView = mRemoteView;
        mRemoteView.setProgressBar(R.id.pbProgress, 100, 0, false);
        mRemoteView.setTextViewText(R.id.tvProgress, 0 + "%");

//        Intent completingIntent = new Intent();
//        completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        completingIntent.setClass(getApplicationContext(), AppUpgradeService.class);
//        // 创建Notifcation对象，设置图标，提示文字,策略
//        mPendingIntent = PendingIntent.getActivity(AppUpgradeService.this, R.string.app_name, completingIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationManager.cancel(mNotificationId);
        mNotificationManager.notify(mNotificationId, mNotification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        mDownloadUrl = intent.getStringExtra(DOWN_URL);
        String fileName = intent.getStringExtra(DOWN_FILE_NAME);

        if (TextUtils.isEmpty(fileName)) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        destFile = new File(DownloadUtils.getCacheFile(this) + File.separator + fileName);
        initHandler();
        initNotification();
        new AppUpgradeThread().start();
        return super.onStartCommand(intent, flags, startId);
    }


    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DOWNLOAD_SUCCESS:
                        Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_LONG).show();
                        installAPK(destFile);
//                        stopSelf();
                        break;
                    case DOWNLOAD_FAIL:
                        Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
                        stopSelf();
                        break;
                    default:
                        break;
                }
            }

        };
    }

    /**
     * 用于监听文件下载
     */
    private DownloadUtils.DownloadListener downloadListener = new DownloadUtils.DownloadListener() {
        @Override
        public void downloading(int progress) {
//            mRemoteView.setImageViewResource(R.id.ivIcon, R.drawable.ic_launcher_round);
            mRemoteView.setTextViewText(R.id.tvName, "更新中...");
            mRemoteView.setProgressBar(R.id.pbProgress, 100, progress, false);
            mRemoteView.setTextViewText(R.id.tvProgress, progress + "%");
            mNotification.contentView = mRemoteView;
            mNotificationManager.notify(mNotificationId, mNotification);

            Log.e("zzz", "downloading  " + progress);
        }

        @Override
        public void downloadOk(File file) {
////                mNotification.contentView.setViewVisibility(R.id.app_upgrade_progressbar, View.GONE);
//            mNotification.defaults = Notification.DEFAULT_SOUND;
//            mNotification.contentIntent = mPendingIntent;
////                mNotification.contentView.setTextViewText(R.id.app_upgrade_title, "下载完成");
//            mNotificationManager.notify(mNotificationId, mNotification);
//            if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
//                Message msg = mHandler.obtainMessage();
//                msg.what = DOWNLOAD_SUCCESS;
//                mHandler.sendMessage(msg);
//            }
//            mNotificationManager.cancel(mNotificationId);

            mNotification.defaults = Notification.DEFAULT_SOUND;
            mRemoteView.setTextViewText(R.id.tvName, "下载完成");
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;

//            Intent completingIntent = new Intent();
//            completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            completingIntent.setClass(getApplicationContext(), AppUpgradeService.class);
//            // 创建Notifcation对象，设置图标，提示文字,策略
//            mPendingIntent = PendingIntent.getActivity(AppUpgradeService.this, R.string.app_name, completingIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);

            mNotificationManager.notify(mNotificationId, mNotification);
            if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
                Message msg = mHandler.obtainMessage();
                msg.what = DOWNLOAD_SUCCESS;
                mHandler.sendMessage(msg);
            }
            mNotificationManager.cancel(mNotificationId);
        }

        @Override
        public void downloadError(Exception e) {
            mNotification.contentView = null;
            Message msg = mHandler.obtainMessage();
            msg.what = DOWNLOAD_FAIL;
            mHandler.sendMessage(msg);
            mNotificationManager.cancel(mNotificationId);
        }
    };

    /**
     * 用于文件下载线程
     *
     * @author
     */
    private class AppUpgradeThread extends Thread {
        @Override
        public void run() {
            DownloadUtils.download(mDownloadUrl, destFile, downloadListener);
        }
    }


    /**
     * 下载完成后自动安装apk
     */
    public void installAPK(File apkFile) {
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            String uri = BuildConfig.APPLICATION_ID + ".fileProvider";
            Uri contentUri = FileProvider.getUriForFile(this, uri, apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);

        //安装完之后删除
        if (apkFile.exists())
            apkFile.delete();
    }


    /**
     * 判断文件是否完整
     *
     * @param apkFilePath
     * @return
     */
    public boolean checkApkFile(String apkFilePath) {
        boolean result = false;
        try {
            PackageManager pManager = getPackageManager();
            PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            if (pInfo == null) {
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationManager = null;
        mNotification = null;
//        mPendingIntent = null;
        mRemoteView = null;
        mDownloadUrl = null;
//        destFile = null;
    }
}
