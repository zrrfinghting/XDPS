package com.xdps.logic.util;

import org.springframework.util.DigestUtils;

/**
 * md5加密
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/29
 */
public class MD5Util {
    /**
     * 将密码+帐号通过MD5加密(不可逆)
     *
     * @param userId 用户名
     * @param password 密码
     * @return MD5密码
     * @throws Exception
     */
    public static String getMd5(String userId, String password) {
        return DigestUtils.md5DigestAsHex((userId + password).getBytes());
    }
}
