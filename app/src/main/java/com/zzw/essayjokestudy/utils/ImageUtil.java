package com.zzw.essayjokestudy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

/**
 * Created by zzw on 2017/6/15.
 * Version:
 * Des:
 */

public class ImageUtil {

    static {
//        System.loadLibrary("jpeg");
        System.loadLibrary("compressimg");
    }


    //建议:BitmapFactory.decodeFil可能会oom 先采用Options先压缩
    //一般后台规定尺寸  宽高  采用Options先压缩-->下面decodeFile方法
    //批量压缩上传 最好开线程池  2-3个
    //for循环  位图回收  jni里面已经做了处理
    //

    /**
     * @param bitmap      要压缩的图片
     * @param quality     压缩系数   越小压缩得越小图片越模糊
     * @param zipFileName 压缩的图片路径
     * @return 1成功  -1失败
     */
    public static native int compressBitmap(Bitmap bitmap, int quality, String zipFileName);


    /**
     * @param path
     * @param finalWidth 宽度
     * @return
     */
    public static Bitmap decodeFile(String path, int finalWidth) {

        //先获取宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        //不加载图片到内存  只拿宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int bitmapWidth = options.outWidth;
        int inSampleSize = 1;
        if (bitmapWidth > finalWidth) {
            inSampleSize = bitmapWidth / finalWidth;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
}
