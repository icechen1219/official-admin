///:SecurityUtil.java
package com.erhui.official.util;

import org.springframework.util.DigestUtils;

/**
 * @author icechen1219
 * @date 2019/02/09
 */
public class SecurityUtil {
    /**
     * 给md5加盐的混淆字符串
     */
    private static final String SALT = "&%5123***&&%%$$#@";

    /**
     * 字符串MD5加密
     * @param str
     * @return
     */
    public static String buildMd5(String str) {
        String base = str + "/" + SALT;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

}
///:SecurityUtil.java
