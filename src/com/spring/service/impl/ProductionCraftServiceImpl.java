package com.spring.service.impl;

import com.spring.dao.ProductionCraftMapper;
import com.spring.model.Junction;
import com.spring.model.ProductionCraft;
import com.spring.page.Page;
import com.spring.service.ProductionCraftService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigInteger;
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

    @Override
    public int addLiarbryJunction(BigInteger TRACKINGCARD_ID, BigInteger PRODUCTION_ID) {
        int i = 0;
        ProductionCraft craft = new ProductionCraft();
        if (null != PRODUCTION_ID && TRACKINGCARD_ID != null){
            //根据工艺id和焊缝id查询关联表
            craft.setTRACKINGCARD_ID(TRACKINGCARD_ID);
            craft.setPRODUCTION_ID(PRODUCTION_ID);
            List<ProductionCraft> junctionList = mapper.getLiarbryJunctionById(craft);
            if (junctionList.size() != 0){
                //已经存在关联记录，不再增加
            }else {
                int i1 = mapper.addLiarbryJunction(craft);
                if (i1 != 0){
                    i = i1;
                }
            }
        }
        return i;
    }

    @Override
    public List<ProductionCraft> getLibraryJunction(BigInteger TRACKINGCARD_ID) {
        return mapper.getLibraryJunction(TRACKINGCARD_ID);
    }

    @Override
    public int deleteLibraryJunctionByTRACKINGCARD_ID(BigInteger TRACKINGCARD_ID) {
        return mapper.deleteLibraryJunctionByTRACKINGCARD_ID(TRACKINGCARD_ID);
    }
}