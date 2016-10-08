package com.collectInfo.dao;

import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;

import com.collectInfo.model.Data;

public interface DataDao {
    int deleteByPrimaryKey(String dataId);

    int insert(Data record);

    int insertSelective(Data record);

    Data selectByPrimaryKey(String dataId);

    int updateByPrimaryKeySelective(Data record);

    int updateByPrimaryKey(Data record);
    
    List<HashMap<String,Object>> selectDataByIp_Datetime(@Param(value="device_ip")String device_ip,@Param(value="start_time")String start_time,@Param(value="end_time")String end_time,@Param(value="startNumber")int startNumber);
}