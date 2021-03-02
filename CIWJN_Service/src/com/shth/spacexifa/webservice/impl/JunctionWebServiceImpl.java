package com.shth.spacexifa.webservice.impl;

import com.shth.spacexifa.service.WeldedJunctionService;
import com.shth.spacexifa.webservice.JunctionWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JunctionWebServiceImpl implements JunctionWebService {
	@Autowired
	private WeldedJunctionService jws;
	
	@Override
	public Object getWeldedJunctionAll() {
		return jws.getWeldedJunctionAll();
	}

	@Override
	public Object getWeldedJunctionById(String object) {
		return jws.getWeldedJunctionById(object);
	}

	@Override
	public int getWeldedjunctionByNo(String object) {
		return jws.getWeldedjunctionByNo(object);
	}

	@Override
	public boolean addJunction(String object) {
		return jws.addJunction(object);
	}

	@Override
	public boolean updateJunction(String object) {
		return jws.updateJunction(object);
	}

	@Override
	public boolean deleteJunction(String object) {
		return jws.deleteJunction(object);
	}

	@Override
	public Object getTaskResultAll(String object) {
		// TODO Auto-generated method stub
		return jws.getTaskResultAll(object);
	}

	@Override
	public boolean addTaskResult(String object) {
		// TODO Auto-generated method stub
		return jws.addTaskResult(object);
	}

	@Override
	public boolean updateTaskResult(String object) {
		// TODO Auto-generated method stub
		return jws.updateTaskResult(object);
	}

	@Override
	public boolean deleteTaskResult(String object) {
		// TODO Auto-generated method stub
		return jws.deleteTaskResult(object);
	}
	
	@Override
	public boolean giveToServer(String object) {
		// TODO Auto-generated method stub
		return jws.giveToServer(object);
	}
}
