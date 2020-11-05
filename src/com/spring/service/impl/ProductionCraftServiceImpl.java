package com.spring.service.impl;

import com.spring.dao.ProductionCraftMapper;
import com.spring.model.ProductionCraft;
import com.spring.page.Page;
import com.spring.service.ProductionCraftService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ProductionCraftServiceImpl implements ProductionCraftService {
    @Resource
    private ProductionCraftMapper mapper;

    @Override
    public List<ProductionCraft> findProductionCraftList(Page page,String searchStr) {
        return mapper.findProductionCraftList(searchStr);
    }

    @Override
    public int addProductionCraft(ProductionCraft productionCraft) {
        return mapper.addProductionCraft(productionCraft);
    }

    @Override
    public int updateProductionCraft(ProductionCraft productionCraft) {
        return mapper.updateProductionCraft(productionCraft);
    }

    @Override
    public int deleteProductionCraft(ProductionCraft productionCraft) {
        return mapper.deleteProductionCraft(productionCraft);
    }
}
