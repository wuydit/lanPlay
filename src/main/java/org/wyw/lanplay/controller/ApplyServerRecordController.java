package org.wyw.lanplay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.wyw.lanplay.annotation.LoginAdminRequired;
import org.wyw.lanplay.annotation.LoginUserRequired;
import org.wyw.lanplay.aop.Log;
import org.wyw.lanplay.dto.BaseEntity;
import org.wyw.lanplay.entity.ApplyServerRecordEntity;
import org.wyw.lanplay.service.ApplyServerRecordService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wuYd
 * @since 2020-03-24
 */
@Slf4j
@Api
@RestController
@RequestMapping("/")
public class ApplyServerRecordController {

    private ApplyServerRecordService applyServerRecordService;

    public ApplyServerRecordController(ApplyServerRecordService applyServerRecordService) {
        this.applyServerRecordService = applyServerRecordService;
    }

//    @Log(desc = "申请服务器列表")
//    @GetMapping("applyServer")
//    @ApiOperation("申请服务器列表")
//    @LoginAdminRequired
//    public ResponseEntity<List<ApplyServerRecordEntity>> list(){
//        return ResponseEntity.ok(applyServerRecordService.list());
//    }

//    @Log(desc = "申请服务器分页")
//    @GetMapping("applyServerPage")
//    @ApiOperation("申请服务器分页")
//    @LoginAdminRequired
//    public ResponseEntity<Page<ApplyServerRecordEntity>> page(@RequestParam(value = "size", defaultValue = "20") Integer size,
//                                                              @RequestParam(value = "current", defaultValue = "1") Integer current){
//        return ResponseEntity.ok(applyServerRecordService.page(new Page<>(current, size)));
//    }

    @Log(desc = "申请服务器")
    @PostMapping("applyServer")
    @ApiOperation("申请服务器")
    @LoginUserRequired
    public ResponseEntity<BaseEntity> applyServer(@RequestBody ApplyServerRecordEntity applyServerRecordEntity){
        return ResponseEntity.ok(new BaseEntity<>(applyServerRecordService.save(applyServerRecordEntity)));
    }

//    @Log(desc = "申请服务器")
//    @PutMapping("applyServer")
//    @ApiOperation("申请服务器")
//    @LoginAdminRequired
//    public ResponseEntity updateServer(@RequestBody ApplyServerRecordEntity applyServerRecordEntity){
//        return ResponseEntity.ok(applyServerRecordService.updateById(applyServerRecordEntity));
//    }

//    @Log(desc = "申请服务器")
//    @GetMapping("applyServer/{id}")
//    @ApiOperation("申请服务器")
//    @LoginAdminRequired
//    public ResponseEntity applyServer(@PathVariable("id") Long id){
//        return ResponseEntity.ok(applyServerRecordService.getById(id));
//    }

//    @Log(desc = "申请服务器删除")
//    @DeleteMapping("applyServer/{id}")
//    @ApiOperation("申请服务器删除")
//    @LoginAdminRequired
//    public ResponseEntity deleteApplyServer(@PathVariable("id") Long id){
//        return ResponseEntity.ok(applyServerRecordService.removeById(id));
//    }

}

