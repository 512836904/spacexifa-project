package com.shth.spacexifa.dao;

import com.shth.spacexifa.model.Junction;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface JunctionMapper extends Mapper<Junction> {

    List<Junction> getJunctionList(Junction junction);

    int addJunction(Junction junction);

    int updateJunction(Junction junction);

    int deleteJunction(@Param("fid") int fid);

    List<Junction> findJunctiobByName(@Param("name") String name);

    List<Junction> findJunctionByFname(@Param("fname") String fname);

    Junction getJunctionById(@Param("fid") Long fid);

    List<Junction> getJunctionAllInfo();

//    //根据工艺信息id和焊缝id查询一条记录
//    List<Junction> getLiarbryJunctionById(@Param("library_id") int library_id,@Param("junction_id") int junction_id);
//
//    //新增工艺信息和焊缝关联记录
//    int addLiarbryJunction(Junction junction);
//
//    //批量删除工艺和焊缝关联记录
//    int deleteLiarbryJunctionByIds(List<Integer> ids);
//
//    List<Junction> getListByLibraryId(int library_id);
}
