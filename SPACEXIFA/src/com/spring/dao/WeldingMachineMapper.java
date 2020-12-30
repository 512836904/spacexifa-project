package com.spring.dao;

import com.spring.model.WeldingMachine;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigInteger;
import java.util.List;

public interface WeldingMachineMapper extends Mapper<WeldingMachine>{
	List<WeldingMachine> getWeldingMachineAll(@Param("parent") BigInteger parent,@Param("str") String str);
	
	List<WeldingMachine> AllMachine(@Param("wid")BigInteger wid);
	
	List<WeldingMachine> getEquipmentno();
	
	void addWeldingMachine(WeldingMachine wm);
	
	void editWeldingMachine(WeldingMachine wm);
	
	void deleteWeldingMachine(@Param("wid")BigInteger wid);
	
	BigInteger getWeldingMachineByEno(@Param("eno")String eno);
	
	int getEquipmentnoCount(@Param("eno")String eno);
	
	List<WeldingMachine> findAllweldmachine();
	
	int getEquipmentidCount(@Param("eid")String eid);

	int getGatheridCount(@Param("itemid")BigInteger itemid,@Param("gather")String gather);

	WeldingMachine getWeldingMachineById(@Param("wid")BigInteger wid);
	
	void editstatus(@Param("wid")BigInteger wid,@Param("status")int status);
	
	void deleteByInsf(@Param("insfId")BigInteger insfId);
	
	List<WeldingMachine> getWeldingMachineByInsf(@Param("insfId")BigInteger insfId);
	
	BigInteger getIdByGatherid(@Param("gatherid")String gatherid);
	
	void editGatherid(@Param("wid")BigInteger wid);
	
	BigInteger getMachineCountByManu(@Param("mid")BigInteger mid,@Param("id")BigInteger id);
	
	void deleteHistory(@Param("wid")BigInteger wid);
	
	void addfactoryType(WeldingMachine wm);
	
	void deletefactory(@Param("statusId")BigInteger statusId);
	
	List<WeldingMachine> getAllMachine();
	
	List<WeldingMachine> getMachineByIns(@Param("id")BigInteger id);
	
	List<WeldingMachine> getMachines(@Param("insid")BigInteger insid);
	
	List<WeldingMachine> getMachineGather();
	
	List<WeldingMachine> getMachineModel();
	
	void resetGatherMachineid(@Param("machineId")String machineId);
	void updateGather(@Param("machineId")String machineId,@Param("gatherId")String gatherId);

	int findMachineByGatherId(@Param("gather_id") String gather_id);

	List<WeldingMachine> findMachineByFip(@Param("ip")String ip);

	WeldingMachine findMachineByGatherNo(@Param("gather_no") String gather_no);
}