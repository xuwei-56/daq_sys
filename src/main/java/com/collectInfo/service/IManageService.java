package com.collectInfo.service;

import com.collectInfo.model.Manage;

public interface IManageService {

	/**
	 * 为设备添加管理员
	 * @param manage
	 * @return
	 */
	public int insertManage(Manage manage);
	
	/**
	 * 更新设备管理员
	 * @param manage
	 * @return
	 */
	public int updateManage(Manage manage);
	
	/**
	 * 删除管理的设备
	 * @param deviceId
	 * @return
	 */
	public int deleteManage(int deviceId);
}
