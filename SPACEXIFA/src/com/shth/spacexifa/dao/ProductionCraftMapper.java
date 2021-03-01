package com.shth.spacexifa.dao;

import com.shth.spacexifa.model.ProductionCraft;
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

    ProductionCraft findProductionByjid(@Param("junction_id") BigInteger junction_id);

    List<ProductionCraft> findAllProductionName();
}
