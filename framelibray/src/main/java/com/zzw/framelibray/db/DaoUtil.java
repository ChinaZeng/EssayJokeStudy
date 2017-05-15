package com.zzw.framelibray.db;

import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by zzw on 2017/5/14.
 * Version: 1.0
 * Des:
 */
public class DaoUtil {

    //  Filed莫名出现这两个东西  $change serialVersionUID
    private static String[] otherFieldName = new String[]{"serialVersionUID"};
    private static String[] otherFieldNameContainsKey = new String[]{"$"};


    private DaoUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String getColumnType(String type) {
        String value = null;
        if (type.contains("String")) {
            value = " text";
        } else if (type.contains("int")) {
            value = " integer";
        } else if (type.contains("boolean")) {
            value = " boolean";
        } else if (type.contains("float")) {
            value = " float";
        } else if (type.contains("double")) {
            value = " double";
        } else if (type.contains("char")) {
            value = " varchar";
        } else if (type.contains("long")) {
            value = " long";
        }
        return value;
    }


    public static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }


    /**
     * 排除不必要的字段
     *
     * @param name
     * @return true表示此字段正常  false表示该字段是包含的在排除的范围内
     */
    public static boolean checkFiled(String name) {
        boolean check = true;

        for (String otherFiled : otherFieldName) {
            if (otherFiled.equals(name)) {
                check = false;
                break;
            }
        }

        if (check) {
            for (String containsFiledKey : otherFieldNameContainsKey) {
                if (name.contains(containsFiledKey)) {
                    check = false;
                    break;
                }
            }

        }

        return check;

    }

}
