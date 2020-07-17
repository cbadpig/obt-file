package com.hyt.obt.file.controller;

import com.hyt.obt.file.service.CandidateMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "上传图片和视频")
@RestController
@RequestMapping("monitor")
public class MonitorUploadController {

    @Autowired
    private CandidateMonitorService candidateMonitorService;

    @ApiOperation(value = "上传图片", notes = "返回结果")
    @RequestMapping(value = "/upload/picture", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadPicture(@RequestPart(value = "file") MultipartFile file , HttpServletRequest request,String dir,String md5) {
        return candidateMonitorService.uploadPicture(file,request,dir,md5);
    }

    @ApiOperation(value = "上传视频", notes = "返回结果")
    @RequestMapping(value = "/upload/video", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadPicture(@RequestPart(value = "file") MultipartFile file,String dir,String md5) {
        return candidateMonitorService.uploadVideo(file,dir,md5);
    }

}
