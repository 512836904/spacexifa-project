package com.shth.spacexifa.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shth.spacexifa.model.WeldedJunction;

import tk.mybatis.mapper.common.Mapper;

public interface WeldedJunctionMapper extends Mapper<WeldedJunction>{
	List<WeldedJunction> getWeldedJunctionAll(@Param("startTime")String startTime);
	
	WeldedJunction getWeldedJunctionById(@Param("id")BigInteger id);
	
	boolean addJunction(WeldedJunction wj);

	boolean updateJunction(WeldedJunction wj);

	boolean deleteJunction(@Param("id")BigInteger id);
	
	int getWeldedjunctionByNo(@Param("wjno")String wjno);
	
	int getCountBySatus(@Param("taskid")BigInteger taskid,@Param("welderid")BigInteger welderid,@Param("machineid")BigInteger machineid,@Param("status")int status);

	boolean addTaskResult(WeldedJunction wj);

	boolean deleteTaskResult(@Param("taskid")BigInteger taskid);

	List<WeldedJunction> getTaskResultAll(@Param("str")String str);

	boolean updateTaskResult(WeldedJunction wj);
	
	void addWps(WeldedJunction wps);
	void addEmployee(WeldedJunction wps);
	void addStep(WeldedJunction wps);
	void addWeldedJunction(WeldedJunction wps);
	void addDetail(WeldedJunction wps);
	void addCard(WeldedJunction wj);
	String getIdByParam(@Param("param")String param);
	String getJunctionIdByParam(@Param("param")String param);
	void addProductNum(WeldedJunction wj);
	void addStepJunction(@Param("stepId")String stepId,@Param("junctionId")String junctionId );
	void addStepFile(WeldedJunction w);
}
