package org.wyw.lanplay.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegDTO implements Serializable {
    private String username;
    private String password;
    private String code;
    private String nickname;
    private String ip;
}
