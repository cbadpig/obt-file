package com.hyt.obt.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyt.obt.file.entity.Monitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MonitorMapper {

    int insertMonitor(Monitor monitor);

    String selectFirstKsMonitor(@Param("ksxxdm")String ksxxdm,@Param("kcdm")String kcdm,@Param("kspcbh")String kspcbh);

}
