package com.spring.service;

import com.spring.model.ProductionCraft;
import com.spring.page.Page;

import java.math.BigInteger;
import java.util.List;

public interface ProductionCraftService {
    List<ProductionCraft> findProductionCraftList(Page page,String searchStr);

    int addProductionCraft(ProductionCraft productionCraft);

    int updateProductionCraft(ProductionCraft productionCraft);

    int deleteProductionCraft(ProductionCraft productionCraft);

    int addLiarbryJunction(BigInteger TRACKINGCARD_ID, BigInteger PRODUCTION_ID,BigInteger JUNCTION_ID);

    List<ProductionCraft> getLibraryJunction(BigInteger TRACKINGCARD_ID);

    int deleteLibraryJunctionByTRACKINGCARD_ID(BigInteger TRACKINGCARD_ID);
}
