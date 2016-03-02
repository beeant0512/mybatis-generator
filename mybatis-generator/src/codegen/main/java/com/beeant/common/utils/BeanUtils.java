package com.beeant.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Beeant on 2016/2/27.
 */
public class BeanUtils {
    public static boolean isNull(Object bean) {
        boolean result = true;
        if (bean == null) {
            return true;
        }
        Class<?> cls = bean.getClass();
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMethod(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[]{});
                if (fieldVal != null) {
                    if ("".equals(fieldVal)) {
                        result = true;
                    } else {
                        result = false;
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return result;
    }


    /**
     * 拼接某属性的 get方法
     *
     * @param fieldName
     * @return String
     */
    public static String parGetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        int startIndex = 0;
        if (fieldName.charAt(0) == '_'){
            startIndex = 1;
        }
        return "get".concat(fieldName.substring(startIndex, startIndex + 1).toUpperCase())
                .concat(fieldName.substring(startIndex + 1));
    }

    /**
     * 判断是否存在某属性的 get方法
     *
     * @param methods
     * @param fieldGetMethod
     * @return boolean
     */
    public static boolean checkGetMethod(Method[] methods, String fieldGetMethod) {
        for (Method method : methods) {
            if (fieldGetMethod.equals(method.getName())) {
                return true;
            }
        }
        return false;
    }
}
