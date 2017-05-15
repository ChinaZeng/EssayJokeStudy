package com.zzw.baselibray.fixBug;

import android.content.Context;
import android.util.Log;

import com.zzw.baselibray.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by zzw on 2017/5/5.
 * 热修复管理
 */

public class FixBugManager {

    private static final String TAG = "FixBugManager";

    private Context mContext;
    private File mDexDir;//应用可以访问的dex目录

    public FixBugManager(Context context) {
        this.mContext = context;
        //获取到应用可以访问的dex目录
        this.mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }


    /**
     * 设置新的dexElements到applicationClassLoader里面
     *
     * @param classLoader
     * @param dexElements
     */
    private void setElementsToClassLoader(ClassLoader classLoader, Object dexElements) throws NoSuchFieldException, IllegalAccessException {
        //1.先获取ClassLoader里面的pathList
        Field pathListFiled = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListFiled.setAccessible(true);
        Object pathList = pathListFiled.get(classLoader);

        //2.获取pathList里面的dexElements字段并设置新的值
        Field dexElementsField = pathList.getClass().getField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, dexElements);
    }

    /**
     * 从ClassLoader里面获取dexElements
     *
     * @param applicationClassLoader
     * @return
     */
    private Object getElementsByClassLoader(ClassLoader applicationClassLoader) throws NoSuchFieldException, IllegalAccessException {
        //1.先获取ClassLoader里面的pathList
        Field pathListFiled = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListFiled.setAccessible(true);
        Object pathList = pathListFiled.get(applicationClassLoader);

        //2.获取pathList里面的dexElements
        Field dexElementsField = pathList.getClass().getField("dexElements");
        dexElementsField.setAccessible(true);
        Object dexElements = dexElementsField.get(pathList);

        return dexElements;
    }


    /**
     * 合并两个dexElements数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    /**
     * 加载所有的修复包
     */
    public void loadFixDex() throws Exception {
        File[] files = mDexDir.listFiles();
        List<File> fixDexFiles = new ArrayList<>();
        for (File dexFile : files) {
            if (dexFile.getName().endsWith(".dex")) {
                fixDexFiles.add(dexFile);
            }
        }
        fixDexFiles(fixDexFiles);
    }

    /**
     * 修复dex包
     *
     * @param fixDexPath dexDex修复路径
     */
    public void fixDex(String fixDexPath) throws Exception {

        File srcFile = new File(fixDexPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(fixDexPath);
        }
        File destFile = new File(mDexDir, srcFile.getName());
        if (destFile.exists()) {
            Log.d(TAG, "patch [" + fixDexPath + "] has be loaded.");
//            return;
            destFile.delete();
        }
        FileUtil.copyFile(srcFile, destFile);// copy to patch's directory
//        FileUtil.deleteFile(srcFile);//copy完成后删除
        //2.2 ClassLoader读取fixDex路径  为什么加入到集合?-->可能已启动就可能要修复
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        fixDexFiles(fixDexFiles);
    }

    /**
     * 修复dex 已经修复过的dex文件全部copy在mContext里面，application初始化的时候将这些多个dex文件一起修复
     *
     * @param fixDexFiles
     */
    private void fixDexFiles(List<File> fixDexFiles) throws Exception {
        //1.先获取applicationClassLoader的pathList字段的dexElements值
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Object applicationDexElements = getElementsByClassLoader(applicationClassLoader);

        //2.获取下载好的补丁的dexElements
        //2.1 移动到系统能够访问的dex目录下 --> ClassLoader
        File optimizedDirectory = new File(mDexDir, "oder");
        if (!optimizedDirectory.exists())
            optimizedDirectory.mkdirs();
        //修复
        for (File fixDexFile : fixDexFiles) {
            //dexPath 加载的dex路径
            //optimizedDirectory 解压路径
            //librarySearchPath  so文件位置
            //parent 父ClassLoader
            ClassLoader fixClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(), //dexPath 加载的dex路径
                    optimizedDirectory,// 解压文件
                    null,
                    applicationClassLoader);
            Object fixDexElements = getElementsByClassLoader(fixClassLoader);

            //3.把补丁的dexElements插到已经已经运行的dexElements前面
            //合并完成  fixDexElements插入dexElements之前
            applicationDexElements = combineArray(fixDexElements, applicationDexElements);
            //把合并的数组注入到原来的applicationClassLoader类中
            setElementsToClassLoader(applicationClassLoader, applicationDexElements);
        }
    }


}
