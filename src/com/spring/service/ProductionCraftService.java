package com.spring.service;

import com.spring.model.ProductionCraft;
import com.spring.page.Page;
import java.util.List;

public interface ProductionCraftService {
    List<ProductionCraft> findProductionCraftList(Page page,String searchStr);

    int addProductionCraft(ProductionCraft productionCraft);

    int updateProductionCraft(ProductionCraft productionCraft);

    int deleteProductionCraft(ProductionCraft productionCraft);
}
