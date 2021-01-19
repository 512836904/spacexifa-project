package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import com.spring.model.Welder;
import com.spring.page.Page;

public interface WelderService {
	/**
	 * 获取所有焊工信息
	 * @param page
	 * @param
	 * @return
	 */
	List<Welder> getWelderAll(Page page, String str);
	
	/**
	 * 新增焊工信息
	 * @param we
	 */
	void addWelder(Welder we);
	
	/**
	 * 修改焊工信息
	 * @param we
	 */
	void editWelder(Welder we);
	
	/**
	 * 删除焊工信息
	 * @param id
	 */
	void removeWelder(BigInteger id);
	
	/**
	 * 判断焊工编号是否存在
	 * @param wno
	 * @return
	 */
	int getWeldernoCount(String wno);

	/**
	 * 查询所有焊工及对应班组信息
	 */
	List<Welder> findAllWelderInfo();
}