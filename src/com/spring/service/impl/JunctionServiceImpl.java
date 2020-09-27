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

    @Override
    public int addLiarbryJunction(int library_id, List<Integer> junction_ids) {
        int i = 0;
        if (null != junction_ids && junction_ids.size() > 0 && library_id != 0){
            for (Integer junction_id: junction_ids){
                //根据工艺id和焊缝id查询关联表
                List<Junction> junctionList = junctionMapper.getLiarbryJunctionById(library_id, junction_id);
                if (junctionList.size() != 0){
                    //已经存在关联记录，不再增加
                }else {
                    Junction junction = new Junction();
                    junction.setLibrary_id(library_id);
                    junction.setJunction_id(junction_id);
                    int i1 = junctionMapper.addLiarbryJunction(junction);
                    if (i1 != 0){
                        i = i1;
                    }
                }
            }
        }
        return i;
    }

    @Override
    public int deleteLiarbryJunctionByIds(List<Integer> ids) {
        return junctionMapper.deleteLiarbryJunctionByIds(ids);
    }
}
