package com.shth.spacexifa.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shth.spacexifa.dao.ParameterMapper;
import com.shth.spacexifa.model.Parameter;
import com.shth.spacexifa.service.ParameterService;

@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class ParameterServiceImpl implements ParameterService {

    @Resource
    private ParameterMapper mapper;

    @Override
    public List<Parameter> getAllParameter() {
        // TODO Auto-generated method stub
        return mapper.getAllParameter();
    }

    @Override
    public void UpdateParameter(Parameter para) {
        // TODO Auto-generated method stub
        mapper.UpdateParameter(para);
    }

    @Override
    public void UpdateNumVersion(BigInteger num) {
        // TODO Auto-generated method stub
        mapper.UpdateNumVersion(num);
    }

    @Override
    public List<Parameter> getParameter() {
        return mapper.getParameter();
    }
}
