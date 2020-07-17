package com.hyt.obt.file.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface CandidateMonitorService {

    public String uploadPicture(MultipartFile file , HttpServletRequest request,String dir,String md5);


    public String uploadVideo(MultipartFile file,String dir,String md5);

}
