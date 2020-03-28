package org.wyw.lanplay.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wyw.lanplay.entity.InvitationCodeEntity;
import org.wyw.lanplay.entity.UserRecordEntity;
import org.wyw.lanplay.service.CommonService;
import org.wyw.lanplay.service.InvitationCodeService;
import org.wyw.lanplay.service.UserRecordService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestParam("username")String username,
                                        @RequestParam("password")String pwd){
        UserRecordEntity userEntity = userRecordService.getOne(
                Wrappers.<UserRecordEntity>lambdaQuery().eq(UserRecordEntity::getUsername,username));
        if(ObjectUtils.isEmpty(userEntity)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未找到");
        }
        if(StringUtils.equals(
                commonService.encryptedPwd(username, userEntity.getPassword()),
                commonService.encryptedPwd(username, pwd))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("账号或密码不正确");
        }
        if(userEntity.getStatus() == 1){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("已封禁");
        }
        if(userEntity.getStatus() == 2){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("已停用");
        }
        return ResponseEntity.ok(commonService.createToken(userEntity));
    }

    @PostMapping("reg")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> login(HttpServletRequest request,
                                        @RequestParam("username")String username,
                                        @RequestParam("password")String pwd,
                                        @RequestParam("code")String code,
                                        @RequestParam("nickname")String nickname){
        int codeCount = invitationCodeService.count(Wrappers.<InvitationCodeEntity>lambdaQuery()
                .eq(InvitationCodeEntity::getCode, code));
        if(codeCount > 0){
            UserRecordEntity userRecordEntity = new UserRecordEntity();
            userRecordEntity.setCreateAt(LocalDateTime.now());
            userRecordEntity.setIdentity("USER");
            userRecordEntity.setIp(request.getRemoteAddr());
            userRecordEntity.setNickname(nickname);
            userRecordEntity.setPassword(commonService.encryptedPwd(username,pwd));
            userRecordEntity.setUsername(username);
            userRecordEntity.setStatus(0);
            if(userRecordService.save(userRecordEntity)){
                invitationCodeService.remove(Wrappers.<InvitationCodeEntity>
                        lambdaQuery().eq(InvitationCodeEntity::getCode, code));
                return ResponseEntity.ok(commonService.createToken(userRecordEntity));
            }
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("注册码错误");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未知错误");
    }
}
