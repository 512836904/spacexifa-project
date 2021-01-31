package com.spring.controller;

import com.github.pagehelper.PageInfo;
import com.spring.model.MyUser;
import com.spring.model.ProductionCraft;
import com.spring.page.Page;
import com.spring.service.ProductionCraftService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping(value = "/production", produces = {"text/json;charset=UTF-8"})
public class ProductionCraftController {
    private Page page;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int total = 0;

    @Autowired
    private ProductionCraftService productionCraftService;

    /**
     * 生产工艺库首页
     *
     * @return
     */
    @RequestMapping("/goProductionCraft")
    public String goProductionCraft() {
        return "production/productionCraft";
    }

    @RequestMapping("/getProductionCraftList")
    @ResponseBody
    public String getProductionCraftList(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        page = new Page(pageIndex, pageSize, total);
        String name_search = request.getParameter("name_search");
        long total = 0;
        try {
            pageIndex = Integer.parseInt(request.getParameter("page"));
            pageSize = Integer.parseInt(request.getParameter("rows"));
            List<ProductionCraft> list = productionCraftService.findProductionCraftList(page, name_search);
            if (list != null) {
                PageInfo<ProductionCraft> pageinfo = new PageInfo<ProductionCraft>(list);
                total = pageinfo.getTotal();
            }
            for (ProductionCraft craft : list) {
                json.put("FID", craft.getFID());
                json.put("FNAME", craft.getFNAME());
                json.put("PREHEAT", craft.getPREHEAT());
                json.put("INTERLAMINATION", craft.getINTERLAMINATION());
                json.put("WELDING_MATERIAL", craft.getWELDING_MATERIAL());
                json.put("ELECTRICITY_FLOOR", craft.getELECTRICITY_FLOOR());
                json.put("ELECTRICITY_UPPER", craft.getELECTRICITY_UPPER());
                json.put("VOLTAGE_FLOOR", craft.getVOLTAGE_FLOOR());
                json.put("VOLTAGE_UPPER", craft.getVOLTAGE_UPPER());
                json.put("SOLDER_SPEED_FLOOR", craft.getSOLDER_SPEED_FLOOR());
                json.put("SOLDER_SPEED_UPPER", craft.getSOLDER_SPEED_UPPER());
                json.put("WIDE_SWING", craft.getWIDE_SWING());
                json.put("RESTS", craft.getRESTS());
                json.put("DATA_SOURCES", craft.getDATA_SOURCES());
                json.put("FJUNCTION", craft.getFJUNCTION());//焊缝id
                json.put("JUNCTION_NAME", craft.getJUNCTION_NAME());//焊缝名称
                json.put("WIRE_DIAMETER", craft.getWIRE_DIAMETER());//焊丝直径
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();
        obj.put("total", total);
        obj.put("rows", ary);
        return obj.toString();
    }

    /**
     * 新增生产工艺库
     *
     * @return
     */
    @RequestMapping("/addProduction")
    @ResponseBody
    public String addProduction(@ModelAttribute ProductionCraft productionCraft) {
        JSONObject obj = new JSONObject();
        try {
            if (null != productionCraft) {
                MyUser myuser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                productionCraft.setCREATOR(String.valueOf(myuser.getId()));
                productionCraft.setDATA_SOURCES(new BigInteger("1"));     //数据来源：系统录入
                int i = productionCraftService.addProductionCraft(productionCraft);
                if (i != 0) {
                    obj.put("success", true);
                } else {
                    obj.put("success", false);
                }
            } else {
                obj.put("success", false);
            }
        } catch (Exception e) {
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * 修改生产工艺库
     *
     * @param productionCraft
     * @return
     */
    @RequestMapping("/updateProduction")
    @ResponseBody
    public String updateProduction(@ModelAttribute ProductionCraft productionCraft) {
        JSONObject obj = new JSONObject();
        try {
            if (null != productionCraft) {
                MyUser myuser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                productionCraft.setMENDER(String.valueOf(myuser.getId()));
                int i = productionCraftService.updateProductionCraft(productionCraft);
                if (i != 0) {
                    obj.put("success", true);
                } else {
                    obj.put("success", false);
                }
            } else {
                obj.put("success", false);
            }
        } catch (Exception e) {
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }

    @RequestMapping("/deleteProduction")
    @ResponseBody
    public String deleteProduction(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        ProductionCraft craft = new ProductionCraft();
        try {
            String fid = request.getParameter("fid");
            if (!StringUtils.isEmpty(fid)) {
                craft.setFID(BigInteger.valueOf(Long.valueOf(fid)));
                int i = productionCraftService.deleteProductionCraft(craft);
                if (i != 0) {
                    obj.put("success", true);
                } else {
                    obj.put("success", false);
                }
            } else {
                obj.put("success", false);
            }
        } catch (Exception e) {
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }
}
