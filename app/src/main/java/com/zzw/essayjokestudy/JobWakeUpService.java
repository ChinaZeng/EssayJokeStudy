package com.zzw.essayjokestudy;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IntDef;

import java.util.List;

/**
 * Created by zzw on 2017/5/10.
 * 唤醒进程  5.0以上才有
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {

    private static final int JOB_WAKE_UP_ID = 3;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //开启轮询 --->触发onStartJob
        JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(JOB_WAKE_UP_ID, new ComponentName(this, JobWakeUpService.class));
        jobInfoBuilder.setPeriodic(2000);//两秒轮询

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfoBuilder.build());
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        //开启定时任务,定时轮询,看MessageService有没有被杀死
        //如果被杀死了--> 启动   -->轮询onStartJob

        //判断服务有没有在运行
        boolean messageServiceAlive = serviceAlive(this, MessageService.class.getName());
        if (!messageServiceAlive) {
            startService(new Intent(this, MessageService.class));
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    private boolean serviceAlive(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
