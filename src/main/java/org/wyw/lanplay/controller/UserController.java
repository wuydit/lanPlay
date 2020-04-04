package org.wyw.lanplay.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wyw.lanplay.annotation.LoginAdminRequired;
import org.wyw.lanplay.annotation.LoginUserRequired;
import org.wyw.lanplay.aop.Log;
import org.wyw.lanplay.dto.BaseEntity;
import org.wyw.lanplay.entity.ApplyServerRecordEntity;
import org.wyw.lanplay.entity.UserRecordEntity;
import org.wyw.lanplay.service.ApplyServerRecordService;
import org.wyw.lanplay.service.CommonService;
import org.wyw.lanplay.service.UserRecordService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Api
@RestController
@RequestMapping("/user")
public class UserController {

    private CommonService commonService;

    private UserRecordService userRecordService;

    private ApplyServerRecordService applyServerRecordService;

    public UserController(CommonService commonService,
                          UserRecordService userRecordService,
                          ApplyServerRecordService applyServerRecordService) {
        this.commonService = commonService;
        this.userRecordService = userRecordService;
        this.applyServerRecordService = applyServerRecordService;
    }

    @Log(desc = "申请服务器")
    @PostMapping("applyServer")
    @ApiOperation("申请服务器")
    @LoginUserRequired
    public ResponseEntity applyServer(HttpServletRequest request,
                                      @RequestParam("maxPeople") Integer maxPeople,
                                      @RequestParam("serverName") String serverName,
                                      @RequestParam("node") String node,
                                      @RequestParam("isPub") Boolean isPub,
                                      @RequestParam("pwd") String pwd){
        UserRecordEntity userRecordEntity = commonService.verifyUser(request.getHeader("token"));
        ApplyServerRecordEntity applyServerRecordEntity = new ApplyServerRecordEntity();
        applyServerRecordEntity.setCreateAt(LocalDateTime.now());
        applyServerRecordEntity.setUserId(userRecordEntity.getId());
        applyServerRecordEntity.setIsDel(Boolean.FALSE);
        applyServerRecordEntity.setAddress(request.getRemoteAddr());
        applyServerRecordEntity.setMaxPeople(maxPeople);
        applyServerRecordEntity.setName(serverName);
        applyServerRecordEntity.setNode(node);
        applyServerRecordEntity.setIsPublic(isPub);
        applyServerRecordEntity.setServerPwd(pwd);
        return ResponseEntity.ok(BaseEntity.ok(applyServerRecordService.save(applyServerRecordEntity)));
    }

    @Log(desc = "获取用户信息")
    @GetMapping("user")
    @ApiOperation("获取用户信息")
    @LoginUserRequired
    public ResponseEntity<BaseEntity> user(HttpServletRequest request){
        UserRecordEntity userRecordEntity = commonService.verifyUser(request.getHeader("token"));
        userRecordEntity.setPassword("");
        return ResponseEntity.ok(BaseEntity.ok(userRecordEntity));
    }


}
