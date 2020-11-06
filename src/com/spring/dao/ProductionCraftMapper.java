package com.spring.dao;

import com.spring.model.Junction;
import com.spring.model.ProductionCraft;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

public interface ProductionCraftMapper {

    List<ProductionCraft> findProductionCraftList(@Param("searchStr") String searchStr);

    int addProductionCraft(ProductionCraft productionCraft);

    int updateProductionCraft(ProductionCraft productionCraft);

    int deleteProductionCraft(ProductionCraft productionCraft);

    List<ProductionCraft> getLiarbryJunctionById(ProductionCraft productionCraft);

    int addLiarbryJunction(ProductionCraft productionCraft);

    List<ProductionCraft> getLibraryJunction(BigInteger TRACKINGCARD_ID);

    int deleteLibraryJunctionByTRACKINGCARD_ID(BigInteger TRACKINGCARD_ID);
}
