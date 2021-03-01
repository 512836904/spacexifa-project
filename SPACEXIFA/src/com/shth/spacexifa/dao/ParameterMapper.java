package com.shth.spacexifa.dao;

import java.math.BigInteger;
import java.util.List;

import com.shth.spacexifa.model.Parameter;

public interface ParameterMapper {
	List<Parameter> getAllParameter();
	void UpdateParameter(Parameter para);
	void UpdateNumVersion(BigInteger num);
	List<Parameter> getParameter();
}
