package org.wyw.lanplay.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wyw.lanplay.annotation.LoginAdminRequired;
import org.wyw.lanplay.annotation.LoginUserRequired;
import org.wyw.lanplay.aop.Log;
import org.wyw.lanplay.dto.BaseEntity;
import org.wyw.lanplay.entity.ServerRecordEntity;
import org.wyw.lanplay.service.ServerRecordService;
import java.util.List;

import static org.wyw.lanplay.dto.ResultEnum.SUCCESS;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wuYd
 * @since 2020-03-24
 */
@Api
@RestController
@RequestMapping("/")
public class ServerRecordController {

    private ServerRecordService serverRecordService;

    public ServerRecordController(ServerRecordService serverRecordService) {
        this.serverRecordService = serverRecordService;
    }

//    @Log(desc = "服务器列表")
//    @GetMapping("server")
//    @ApiOperation("申请服务器列表")
//    @LoginAdminRequired
//    public ResponseEntity<List<ServerRecordEntity>> list(){
//        return ResponseEntity.ok(serverRecordService.list());
//    }

    @Log(desc = "服务器分页")
    @GetMapping("serverPage")
    @ApiOperation("服务器分页")
    @LoginUserRequired
    public ResponseEntity<BaseEntity<Page<ServerRecordEntity>>> page(@RequestParam(value = "size", defaultValue = "20") Integer size,
                                                         @RequestParam(value = "current", defaultValue = "1") Integer current){
        return ResponseEntity.ok(new BaseEntity<>(SUCCESS,
                serverRecordService.page(new Page<>(current, size),
                Wrappers.<ServerRecordEntity>lambdaQuery()
                        .select(ServerRecordEntity::getId,
                                ServerRecordEntity::getMaxPeople,
                                ServerRecordEntity::getName,
                                ServerRecordEntity::getNode,
                                ServerRecordEntity::getUserId
                        ).eq(ServerRecordEntity::getIsDel, Boolean.FALSE))));
    }

    @Log(desc = "服务器{User}")
    @GetMapping("server/{id}")
    @ApiOperation("服务器{User}")
    @LoginUserRequired
    public ResponseEntity<BaseEntity<ServerRecordEntity>> server(@PathVariable String id){
        return ResponseEntity.ok(new BaseEntity<>(serverRecordService.getById(id)));
    }
//
//    @Log(desc = "服务器添加")
//    @PostMapping("server")
//    @ApiOperation("服务器添加")
//    @LoginAdminRequired
//    public ResponseEntity applyServer(@RequestBody ServerRecordEntity serverRecordEntity){
//        return ResponseEntity.ok(serverRecordService.save(serverRecordEntity));
//    }
//
//    @Log(desc = "服务器修改")
//    @PutMapping("server")
//    @ApiOperation("服务器修改")
//    @LoginAdminRequired
//    public ResponseEntity updateServer(@RequestBody ServerRecordEntity serverRecordEntity){
//        return ResponseEntity.ok(serverRecordService.updateById(serverRecordEntity));
//    }
//
//    @Log(desc = "服务器获取")
//    @GetMapping("server/{id}")
//    @ApiOperation("服务器获取")
//    @LoginAdminRequired
//    public ResponseEntity applyServer(@PathVariable("id") Long id){
//        return ResponseEntity.ok(serverRecordService.getById(id));
//    }
//
//    @Log(desc = "服务器删除")
//    @DeleteMapping("server/{id}")
//    @ApiOperation("服务器删除")
//    @LoginAdminRequired
//    public ResponseEntity deleteServer(@PathVariable("id") Long id){
//        return ResponseEntity.ok(serverRecordService.removeById(id));
//    }
}

