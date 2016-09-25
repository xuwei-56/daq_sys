package com.collectInfo.dao;

import com.collectInfo.model.Data;

public interface DataDao {
    int deleteByPrimaryKey(String dataId);

    int insert(Data record);

    int insertSelective(Data record);

    Data selectByPrimaryKey(String dataId);

    int updateByPrimaryKeySelective(Data record);

    int updateByPrimaryKey(Data record);
}