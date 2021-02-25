package com.shth.spacexifa.service;

import java.math.BigInteger;
import java.util.List;

import com.shth.spacexifa.page.Page;
import com.shth.spacexifa.model.Td;

public interface TdService {

	List<Td> findAll(Page page, String str);
	List<Td> findAllpro(long ins);
	List<Td> findAllcom();
	List<Td> findAlldiv(long ins);
	List<Td> getAllPosition(BigInteger parent,String str);
	List<Td> getMachine(BigInteger mach,BigInteger parent);
	String findweld(String weldid);
	String findInsname(long uid);
	long findInsid(String insname);
	long findIns(long uid);
	String findPosition(String equip);
	List<Td> allWeldname();
	List<Td> getAllMachine(String ins);
	List<Td> getAllPositions(BigInteger parent);
	/**
	 * 获取某天工作、焊接时长
	 * @param time 日期格式年-月-日
	 * @param machineid 焊机id
	 * @return
	 */
	Td getLiveTime(String time,String totime,BigInteger machineid);
	String getBootTime(String time,BigInteger machineId);

	Td countWelderStatusNum();
}