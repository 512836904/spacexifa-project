package com.spring.service;

import com.spring.model.Junction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JunctionService {

    //查询焊缝信息列表
    List<Junction> getJunctionList(Junction junction);

    /**
     * 新增焊缝
     * @param junction
     * @return
     */
    int addJunction(Junction junction);

    int updateJunction(Junction junction);

    int deleteJunction(int fid);
}
