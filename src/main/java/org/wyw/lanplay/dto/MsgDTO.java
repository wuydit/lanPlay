package org.wyw.lanplay.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MsgDTO implements Serializable {

    private Long serverId;

    private Long userId;

    private String msg;

    private String token;
}
