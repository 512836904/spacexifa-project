package com.shth.spacexifa.dao;

import com.shth.spacexifa.dto.WeldDto;
import com.shth.spacexifa.model.DataStatistics;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigInteger;
import java.util.List;

public interface DataStatisticsMapper  extends Mapper<DataStatistics>{
	List<DataStatistics> getItemMachineCount(@Param("parent") BigInteger parent);

	List<DataStatistics> getItemMachineByItemType(@Param("dto") WeldDto dto,@Param("itemtype") int itemtype);

	List<DataStatistics> getMachineRadio(@Param("dto") WeldDto dto,@Param("itemtype") int itemtype);

	int getStartingUpMachineNum(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	DataStatistics getWorkMachineNum(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	DataStatistics getmaxele(@Param("fjunctionid") BigInteger fjunctionid);

	DataStatistics getWorkJunctionNum(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	DataStatistics getWorkJunctionNumByWelder(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	BigInteger  getStaringUpTime(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	BigInteger  getStaringUpTimeByJunction(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	BigInteger  getStaringUpTimeByWelder(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	DataStatistics getParameter();

	BigInteger getStandytime(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	BigInteger getStandytimeByWelder(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	BigInteger getStandytimeByJunction(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	List<DataStatistics> getweldernameCount(@Param("dto") WeldDto dto,@Param("tasktime") String tasktime);

	BigInteger getWorkingJunction(@Param("dto") WeldDto dto);

	BigInteger getStandJunction(@Param("dto") WeldDto dto);

	DataStatistics getEleVolByJunction(@Param("dto") WeldDto dto);

	DataStatistics getWorkTimeAndEleVol(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	DataStatistics getItemWorkTime(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	DataStatistics getWorkTimeAndEleVolByWelder(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	DataStatistics getWorkTimeAndEleVolByJunction(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	List<DataStatistics> getAllMachine(@Param("item") BigInteger item);

	List<DataStatistics> getAllWelder(@Param("parent") BigInteger parent,@Param("dto") WeldDto dto);

	List<DataStatistics> getAllWelder(@Param("parent") BigInteger parent);

	List<DataStatistics> getAllJunction(@Param("junctionno") String junctionno);

	List<DataStatistics> getAllInsframe(@Param("parent") BigInteger parent);

	List<DataStatistics> getWeldItemInCount(@Param("dto") WeldDto dto);

	List<DataStatistics> getWeldItemOutCount(@Param("dto") WeldDto dto);

	List<DataStatistics> getWeldMachineInCount(@Param("dto") WeldDto dto,@Param("itemid") BigInteger itemid);

	List<DataStatistics> getWeldMachineOutCount(@Param("dto") WeldDto dto,@Param("itemid") BigInteger itemid);

	List<DataStatistics> getWeldPersonInCount(@Param("dto") WeldDto dto);

	List<DataStatistics> getWeldPersonOutCount(@Param("dto") WeldDto dto);

	List<DataStatistics> getWeldPieceInCount(@Param("dto") WeldDto dto,@Param("junctionno") String junctionno);

	List<DataStatistics> getWeldPieceOutCount(@Param("dto") WeldDto dto,@Param("junctionno") String junctionno);

	List<DataStatistics> getFauit(@Param("dto") WeldDto dto,@Param("value") int value);

	List<DataStatistics> getFauitDetail(@Param("dto") WeldDto dto,@Param("id") BigInteger id,@Param("value") int value);

	List<DataStatistics> getWorkRank(@Param("parent")BigInteger parent,@Param("time")String time,@Param("time2")String time2);

	List<DataStatistics> getWelderWorkTime(@Param("itemid")BigInteger itemid,@Param("time")String time);

	List<DataStatistics> getPersonWorkTime(@Param("itemid")BigInteger itemid,@Param("time")String time);

	List<DataStatistics> getStandbyRank(@Param("parent")BigInteger parent,@Param("time")String time);

	DataStatistics getWorkMachineCount(@Param("itemid")BigInteger itemid,@Param("time")String time);

	List<DataStatistics> getItemWeldTime(@Param("dto")WeldDto dto);

	List<DataStatistics> getItemOverProofTime(@Param("dto")WeldDto dto);

	List<DataStatistics> getOnAndWeldTime(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	List<DataStatistics> getEquipmentUtilize(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	List<DataStatistics> getWireAndFlow(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	List<DataStatistics> getTaskDetail(@Param("parent")BigInteger parent,@Param("welderno")String welderno,@Param("taskno")String taskno,@Param("dtoTime1")String dtoTime1,@Param("dtoTime2")String dtoTime2);

	List<DataStatistics> getTask(@Param("parent")BigInteger parent,@Param("welderno")String welderno,@Param("taskno")String taskno,@Param("dtoTime1")String dtoTime1,@Param("dtoTime2")String dtoTime2);

	List<DataStatistics> getMachineTask(@Param("parent")BigInteger parent,@Param("sql")String sql,@Param("type")int type);

	List<DataStatistics> getMachineNoTask(@Param("itemid")BigInteger itemid,@Param("time1")String time1, @Param("totime")String totime, @Param("time2")String time2);

	List<DataStatistics> getWarnTimes(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	List<DataStatistics> getTaskDetails(@Param("itemid") BigInteger itemid,@Param("dto") WeldDto dto);

	List<DataStatistics> getHistoryData(@Param("str") String str, @Param("filed") String filed);

	List<DataStatistics> getMachineData(@Param("itemid")BigInteger insid, @Param("dto") WeldDto dto);

	List<DataStatistics> findAverageWorkingTime(@Param("startTime")String startTime,@Param("itemType")int itemType);

	List<DataStatistics> findStandbyWorkingTime(@Param("startTime")String startTime,@Param("itemType")int itemType);

	List<DataStatistics> findLoadRateList(@Param("startTime")String startTime,@Param("itemType")int itemType);

	List<DataStatistics> getWireMatral(@Param("time1")String time1,@Param("itemType")int itemType);

	List<DataStatistics> findWelderMaterialConsume(@Param("startTime")String startTime);

	List<DataStatistics> findSupergageInfo(@Param("startTime")String startTime);

	List<DataStatistics> findSupergageCumulativeNumber(@Param("startTime")String startTime,@Param("itemType")int itemType);

	List<DataStatistics> findJobSetNormRate(@Param("startTime")String startTime);

	List<DataStatistics> countWelderNumByIid();
}