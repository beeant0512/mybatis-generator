package com.beeant.common.utils;

import java.util.Random;
import java.util.UUID;

/**
 * 随机字符串
 * <p>
 * Created by Beeant on 2016/1/12.
 */
public class RandomString {

    public static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBER_CHAR = "0123456789";

    /**
     * 指定长度的UUID
     *
     * @param length UUID长度
     * @return String
     */
    public static String getUUID(int length) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").substring(0, length);
    }

    public static String getNonLineUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getMixString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_CHAR.charAt(random.nextInt(LETTER_CHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getLowerString(int length) {
        return getMixString(length).toLowerCase();
    }

    /**
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getUpperString(int length) {
        return getMixString(length).toUpperCase();
    }

    /**
     * 生成一个定长的纯0字符串
     *
     * @param length 字符串长度
     * @return 纯0字符串
     */
    public static String getZeroString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     *
     * @param num       数字
     * @param fixedLength 字符串长度
     * @return 定长的字符串
     */
    public static String toFixedLengthString(long num, int fixedLength) {
        StringBuilder sb = new StringBuilder();
        String strNum = String.valueOf(num);
        if (fixedLength - strNum.length() >= 0) {
            sb.append(getZeroString(fixedLength - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixedLength
                    + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 每次生成的len位数都不相同
     *
     * @param param 参数
     * @param len   长度
     * @return 定长的数字
     */
    public static int getNotSimple(int[] param, int len) {
        Random rand = new Random();
        for (int i = param.length; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = param[index];
            param[index] = param[i - 1];
            param[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < len; i++) {
            result = result * 10 + param[i];
        }
        return result;
    }

}
