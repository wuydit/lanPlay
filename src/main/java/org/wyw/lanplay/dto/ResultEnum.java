package org.wyw.lanplay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum ResultEnum implements Serializable {
    //成功
    SUCCESS(0, "success"),
    ERROR(1, "ERROR！"),

    //登录注册100前缀
    USER_STOP(100001, "账号已停用！"),
    USER_BAN(100002, "账号已封禁！"),
    USER_ACCOUNT_ERROR(100003, "账号或密码不正确！"),
    USER_NOT_FIND(100004, "用户未找到！"),
    USER_INVITATION_CODE_ERROR(100005, "注册码错误！"),

    UNDEFINE(-1, "未定义异常信息");
    private int code;
    private String msg;
}
