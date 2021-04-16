package com.shth.spacexifa.service;

import java.math.BigInteger;
import java.util.List;

import com.shth.spacexifa.model.Person;
import com.shth.spacexifa.page.Page;

public interface PersonService {

	List<Person> findAll(Page page, BigInteger parent,String str);
	List<Person> findAll(BigInteger parent);
	List<Person> findAll(BigInteger parent,String str);
	List<Person> findLeve(int type);
	List<Person> dic();
	List<Person> ins();
	void save(Person welder);
	Person findById(BigInteger fid);
	int getUsernameCount(String welderno);
	void update(Person welder);
	void delete(BigInteger fid);

	/**
	 * 获取所有焊工
	 * @return
	 */
	List<Person> getWelder();
	
	/**
	 * 根据焊工编号获取id
	 * @param welderno
	 * @return
	 */
	Person getIdByWelderno(String welderno);

	/**
	 * 获取所有空闲焊工
	 * @return
	 */
	List<Person> getFreeWelder(Page page,String str);
	
	/**
	 * 获取所有当前组织机构下的焊工
	 * @return
	 */
	List<Person> getWeldername(BigInteger insid);

	/**
	 * 查询所有焊工信息，
	 * 过滤所有的离职焊工
	 * @return
	 */
	List<Person> findAllWelders();

	/**
	 * 根据焊工ID获取组织机构
	 * @param fid
	 * @return
	 */
	String getInsidByFid(BigInteger fid);


}