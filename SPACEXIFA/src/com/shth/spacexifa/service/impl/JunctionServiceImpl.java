package com.shth.spacexifa.service.impl;

import com.github.pagehelper.PageHelper;
import com.shth.spacexifa.dao.JunctionMapper;
import com.shth.spacexifa.model.Junction;
import com.shth.spacexifa.page.Page;
import com.shth.spacexifa.service.JunctionService;
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

    @Override
    public List<Junction> findJunctiobByName(String name) {
        return junctionMapper.findJunctiobByName(name);
    }

    @Override
    public List<Junction> findJunctionByFname(String fname) {
        return junctionMapper.findJunctionByFname(fname);
    }

    @Override
    public Junction getJunctionById(Long fid) {
        return junctionMapper.getJunctionById(fid);
    }

    @Override
    public List<Junction> getJunctionAllInfo() {
        return junctionMapper.getJunctionAllInfo();
    }
}
