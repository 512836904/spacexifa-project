package com.shth.spacexifa.service;

import com.shth.spacexifa.dto.WeldDto;
import com.shth.spacexifa.model.DataStatistics;
import com.shth.spacexifa.page.Page;

import java.math.BigInteger;
import java.util.List;

public interface DataStatisticsService {
	/**
	 * 根据组织机构id获取项目部焊机总数
	 * @param page 分页
	 * @param parent 组织机构父id
	 * @return
	 */
	List<DataStatistics> getItemMachineCount(Page page, BigInteger parent);




	/**
	 * 根据组织机构统计焊机
	 * @param page
	 * @param
	 * @return
	 */
	List<DataStatistics> getItemMachineByItemType(WeldDto dto, int itemtype);

	List<DataStatistics> getItemMachineCount(BigInteger parent);

	/**
	 * 获取开机焊机总数
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	int getStartingUpMachineNum(BigInteger itemid,WeldDto dto);

	/**
	 * 获取焊机任务数
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getweldernameCount(WeldDto dto,String tasktime);

	/**
	 * 获取设备利用率
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getMachineRadio(WeldDto dto, int itemtype);

	/**
	 * 获取参数
	 * @return
	 */
	DataStatistics getParameter();

	/**
	 * 获取工作的焊机数
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	DataStatistics getWorkMachineNum(BigInteger itemid,WeldDto dto);

	/**
	 * 获取工作的焊口数
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	DataStatistics getWorkJunctionNum(BigInteger itemid,WeldDto dto);

	DataStatistics getWorkJunctionNumByWelder(BigInteger itemid,WeldDto dto);

	/**
	 * 获取开机总时长
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	BigInteger getStaringUpTime(BigInteger itemid,WeldDto dto);
	BigInteger getStaringUpTimeByJunction(BigInteger itemid,WeldDto dto);
	BigInteger getStaringUpTimeByWelder(BigInteger itemid,WeldDto dto);

	/**
	 * 获取工件焊接时长
	 * @param dto 扩展参数类
	 * @return
	 */
	BigInteger getWorkingJunction(WeldDto dto);
	DataStatistics getEleVolByJunction(WeldDto dto);
	BigInteger getStandJunction(WeldDto dto);


	/**
	 * 获取待机总时长
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	BigInteger getStandytime(BigInteger itemid,WeldDto dto);

	BigInteger getStandytimeByWelder(BigInteger itemid,WeldDto dto);

	BigInteger getStandytimeByJunction(BigInteger itemid,WeldDto dto);

	/**
	 * 获取焊接时长，平均电流电压
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	DataStatistics getWorkTimeAndEleVol(BigInteger itemid,WeldDto dto);

	DataStatistics getWorkTimeAndEleVolByWelder(BigInteger itemid,WeldDto dto);

	DataStatistics getWorkTimeAndEleVolByJunction(BigInteger itemid,WeldDto dto);

	/**
	 * 班组焊接时间及焊丝消耗
	 * @param dto
	 * @return
	 */
	DataStatistics getItemWorkTime(BigInteger itemid,WeldDto dto);

	/**
	 * 获取所有的焊机id，编号以及组织机构id，名称
	 * @param page 分页
	 * @param itemid 组织机构id
	 * @return
	 */
	List<DataStatistics> getAllMachine(Page page,BigInteger itemid);

	/**
	 * 获取所有的焊工编号，姓名
	 * @param page
	 * @return
	 */
	List<DataStatistics> getAllWelder(Page page,BigInteger parent,WeldDto dto);

	/**
	 * 人员生产数据导出Excel
	 * @return
	 */
	List<DataStatistics> getAllPersonData(BigInteger parent,WeldDto dto);

	/**
	 * 获取所有悍缝编号及组织机构id，name
	 * @param page 分页
	 * @param junctionno 焊缝编号
	 * @return
	 */
	List<DataStatistics> getAllJunction(Page page,String junctionno);

	/**
	 * 获取所有项目部组织机构
	 * @return
	 */
	List<DataStatistics> getAllInsframe(BigInteger parent);

