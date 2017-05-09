package com.zzw.framelibray.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.zzw.framelibray.skin.attr.SkinAttr;
import com.zzw.framelibray.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzw on 2017/5/8.
 * 皮肤属性解析的支持类
 */

public class SkinAttrSupport {
    private static final String TAG = "SkinAttrSupport";


    /**
     * 获取SkinAttr的属性
     *
     * @param context
     * @param attrs
     * @return
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        //background src textColor
        List<SkinAttr> skinAttrs = new ArrayList<>();

        int attrLength = attrs.getAttributeCount();
        for (int i = 0; i < attrLength; i++) {
            //获取名称和值  background textColor等
            String attrName = attrs.getAttributeName(i);
//            String attrValue =attrs.getAttributeValue(i) ;
//            Log.e(TAG, "attrName-->" + attrName + "   attrValue-->" + attrValue);

            //只获取重要的自己需要的属性
            SkinType skinType = getSkinType(attrName);
            if (skinType != null) {
                //传资源名称(目前只有attrValue是一个@int类型)和skinType
                String resName = getResName(context, attrs.getAttributeValue(i));
                if (TextUtils.isEmpty(resName)) {
                    continue;
                }
                SkinAttr skinAttr = new SkinAttr(resName, skinType);
                skinAttrs.add(skinAttr);
            }
        }

        return skinAttrs;
    }

    /**
     * 获取资源名称  img_src
     *
     * @param context
     * @param attrValue attrValue是一个地址 @1212121这样的  后面是资源id
     * @return 资源名称  比如:R.mipmap.img_src  --> 得到的是img_src
     */
    private static String getResName(Context context, String attrValue) {
        if (attrValue.startsWith("@")) {
            attrValue = attrValue.substring(1);
            int resId = Integer.parseInt(attrValue);
            return context.getResources().getResourceEntryName(resId);
        }

        return null;
    }

    /**
     * 通过名称获取SkinType
     *
     * @param attrName
     * @return
     */
    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            if (skinType.getResName().equals(attrName)) {
                return skinType;
            }
        }
        return null;
    }
}
