package com.hyt.obt.file.service.impl;

import com.arcsoft.face.FaceFeature;
import com.hyt.obt.file.entity.Monitor;
import com.hyt.obt.file.entity.MonitorYml;
import com.hyt.obt.file.mapper.MonitorMapper;
import com.hyt.obt.file.service.CandidateMonitorService;
import com.hyt.obt.file.util.FaceARCUtil;
import com.hyt.obt.file.wrapper.FastDFSClientWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CandidateMonitorServiceImpl implements CandidateMonitorService {

    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;
    @Autowired
    private MonitorMapper monitorMapper;
    @Autowired
    private MonitorYml monitorYml;

    @Override
    public String uploadPicture(MultipartFile file, HttpServletRequest request, String dir, String md5) {

        try {
            if (file == null || StringUtils.isBlank(file.getOriginalFilename()) || StringUtils.isBlank(dir)) {
                return "{\"code\":1,\"msg\":\"Failed\"}";
            }

            String path = fastDFSClientWrapper.uploadFile(file);
            if (StringUtils.isNotBlank(path)) {
                //人脸识别
                String exceptionType = "100";
                try {
                    exceptionType = FaceARCUtil.faceARC(firstFaceFeature(request,dir),file.getInputStream());
                } catch (Exception e) {

                }
                insertMonitor(dir,"100",exceptionType,path);
            }

        } catch (Exception e) {
            return "{\"code\":1,\"msg\":\"Failed\"}";
        }

        return "{\"code\":0,\"msg\":\"Successed\"}";
    }

    @Override
    public String uploadVideo(MultipartFile file, String dir, String md5) {
        try {
            if (file == null || StringUtils.isBlank(file.getOriginalFilename()) || StringUtils.isBlank(dir)) {
                return "{\"code\":1,\"msg\":\"Failed\"}";
            }

            String path = fastDFSClientWrapper.uploadFile(file);
            if (StringUtils.isNotBlank(path)) {
                insertMonitor(dir,"400","100",path);
            }

        } catch (Exception e) {
            return "{\"code\":1,\"msg\":\"Failed\"}";
        }

        return "{\"code\":0,\"msg\":\"Successed\"}";
    }


    public void insertMonitor(String dir,String fileType,String exceptionType,String filepath) {

        String[] dirs = dir.split("_");
        String kspcbh = dirs[0];
        String kcdm = dirs[1];
        String ksxxdm = dirs[2];
        String xm = dirs[3];
        LocalDateTime nowDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");


        Monitor monitor = Monitor.builder()
                .ksxxdm(ksxxdm)
                .cname(xm)
                .kspcbh(kspcbh)
                .kcdm(kcdm)
                .filePath(filepath)
                .fileType(fileType)
                .exceptionType(exceptionType)
                .uploadTime(nowDateTime.format(dateTimeFormatter))
                .build();

        monitorMapper.insertMonitor(monitor);
    }

    private FaceFeature firstFaceFeature(HttpServletRequest request, String dir) {

        InputStream is = null;
        FaceFeature faceFeature = null;
        try {
            String[] dirs = dir.split("_");
            String kspcbh = dirs[0];
            String kcdm = dirs[1];
            String ksxxdm = dirs[2];

            String key = "picture_first_" +  ksxxdm;
            faceFeature = (FaceFeature) request.getSession().getAttribute(key);
            if (faceFeature != null) {
                return faceFeature;
            }

            String filePath = monitorMapper.selectFirstKsMonitor(ksxxdm,kcdm,kspcbh);
            if (StringUtils.isBlank(filePath)) {
                return null;
            }
            System.out.println(MonitorYml.getStaticFastDfsUlr() + filePath);
            URL url = new URL(MonitorYml.getStaticFastDfsUlr()+ filePath);
            is = url.openStream();
            faceFeature = FaceARCUtil.getFaceFeature(is);
            request.getSession().setAttribute(key,faceFeature);
        } catch (Exception e) {
            return null;
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    return null;
                }
            }
        }

        return faceFeature;
    }
}
