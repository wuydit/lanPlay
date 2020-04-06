package org.wyw.lanplay.socket;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.stereotype.Controller;
import org.wyw.lanplay.dto.*;
import org.wyw.lanplay.entity.CollectRecordEntity;
import org.wyw.lanplay.entity.ServerRecordEntity;
import org.wyw.lanplay.entity.UserRecordEntity;
import org.wyw.lanplay.service.CollectRecordService;
import org.wyw.lanplay.service.CommonService;
import org.wyw.lanplay.service.ServerRecordService;

import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@Controller
public class RoomController {

    private SimpMessagingTemplate simpMessagingTemplate;

    private CollectRecordService collectRecordService;

    private ServerRecordService serverRecordService;


    public RoomController(SimpMessagingTemplate simpMessagingTemplate,
                          CollectRecordService collectRecordService,
                          ServerRecordService serverRecordService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.collectRecordService = collectRecordService;
        this.serverRecordService = serverRecordService;
    }

    @MessageMapping("send.roomMsg")
    public void handle(MsgDTO msg, UserRecordEntity userRecordEntity) {
        int count = collectRecordService.count(Wrappers.<CollectRecordEntity>lambdaQuery()
                .eq(CollectRecordEntity::getServerId, msg.getServerId())
                .eq(CollectRecordEntity::getUserId, userRecordEntity.getId())
        );
        if(count > 0 ){
            simpMessagingTemplate.convertAndSendToUser(
                    String.valueOf(msg.getServerId()),"/room/roomMsg",
                    new SendMsgDTO(
                            userRecordEntity.getName(),
                            userRecordEntity.getUrl(),
                            msg.getMsg()));
        }
    }

    @MessageMapping("ctl")
    public void handle(String msg, UserRecordEntity userRecordEntity) {
        String type = JSON.parseObject(msg).getString("type");
        if(StringUtils.equals(type, "join")){
            JoinServerDTO joinServerDTO = JSON.parseObject(msg, JoinServerDTO.class);
            ServerRecordEntity serverRecordEntity = serverRecordService.getById(joinServerDTO.getServerId());
            int count = collectRecordService.count(Wrappers.<CollectRecordEntity>lambdaQuery()
                    .eq(CollectRecordEntity::getServerId, joinServerDTO.getServerId()));
            if(count >= serverRecordEntity.getMaxPeople()){
                simpMessagingTemplate.convertAndSendToUser(userRecordEntity.getUsername(),
                        "/user",JSON.toJSONString(BaseEntity.status(ResultEnum.UNDEFINE,"最大上限")));
            }
            if(!serverRecordEntity.getIsPub()){
                if(!StringUtils.equals(serverRecordEntity.getPassword(), joinServerDTO.getPassword())){
                    simpMessagingTemplate.convertAndSendToUser(userRecordEntity.getUsername(),
                            "/user",JSON.toJSONString(BaseEntity.status(ResultEnum.UNDEFINE,"密码错误")));
                }
            }
            CollectRecordEntity collectRecordEntity = new CollectRecordEntity();
            collectRecordEntity.setCreateAt(LocalDateTime.now());
            collectRecordEntity.setIsDel(Boolean.FALSE);
            collectRecordEntity.setServerId(serverRecordEntity.getId());
            collectRecordEntity.setUserId(userRecordEntity.getId());
            collectRecordService.save(collectRecordEntity);
            simpMessagingTemplate.convertAndSendToUser(userRecordEntity.getUsername(),
                    "/user",JSON.toJSONString(BaseEntity.status(ResultEnum.SUCCESS,
                            serverRecordEntity.getAddress())));
        }

        if(StringUtils.equals(type, "out")){
            JoinServerDTO joinServerDTO = JSON.parseObject(msg, JoinServerDTO.class);
            collectRecordService.remove(Wrappers.<CollectRecordEntity>lambdaQuery()
                    .eq(CollectRecordEntity::getServerId,joinServerDTO.getServerId())
                    .eq(CollectRecordEntity::getUserId, userRecordEntity.getId())
            );
            simpMessagingTemplate.convertAndSendToUser(userRecordEntity.getUsername(),
                    "/user",JSON.toJSONString(BaseEntity.status(ResultEnum.SUCCESS,
                           "退出服务器成功")));
        }
    }
}

