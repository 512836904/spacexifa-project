package com.shth.spacexifa.service;

import com.shth.spacexifa.model.ProductionCraft;
import com.shth.spacexifa.page.Page;

import java.math.BigInteger;
import java.util.List;

public interface ProductionCraftService {
    List<ProductionCraft> findProductionCraftList(Page page,String searchStr);

    int addProductionCraft(ProductionCraft productionCraft);

    int updateProductionCraft(ProductionCraft productionCraft);

    int deleteProductionCraft(ProductionCraft productionCraft);

    int addLiarbryJunction(BigInteger TRACKINGCARD_ID, BigInteger PRODUCTION_ID);

    List<ProductionCraft> getLibraryJunction(BigInteger TRACKINGCARD_ID);

    int deleteLibraryJunctionByTRACKINGCARD_ID(BigInteger TRACKINGCARD_ID);

    /**
     * 根据焊缝id查询工艺信息表
     */
    ProductionCraft findProductionByjid(BigInteger junction_id);

    /**
     * 查询所有工艺名
     * @return
     */
    List<ProductionCraft> findAllProductionName();
}