	/**
	 * 获取组织机构累计焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldItemInCount(Page page, WeldDto dto);

	/**
	 * 获取组织机构超规范焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldItemOutCount(Page page, WeldDto dto);

	/**
	 * 获取焊机累计焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldMachineInCount(Page page, WeldDto dto ,BigInteger itemid);

	/**
	 * 获取焊机超规范焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldMachineOutCount(Page page, WeldDto dto ,BigInteger itemid);

	/**
	 * 获取焊工累计焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldPersonInCount(Page page, WeldDto dto);

	/**
	 * 根据焊缝id查询工艺信息表
	 */
	DataStatistics getmaxele(BigInteger junction_id);

	/**
	 * 获取焊工超规范焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldPersonOutCount(Page page, WeldDto dto);

	/**
	 * 获取工件累计焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldPieceInCount(Page page, WeldDto dto,String junctionno);

	/**
	 * 获取工件超规范焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldPieceOutCount(Page page, WeldDto dto,String junctionno);

	/**
	 * 获取焊机故障
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @param value 故障类型id
	 * @return
	 */
	List<DataStatistics> getFauit(Page page,WeldDto dto,int value);

	/**
	 * 获取焊机故障
	 * @param dto 扩展参数类
	 * @param value 故障类型id
	 * @return
	 */
	List<DataStatistics> getFauit(WeldDto dto,int value);

	/**
	 * 获取焊机故障明细
	 * @param page 分页
	 * @param dto 扩展dto类
	 * @param id 焊机id
	 * @param value 故障类型id
	 * @return
	 */
	List<DataStatistics> getFauitDetail(Page page,WeldDto dto,BigInteger id,int value);

	/**
	 * 班组生产数据导出Excel
	 * @param
	 * @return
	 */
	List<DataStatistics> getAllItemData();

	/**
	 * 设备生产数据导出Excel
	 * @param itemid
	 * @return
	 */
	List<DataStatistics> getAllMachineData(BigInteger itemid);



	/**
	 * 工件生产数据导出Excel
	 * @param
	 * @return
	 */
	List<DataStatistics> getAllJunctionData(String junctionno);

	/**
	 * 班组累计焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldItemInCountData(WeldDto dto);

	/**
	 * 班组超规范焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldItemOutCountData(WeldDto dto);

	/**
	 * 设备累计焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldMachineInCountData(WeldDto dto,BigInteger itemid);

	/**
	 * 设备超规范焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldMachineOutCountData(WeldDto dto,BigInteger itemid);


	/**
	 * 人员累计焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldPersonInCountData(WeldDto dto);

	/**
	 * 人员超规范焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldPersonOutCountData(WeldDto dto);

	/**
	 * 工件累计焊接时长
	 * @param dto
	 * @param
	 * @return
	 */
	List<DataStatistics> getWeldWorkpieceInCountData(WeldDto dto, String junctionno);

	/**
	 * 工件超规范焊接时长
	 * @param dto
	 * @param
	 * @return
	 */
	List<DataStatistics> getWeldWorkpieceOutCountData(WeldDto dto, String junctionno);

	/**
	 * 焊工工作量排行（焊接时长）
	 * @param parent 事业部id
	 * @param time 时间
	 * @return
	 */
	List<DataStatistics> getWorkRank(Page page,BigInteger parent,String time,String time2);

	/**
	 * 工区或班组焊工工作量排行（焊接时长）
	 * @param parent 事业部id
	 * @param time 时间
	 * @return
	 */
	List<DataStatistics> getWelderWorkTime(BigInteger parent,String time);

	/**
	 * 工区或班组焊工工作量排行（焊接时长）
	 * @param parent 事业部id
	 * @param time 时间
	 * @return
	 */
	List<DataStatistics> getPersonWorkTime(BigInteger parent,String time);

	/**
	 * 焊工工作时长排行（待机时长）
	 * @param page
	 * @param parent
	 * @param time
	 * @return
	 */
	List<DataStatistics> getStandbyRank(Page page,BigInteger parent,String time);

