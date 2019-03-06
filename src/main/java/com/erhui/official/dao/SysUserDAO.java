package com.erhui.official.dao;

import com.erhui.official.domain.SysUserDTO;
import org.springframework.stereotype.Repository;

/**
 * SysUserDAO继承基类
 */
@Repository
public interface SysUserDAO extends MyBatisBaseDao<SysUserDTO, Integer> {
    SysUserDTO selectByUsernameAndPassword(SysUserDTO userDTO);

    SysUserDTO findByUsername(String username);
}