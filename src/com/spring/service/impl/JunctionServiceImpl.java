package com.spring.service.impl;

import com.github.pagehelper.PageHelper;
import com.spring.dao.JunctionMapper;
import com.spring.model.Junction;
import com.spring.page.Page;
import com.spring.service.JunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class JunctionServiceImpl implements JunctionService {

    @Autowired
    JunctionMapper junctionMapper;


    @Override
    public List<Junction> getJunctionList(Page page, Junction junction) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        return junctionMapper.getJunctionList(junction);
    }

    @Override
    public int addJunction(Junction junction) {
        return junctionMapper.addJunction(junction);
    }

    @Override
    public int updateJunction(Junction junction) {
        return junctionMapper.updateJunction(junction);
    }

    @Override
    public int deleteJunction(int fid) {
        return junctionMapper.deleteJunction(fid);
    }

}
