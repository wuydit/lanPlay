package org.wyw.lanplay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.wyw.lanplay.entity.UserRecordEntity;
import org.wyw.lanplay.service.CommonService;
import java.util.Map;

@Slf4j
public class TokenAuthHandshakeInterceptor implements HandshakeInterceptor {

    private CommonService commonService;

    public TokenAuthHandshakeInterceptor(CommonService commonService) {
        this.commonService = commonService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
       String query = serverHttpRequest.getURI().getQuery();
        String token = query.substring(query.indexOf("token=") + 6, (query.indexOf("&") == -1? query.length():query.indexOf("&")));
        UserRecordEntity userRecordEntity = commonService.verifyUser(token);
        if(userRecordEntity == null){
            return false;
        }
        map.put("user", userRecordEntity);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
    }
}
