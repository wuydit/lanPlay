package org.wyw.lanplay.service;

import org.wyw.lanplay.entity.UserRecordEntity;

public interface CommonService {

    /**
     * 根据账号信息生成两小时token
     * @param userRecordEntity 账号信息
     * @return token
     */
    String createToken(UserRecordEntity userRecordEntity);

    /**
     * 对密码进行加密
     * @param pwd 密码
     * @return 加密后的串
     */
    String encryptedPwd(String key, String pwd);

    UserRecordEntity verifyAdmin(String token);

    UserRecordEntity verifyUser(String token);

}
