package org.wyw.lanplay.dto;


public class JoinServerDTO extends BaseMsgDTO {

    private Long serverId;

    private String password;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
