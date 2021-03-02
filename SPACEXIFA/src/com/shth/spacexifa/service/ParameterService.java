package com.shth.spacexifa.service;

import java.math.BigInteger;
import java.util.List;

import com.shth.spacexifa.model.Parameter;

public interface ParameterService {
	List<Parameter> getAllParameter();
	void UpdateParameter(Parameter para);
	List<Parameter> getParameter();
	void UpdateNumVersion(BigInteger num);

	//查询超规范信息是否展示
	Parameter getParameterBySupergage();
	//修改超规范信息展示状态
	int updateParameterBySupergage(Parameter parameter);
}
