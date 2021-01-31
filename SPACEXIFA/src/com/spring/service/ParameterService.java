package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import com.spring.model.DataStatistics;
import com.spring.model.Parameter;

public interface ParameterService {
	List<Parameter> getAllParameter();
	void UpdateParameter(Parameter para);
	List<Parameter> getParameter();
	void UpdateNumVersion(BigInteger num);
}
