package com.shth.spacexifa.service;

import com.shth.spacexifa.model.Junction;
import com.shth.spacexifa.page.Page;

import java.util.List;

public interface JunctionService {

    //查询焊缝信息列表
    List<Junction> getJunctionList(Page page, Junction junction);

    /**
     * 新增焊缝
     * @param junction
     * @return
     */
    int addJunction(Junction junction);

    int updateJunction(Junction junction);

    int deleteJunction(int fid);

    /**
     * 根据焊缝名称查询焊缝信息
     * @param name
     * @return
     */
    List<Junction> findJunctiobByName(String name);

    /**
     * 根据工艺名查询所有焊缝
     * @return
     */
    List<Junction> findJunctionByFname(String fname);

    Junction getJunctionById(Long fid);

    List<Junction> getJunctionAllInfo();
}
