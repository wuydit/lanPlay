package org.wyw.lanplay.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.wyw.lanplay.aop.Log;
import org.wyw.lanplay.dto.BaseEntity;
import org.wyw.lanplay.dto.LoginDTO;
import org.wyw.lanplay.entity.InvitationCodeEntity;
import org.wyw.lanplay.entity.UserRecordEntity;
import org.wyw.lanplay.service.CommonService;
import org.wyw.lanplay.service.InvitationCodeService;
import org.wyw.lanplay.service.UserRecordService;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static org.wyw.lanplay.dto.ResultEnum.*;

@Api
@RestController
@RequestMapping("/auth")
public class AuthController {

    private CommonService commonService;

    private UserRecordService userRecordService;

    private InvitationCodeService invitationCodeService;

    public AuthController(CommonService commonService,
                          UserRecordService userRecordService,
                          InvitationCodeService invitationCodeService) {
        this.commonService = commonService;
        this.userRecordService = userRecordService;
        this.invitationCodeService = invitationCodeService;
    }

    @Log(desc = "登录")
    @PostMapping("login")
    public ResponseEntity<BaseEntity> login(@RequestBody LoginDTO loginDTO){
        UserRecordEntity userEntity = userRecordService.getOne(
                Wrappers.<UserRecordEntity>lambdaQuery().eq(UserRecordEntity::getUsername, loginDTO.getUsername()));
        if(ObjectUtils.isEmpty(userEntity)){
            return ResponseEntity.ok(BaseEntity.status(USER_NOT_FIND));
        }
        String cryStr =  commonService.encryptedPwd(loginDTO.getUsername(), loginDTO.getPassword());
        if(!StringUtils.equals(userEntity.getPassword(), cryStr)){
            return ResponseEntity.ok(BaseEntity.status(USER_ACCOUNT_ERROR));
        }
        if(userEntity.getStatus() == 1){
            return ResponseEntity.ok(BaseEntity.status(USER_BAN));
        }
        if(userEntity.getStatus() == 2){
            return ResponseEntity.ok(BaseEntity.status(USER_STOP));
        }
        return ResponseEntity.ok(BaseEntity.ok(commonService.createToken(userEntity)));
    }

    @Log(desc = "注册")
    @PostMapping("reg")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<BaseEntity> login(HttpServletRequest request,
                                        @RequestParam("username")String username,
                                        @RequestParam("password")String pwd,
                                        @RequestParam("code")String code,
                                        @RequestParam("nickname")String nickname,
                                        @RequestParam("ip")String ip){
        int codeCount = invitationCodeService.count(Wrappers.<InvitationCodeEntity>lambdaQuery()
                .eq(InvitationCodeEntity::getCode, code));
        if(codeCount > 0){
            UserRecordEntity userRecordEntity = new UserRecordEntity();
            userRecordEntity.setCreateAt(LocalDateTime.now());
            userRecordEntity.setIdentity("USER");
            userRecordEntity.setIp(ip);
            userRecordEntity.setNickname(nickname);
            userRecordEntity.setPassword(commonService.encryptedPwd(username,pwd));
            userRecordEntity.setUsername(username);
            userRecordEntity.setStatus(0);
            if(userRecordService.save(userRecordEntity)){
                invitationCodeService.remove(Wrappers.<InvitationCodeEntity>
                        lambdaQuery().eq(InvitationCodeEntity::getCode, code));
                return ResponseEntity.ok(BaseEntity.ok(commonService.createToken(userRecordEntity)));
            }
        }else {
            return ResponseEntity.ok(BaseEntity.status(USER_INVITATION_CODE_ERROR));
        }
        return ResponseEntity.ok(BaseEntity.status(UNDEFINE));
    }
}
