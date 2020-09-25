package com.spring.dao;

import com.spring.model.Junction;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface JunctionMapper extends Mapper<Junction> {

    List<Junction> getJunctionList(Junction junction);

    int addJunction(Junction junction);

    int updateJunction(Junction junction);

    int deleteJunction(@Param("fid") int fid);
}
