package org.wyw.lanplay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.wyw.lanplay.aop.Log;
import org.wyw.lanplay.entity.ServerRecordEntity;
import org.wyw.lanplay.service.ServerRecordService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wuYd
 * @since 2020-03-24
 */
@Controller
@RequestMapping("/")
public class ServerRecordController {

    private ServerRecordService serverRecordService;

    public ServerRecordController(ServerRecordService serverRecordService) {
        this.serverRecordService = serverRecordService;
    }

    @Log(desc = "服务器列表")
    @GetMapping("server")
    @ApiOperation("申请服务器列表")
    public ResponseEntity<List<ServerRecordEntity>> list(){
        return ResponseEntity.ok(serverRecordService.list());
    }

    @Log(desc = "服务器分页")
    @GetMapping("serverPage")
    @ApiOperation("申请服务器分页")
    public ResponseEntity<Page<ServerRecordEntity>> page(@RequestParam(value = "size", defaultValue = "20") Integer size,
                                                              @RequestParam(value = "current", defaultValue = "1") Integer current){
        return ResponseEntity.ok(serverRecordService.page(new Page<>(current, size)));
    }

    @Log(desc = "服务器添加")
    @PostMapping("server")
    @ApiOperation("服务器添加")
    public ResponseEntity applyServer(@RequestBody ServerRecordEntity serverRecordEntity){
        return ResponseEntity.ok(serverRecordService.save(serverRecordEntity));
    }

    @Log(desc = "服务器修改")
    @PutMapping("server")
    @ApiOperation("服务器修改")
    public ResponseEntity updateServer(@RequestBody ServerRecordEntity serverRecordEntity){
        return ResponseEntity.ok(serverRecordService.updateById(serverRecordEntity));
    }

    @Log(desc = "服务器获取")
    @GetMapping("server/{id}")
    @ApiOperation("服务器获取")
    public ResponseEntity applyServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(serverRecordService.getById(id));
    }

    @Log(desc = "服务器删除")
    @DeleteMapping("server/{id}")
    @ApiOperation("服务器删除")
    public ResponseEntity deleteServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(serverRecordService.removeById(id));
    }
}

