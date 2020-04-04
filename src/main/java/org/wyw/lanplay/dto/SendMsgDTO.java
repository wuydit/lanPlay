package org.wyw.lanplay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SendMsgDTO implements Serializable {

    private String user;

    private String msg;
}
