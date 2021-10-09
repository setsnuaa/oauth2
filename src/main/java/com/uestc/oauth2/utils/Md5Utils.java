package com.uestc.oauth2.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.Objects;
import java.util.Random;

/**
 * @author jie.zhong
 * @version 1.0
 * @date 2021/9/22 17:00
 */
public class Md5Utils {

    /**
     * 1.sb为16位密码盐  是两个 99999999之间的整数 若随机出来长度不足用0补位
     * 2.使用明文密码+密码盐 生成的MD5密码密文 以下称位密文第5步
     * 3.cs是一个48位的计算后的密码
     * 4.生成最终密码
     *
     * <li>PS: 生成密码规则  加密+盐 生成MD5 --->>>>> 最终密码每次生成三位</li>
     * <li>第一位根据规则取密文  0 2 4 6 8 以此类似的偶数位</li>
     * <li>第二位根据取密码盐 0 1 2 3 以此内推按每次叠加的顺序取</li>
     * <li>第三位 根据密文取 1 3 5 7 9 是密文的奇数位</li>
     *
     * 将密码盐 按照规则添加到密文中
     *
     * @param password 等待加密密码
     * @return 密文
     */
    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        int maxLen = 16;
        if (len < maxLen) {
            for (int i = 0; i < maxLen - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        int maxCharLen = 48;
        int devValue = 3;
        for (int i = 0; i < maxCharLen; i += devValue) {
            assert password != null;
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * 验证
     *
     * 验证过程
     * 根据加密规则 去除源密文 和 密码盐
     * 将明文和密码盐 进行md5及密码得到md5加密
     * 使用源密码+密文的字符串去对比 过滤密码盐的源密码
     *
     * @param password 明文密码
     * @param md5      密文
     * @return 结果
     */
    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        int maxCharLen = 48;
        int devValue = 3;
        for (int i = 0; i < maxCharLen; i += devValue) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return Objects.equals(md5Hex(password + salt), new String(cs1));
    }

    private static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(generate("admin"));
    }
}
