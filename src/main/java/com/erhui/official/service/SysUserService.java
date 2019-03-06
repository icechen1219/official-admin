///:SysUserService.java
package com.erhui.official.service;

import com.erhui.official.domain.SysUserDTO;

/**
 * @author icechen1219
 * @date 2019/02/04
 */
public interface SysUserService {
    /**
     * 登录服务
     *
     * @param username
     * @param password
     * @return
     */
    SysUserDTO loginCheck(String username, String password, String ipAddress);

    /**
     * 根据用户名查找唯一用户
     * @param username
     * @return
     */
    SysUserDTO findUniqueUser(String username);

    /**
     * 注册新用户
     * @param userDTO
     * @return
     */
    boolean register(SysUserDTO userDTO);
}
///:SysUserService.java
