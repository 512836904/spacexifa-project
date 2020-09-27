package com.spring.service;

import com.spring.model.Junction;
import com.spring.page.Page;
import org.apache.ibatis.annotations.Param;

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
     * 新增工艺信息和焊缝关联记录
     */
    int addLiarbryJunction(int library_id,List<Integer> junction_id);

    /**
     * 批量删除工艺信息和焊缝关联记录
     */
    int deleteLiarbryJunctionByIds(List<Integer> ids);

}