	/**
	 * 获取用户当前组织机构下的焊丝消耗量和气体消耗量
	 * @param userInsframework
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWireAndFlow(BigInteger userInsframework, WeldDto dto);

	/**
	 * 获取用户当前组织机构下的开机和焊接的焊机数以及总焊机数
	 * @param userInsframework
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getEquipmentUtilize(BigInteger userInsframework, WeldDto dto);

	/**
	 * 获取用户当前组织机构下的开机时间和焊接时间
	 * @param userInsframework
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getOnAndWeldTime(BigInteger userInsframework, WeldDto dto);

	/**
	 * 获取工作的焊机数
	 * @param itemid
	 * @param time
	 * @return
	 */
	DataStatistics getWorkMachineCount(BigInteger itemid, String time);

	/**
	 * 获取项目部正常工作时长
	 * @param dto 事业部id，起始时间，结束时间
	 * @return
	 */
	List<DataStatistics> getItemWeldTime(WeldDto dto);

	/**
	 * 获取项目部超标工作时长
	 * @param dto 事业部id，起始时间，结束时间
	 * @return
	 */
	List<DataStatistics> getItemOverProofTime(WeldDto dto);

	/**
	 * 获取生产任务详情
	 * @param parent 组织机构id
	 * @param welderno 焊机编号
	 * @param taskno 任务编号
	 * @param time1 开始时间
	 * @param time2 结束时间
	 * @return
	 */
	List<DataStatistics> getTaskDetail(BigInteger parent,String welderno,String taskno,String time1,String time2);

	/**
	 * 获取所有任务
	 * @param page 分页
	 * @param parent 组织机构id
	 * @param welderno 焊机编号
	 * @param taskno 任务编号
	 * @param time1 开始时间
	 * @param time2 结束时间
	 * @return
	 */
	List<DataStatistics> getTask(Page page,BigInteger parent,String welderno,String taskno,String time1,String time2);
	List<DataStatistics> getTask(BigInteger parent,String welderno,String taskno,String time1,String time2);

	/**
	 * 获取焊机任务表
	 * @param page
	 * @param parent 归属id
	 * @param sql 时间查询sql
	 * @param type 分配状态 0：所有，1：未分配，2：已分配
	 * @return
	 */
	List<DataStatistics> getMachineTask(Page page,BigInteger parent,String sql,int type);
	List<DataStatistics> getMachineTask(BigInteger parent,String sql,int type);

	String getDay(String time,String time2);

	/**
	 *
	 * @param
	 * @param
	 * @return
	 */
	List<DataStatistics> getMachineNoTask(BigInteger insid, String time1, String totime, String time2);

	/**
	 * 不同组织机构设备故障次数
	 * @return
	 */
	List<DataStatistics> getWarnTimes(BigInteger itemid, WeldDto dto);

	/**
	 * 获取用户当前组织机构下的开机时间和焊接时间
	 * @param userInsframework
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getTaskDetails(BigInteger userInsframework, WeldDto dto);

	/**
	 * 查询历史数据
	 * @Description
	 * @author Bruce
	 * @date 2020年4月2日下午2:28:00
	 * @param str
	 * @param filed
	 * @return
	 */
	List<DataStatistics> getHistoryData(String str, String filed);

	/**
	 * 班组生产数据报表
	 * @param
	 * @param dto 时间
	 * @return
	 */
	List<DataStatistics> getMachineData(Page page, BigInteger insid, WeldDto dto);

	/**
	 * 查询人均工作时长(焊接时长)
	 */
	List<DataStatistics> findAverageWorkingTime(String time,int itemType);

	/**
	 * 查询人均工作时长(待机时长)
	 */
	List<DataStatistics> findStandbyWorkingTime(String time,int itemType);

	/**
	 * 根据组织机构分组统计焊接规范符合率
	 */
	List<DataStatistics> findLoadRateList(String startTime,int itemType);

	/**
	 * 根据组织机构查找焊材
	 */
	List<DataStatistics> getWireMatral(String time1,int itemType);

	/**
	 * 查询焊工焊材消耗量排行
	 */
	List<DataStatistics> findWelderMaterialConsume(String startTime);

	List<DataStatistics> findSupergageInfo(String startTime);

	List<DataStatistics> findSupergageCumulativeNumber(String startTime,int itemType);

	List<DataStatistics> findJobSetNormRate(String startTime);

	/**
	 * 根据组织机构分别统计焊工数量
	 * @return
	 */
	List<DataStatistics> countWelderNumByIid();
}
