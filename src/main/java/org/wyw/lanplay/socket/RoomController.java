package org.wyw.lanplay.socket;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.wyw.lanplay.dto.MsgDTO;
import org.wyw.lanplay.dto.SendMsgDTO;
import org.wyw.lanplay.entity.CollectRecordEntity;
import org.wyw.lanplay.entity.UserRecordEntity;
import org.wyw.lanplay.service.CollectRecordService;
import org.wyw.lanplay.service.CommonService;

@Slf4j
@Controller
public class RoomController {

    private SimpMessagingTemplate simpMessagingTemplate;

    private CollectRecordService collectRecordService;

    private CommonService commonService;

    public RoomController(SimpMessagingTemplate simpMessagingTemplate,
                          CollectRecordService collectRecordService,
                          CommonService commonService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.collectRecordService = collectRecordService;
        this.commonService = commonService;
    }

    @MessageMapping("send.roomMsg")
    public void handle(MsgDTO msg) {
        UserRecordEntity userRecordEntity = commonService.verifyUser(msg.getToken());
        if(userRecordEntity == null ){
            log.info("未验证的用户，禁止发消息");
            return;
        }
        if(!userRecordEntity.getId().equals(msg.getUserId())){
            log.info("非本房间用户，禁止发消息");
            return;
        }
        log.info(msg.toString());
        int count = collectRecordService.count(Wrappers.<CollectRecordEntity>lambdaQuery()
                .eq(CollectRecordEntity::getServerId, msg.getServerId())
                .eq(CollectRecordEntity::getUserId, msg.getUserId())
        );
        if(count > 0 ){
            simpMessagingTemplate.convertAndSendToUser(
                    String.valueOf(msg.getServerId()),"/room/roomMsg",
                    new SendMsgDTO(userRecordEntity.getNickname(), msg.getMsg()));
        }
    }
}

