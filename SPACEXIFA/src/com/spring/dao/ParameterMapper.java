package com.spring.dao;

import java.math.BigInteger;
import java.util.List;

import com.spring.model.DataStatistics;
import com.spring.model.Parameter;
import org.apache.ibatis.annotations.Param;

public interface ParameterMapper {
	List<Parameter> getAllParameter();
	void UpdateParameter(Parameter para);
	void UpdateNumVersion(BigInteger num);
	List<Parameter> getParameter();
}
