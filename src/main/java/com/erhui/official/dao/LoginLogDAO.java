package com.erhui.official.dao;

import com.erhui.official.domain.LoginLogDTO;
import org.springframework.stereotype.Repository;

/**
 * LoginLogDAO继承基类
 */
@Repository
public interface LoginLogDAO extends MyBatisBaseDao<LoginLogDTO, Integer> {
}