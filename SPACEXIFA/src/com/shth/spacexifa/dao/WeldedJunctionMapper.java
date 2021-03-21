package com.shth.spacexifa.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shth.spacexifa.dto.WeldDto;
import com.shth.spacexifa.model.WeldedJunction;

import tk.mybatis.mapper.common.Mapper;

public interface WeldedJunctionMapper extends Mapper<WeldedJunction>{
	List<WeldedJunction> getWeldedJunctionAll(@Param("str")String str);

	List<WeldedJunction> getTaskResultAll(@Param("str")String str);

	List<WeldedJunction> getJunctionByWelder(@Param("welder")String welder,@Param("dto")WeldDto dto);

	WeldedJunction getWeldedJunctionById(@Param("id")BigInteger id);

	boolean addJunction(WeldedJunction wj);

	boolean updateJunction(WeldedJunction wj);

	boolean deleteJunction(@Param("id")BigInteger id);

	int getWeldedjunctionByNo(@Param("wjno")String wjno);

	List<WeldedJunction> getJunctionHistory(@Param("search")String search);

	List<WeldedJunction> getJunctionweldtime(@Param("search")String search,@Param("tasktime")String tasktime);

	List<WeldedJunction> getJunctionstandtime(@Param("search")String search);

	List<WeldedJunction> getWelderweldtime(@Param("search")String search,@Param("tasktime")String tasktime);

	List<WeldedJunction> getmachineweldtime(@Param("search")String search,@Param("tasktime")String tasktime);

	List<WeldedJunction> getMachineweldtime(@Param("search")String search);

	List<WeldedJunction> getJMByWelder(@Param("dto") WeldDto dto,@Param("welderid")String welderid);

	String getFirsttime(@Param("dto") WeldDto dto,@Param("machineid")BigInteger machineid, @Param("welderid")String welderid, @Param("junid")String junid);

	String getLasttime(@Param("dto") WeldDto dto,@Param("machineid")BigInteger machineid, @Param("welderid")String welderid, @Param("junid")String junid);

	int getCountByTaskid(@Param("taskid")BigInteger taskid,@Param("type")BigInteger type);

	boolean addTask(WeldedJunction wj);

	boolean updateTask(WeldedJunction wj);

	boolean updateTaskByFid(WeldedJunction wj);

	List<WeldedJunction> getFreeJunction(@Param("str")String str);

	List<WeldedJunction> getRealWelder(@Param("taskid")BigInteger taskid);

	List<WeldedJunction> getSwDetail(@Param("taskno")String taskno,@Param("time")String time,@Param("dto") WeldDto dto);

	List<WeldedJunction> getCardList(@Param("search")String search);

	void addCard(WeldedJunction wj);

	void updateCard(WeldedJunction wj);

	void deleteCard(String fid);

	void addProductNum(WeldedJunction wj);

	List<WeldedJunction> getProductList(@Param("search")String search);

	void deleteProduct(String fid);

	WeldedJunction getProductByCardid(@Param("fid")String fid);

	void turnDown(WeldedJunction weldtask);

	void passReview(@Param("fid")String fid,@Param("value")String value);

	void updateProductNum(WeldedJunction wj);

	void addProductWpsHistory(@Param("fid")String fid,@Param("wpsId")String wpsId, @Param("userId")String userId, @Param("time")String time);

	List<WeldedJunction> getProductWpsHistory(@Param("search")String search);

	int getCardCount(@Param("cardName")String cardName);

	int getTaskCount(@Param("taskName")String taskName);

	String getWpsIdByCardId(@Param("fid")String fid);

}
