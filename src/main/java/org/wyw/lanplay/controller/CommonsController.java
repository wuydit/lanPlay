package org.wyw.lanplay.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.wyw.lanplay.dto.BaseEntity;
import org.wyw.lanplay.utils.FileHandleUtil;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Api
@Controller("commons")
public class CommonsController {

    @ApiOperation("上传")
    @PostMapping("onloadUpdateFile")
    public ResponseEntity onloadUpdateFile(@RequestParam MultipartFile file)throws IOException{
        String str= FileHandleUtil.upload(file.getInputStream(),"update/",
                UUID.randomUUID().toString() +
                        Objects.requireNonNull(file.getOriginalFilename())
                                .substring(file.getOriginalFilename().lastIndexOf(".")));
        return ResponseEntity.ok(BaseEntity.ok(str));
    }
}
