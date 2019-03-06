///:SysUserServiceImpl.java
package com.erhui.official.service.impl;

import com.erhui.official.dao.LoginLogDAO;
import com.erhui.official.dao.SysUserDAO;
import com.erhui.official.domain.LoginLogDTO;
import com.erhui.official.domain.SysUserDTO;
import com.erhui.official.service.SysUserService;
import com.erhui.official.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author icechen1219
 * @date 2019/02/04
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDAO sysUserDAO;
    @Autowired
    private LoginLogDAO loginLogDAO;

    @Override
    public SysUserDTO loginCheck(String username, String password, String ipAddress) {
        SysUserDTO userDTO = new SysUserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        SysUserDTO loginUser = sysUserDAO.selectByUsernameAndPassword(userDTO);
        if (loginUser != null) {
            // TODO: 更新登录日志
            LoginLogDTO logDTO = new LoginLogDTO();
            logDTO.setUserId(loginUser.getUserId());
            logDTO.setLoginIp(ipAddress);
            loginLogDAO.insertSelective(logDTO);
        }
        return loginUser;
    }

    @Override
    public SysUserDTO findUniqueUser(String username) {
        SysUserDTO user = sysUserDAO.findByUsername(username);
        return user;
    }

    @Override
    public boolean register(SysUserDTO userDTO) {
        // 将表单的明文密码通过md5加密后再存入数据库
        String md5 = SecurityUtil.buildMd5(userDTO.getPassword());
        userDTO.setPassword(md5);
        int rows = sysUserDAO.insertSelective(userDTO);

        return rows == 1;
    }
}
///:SysUserServiceImpl.java
