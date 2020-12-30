package com.spring.service;

import com.spring.model.Gather;
import com.spring.page.Page;

import java.math.BigInteger;
import java.util.List;

public interface GatherService {
	/**
	 * 分页显示采集列表
	 * @param page 分页
	 * @param str 查询信息
	 * @return
	 */
	List<Gather> getGatherPageAll(Page page,String str,BigInteger parent);

	/**
	 * 查询采集列表
	 * @param str 查询信息
	 * @return
	 */
	List<Gather> getGatherAll(String str,BigInteger parent);

	/**
	 * 根据编号查询id
	 * @param
	 * @return
	 */
	BigInteger getGatherByNo(String gatherno);

	/**
	 * 判断采集编号是否存在
	 * @param
	 * @return
	 */
	int getGatherNoCount(String gatherno,BigInteger item);
	int getGatherNoByItemCount(String gatherno);

	/**
	 * 根据id查询采集信息
	 * @param id 采集id
	 * @return
	 */
	Gather getGatherById(BigInteger id);

	/**
	 * 添加采集信息
	 * @param
	 */
	void addGather(Gather ins);

	/**
	 * 修改采集信息
	 * @param
	 */
	void editGather(Gather ins);

	/**
	 * 删除采集信息
	 * @param
	 */
	void deleteGather(String id);

	/**
	 * 根据组织机构id查找采集id及编号
	 * @param insfid 组织机构id
	 * @return
	 */
	List<Gather> getGatherByInsfid(BigInteger insfid);

	/**
	 * 根据焊机id查询采集模块
	 */
	List<Gather> findGatherByMachineId(BigInteger machineId);
}
