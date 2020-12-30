package com.spring.service;

import java.math.BigInteger;

import com.spring.model.WeldedJunction;

public interface WeldedJunctionService {
	
	/**
	 * 查询所有焊口
	 * @param object
	 * @return
	 */
	Object getWeldedJunctionAll();
	
	/**
	 * 根据id查找焊口
	 * @param object
	 * @return
	 */
	Object getWeldedJunctionById(String object);
	
	/**
	 * 判断焊缝编号是否存在
	 * @param object
	 * @return 受影响的行数
	 */
	int getWeldedjunctionByNo(String object);
	
	/**
	 * 新增焊缝
	 * @param object
	 */
	boolean addJunction(String object);

	/**
	 * 修改焊缝
	 * @param object
	 */
	boolean updateJunction(String object);

	/**
	 * 删除焊缝
	 * @param object
	 */
	boolean deleteJunction(String object);
	
	/**
	 * 查询所有任务执行结果
	 * @param object
	 * @return
	 */
	Object getTaskResultAll(String object);
	
	/**
	 * 新增任务执行结果
	 * @param object
	 */
	boolean addTaskResult(String object);

	/**
	 * 修改任务执行结果
	 * @param object
	 */
	boolean updateTaskResult(String object);

	/**
	 * 删除任务执行结果
	 * @param object
	 */
	boolean deleteTaskResult(String object);
	
	/**
	 * 发送任务相关信息给转发器
	 * @param object
	 */
	boolean giveToServer(String object);
	
	Object getMachineData();
	
	/**
	 * mes数据存储数据库
	 * @Description
	 * @author Bruce
	 * @date 2020年7月17日下午3:02:32
	 * @param wps
	 */
	void addWps(WeldedJunction wj);
	void addEmployee(WeldedJunction wj);
	void addStep(WeldedJunction wj);
	void addWeldedJunction(WeldedJunction wj);
	void addDetail(WeldedJunction wj);
	void addCard(WeldedJunction wj);
	void addProductNum(WeldedJunction wj);
	String getIdByParam(String param);
	String getJunctionIdByParam(String param);
	/**
	 * 对应工步焊缝
	 * @Description
	 * @author Bruce
	 * @date 2020年8月18日下午5:56:19
	 * @param stepId 工步id
	 * @param junctionId 焊缝id
	 */
	void addStepJunction(String stepId,String junctionId );

	void addStepFile(WeldedJunction w);
}
