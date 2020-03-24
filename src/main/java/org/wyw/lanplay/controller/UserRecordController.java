package org.wyw.lanplay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.wyw.lanplay.aop.Log;
import org.wyw.lanplay.entity.ServerRecordEntity;
import org.wyw.lanplay.entity.UserRecordEntity;
import org.wyw.lanplay.service.ServerRecordService;
import org.wyw.lanplay.service.UserRecordService;

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
@RequestMapping("/user-record-entity")
public class UserRecordController {


    private UserRecordService userRecordService;

    public UserRecordController(UserRecordService userRecordService) {
        this.userRecordService = userRecordService;
    }

    @Log(desc = "用户列表")
    @GetMapping("user")
    @ApiOperation("用户列表")
    public ResponseEntity<List<UserRecordEntity>> list(){
        return ResponseEntity.ok(userRecordService.list());
    }

    @Log(desc = "用户分页")
    @GetMapping("userPage")
    @ApiOperation("用户分页")
    public ResponseEntity<Page<UserRecordEntity>> userPage(@RequestParam(value = "size", defaultValue = "20") Integer size,
                                                         @RequestParam(value = "current", defaultValue = "1") Integer current){
        return ResponseEntity.ok(userRecordService.page(new Page<>(current, size)));
    }

    @Log(desc = "用户添加")
    @PostMapping("user")
    @ApiOperation("用户添加")
    public ResponseEntity addUser(@RequestBody UserRecordEntity userRecordEntity){
        return ResponseEntity.ok(userRecordService.save(userRecordEntity));
    }

    @Log(desc = "用户修改")
    @PutMapping("user")
    @ApiOperation("用户修改")
    public ResponseEntity updateUser(@RequestBody UserRecordEntity userRecordEntity){
        return ResponseEntity.ok(userRecordService.updateById(userRecordEntity));
    }

    @Log(desc = "用户获取")
    @GetMapping("user/{id}")
    @ApiOperation("用户获取")
    public ResponseEntity user(@PathVariable("id") Long id){
        return ResponseEntity.ok(userRecordService.getById(id));
    }

    @Log(desc = "用户获取")
    @DeleteMapping("user/{id}")
    @ApiOperation("用户获取")
    public ResponseEntity applyServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(userRecordService.removeById(id));
    }
}

