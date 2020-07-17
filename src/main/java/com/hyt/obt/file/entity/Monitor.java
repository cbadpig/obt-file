package com.hyt.obt.file.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@TableName("t_ks_monitor")
@ApiModel(value="Monitor对象", description="考生图片视频上传记录表")
public class Monitor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String ksxxdm;
    private String cname;
    private String kcdm;
    private String kspcbh;
    private String filePath;
    private String fileType;
    private String exceptionType;
    private String uploadTime;

}
