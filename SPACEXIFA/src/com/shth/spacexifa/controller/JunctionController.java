package com.shth.spacexifa.controller;

import com.github.pagehelper.PageInfo;
import com.shth.spacexifa.model.Junction;
import com.shth.spacexifa.model.Parameter;
import com.shth.spacexifa.page.Page;
import com.shth.spacexifa.service.JunctionService;
import com.shth.spacexifa.service.ParameterService;
import com.shth.spacexifa.util.IsnullUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 焊缝管理
 */
@Controller
@RequestMapping(value = "/junction", produces = {"text/json;charset=UTF-8"})
public class JunctionController {

    private Page page;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int total = 0;
    IsnullUtil iutil = new IsnullUtil();

    @Autowired
    JunctionService junctionService;
    @Autowired
    private ParameterService parameterService;

    @RequestMapping("/goJunction")
    public String goJunction(HttpServletRequest request, Model model) {
        return "junction/junction";
    }

    /**
     * 焊缝信息列表
     * @param request
     * @return
     */
    @RequestMapping("/getJunctionList")
    @ResponseBody
    public String getJunctionList(HttpServletRequest request) {
        pageIndex = Integer.parseInt(request.getParameter("page"));
        pageSize = Integer.parseInt(request.getParameter("rows"));
        String search = request.getParameter("searchStr");
        Junction jun = new Junction();
        String junctionSearch = request.getParameter("junctionSearch");
        String junction_name_search = request.getParameter("junction_name_search");
        if (null != junctionSearch && !"".equals(junctionSearch)){
            jun.setFjunction(junctionSearch);
        }
        if (null != junction_name_search && !"".equals(junction_name_search)){
            jun.setJunction_name(junction_name_search);
        }
        page = new Page(pageIndex, pageSize, total);
        List<Junction> junctionList = junctionService.getJunctionList(page,jun);
        long total = 0;
        if (junctionList != null) {
            PageInfo<Junction> pageinfo = new PageInfo<Junction>(junctionList);
            total = pageinfo.getTotal();
        }
       // request.setAttribute("junctionList", junctionList);
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
        for (Junction junction : junctionList){
            json.put("fid" ,junction.getFid());
            json.put("fjunction" ,junction.getFjunction());
            json.put("junction_length" ,junction.getJunction_length());
            json.put("junction_format" ,junction.getJunction_format());
            json.put("current_limit" ,junction.getCurrent_limit());
            json.put("current_lower_limit" ,junction.getCurrent_lower_limit());
            json.put("junction_name" ,junction.getJunction_name());
            json.put("FMAXVOLTAGE" ,junction.getFMAXVOLTAGE());
            json.put("FMINVOLTAGE" ,junction.getFMINVOLTAGE());
            ary.add(json);
        }
        obj.put("total", total);
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/addJunction")
    @ResponseBody
    public String addJunction(HttpServletRequest request){
        JSONObject obj = new JSONObject();
        Junction junction = new Junction();
        try {
            String fjunction = request.getParameter("fjunction");//焊缝编号
            String junction_length = request.getParameter("junction_length");//长度
            String junction_format = request.getParameter("junction_format");//规格
            String current_limit = request.getParameter("current_limit");//电流上限
            String current_lower_limit = request.getParameter("current_lower_limit");//电流下限
            String junction_name = request.getParameter("junction_name");//焊缝名称
            String FMAXVOLTAGE = request.getParameter("FMAXVOLTAGE");//电压上限
            String FMINVOLTAGE = request.getParameter("FMINVOLTAGE");//电压下限

            junction.setFjunction(fjunction);
            junction.setJunction_length(junction_length);
            junction.setJunction_format(junction_format);
            junction.setCurrent_limit(current_limit);
            junction.setCurrent_lower_limit(current_lower_limit);
            junction.setJunction_name(junction_name);
            junction.setFMAXVOLTAGE(FMAXVOLTAGE);
            junction.setFMINVOLTAGE(FMINVOLTAGE);

            int i = junctionService.addJunction(junction);
            if (i != 0){
                obj.put("success", true);
            }else{
                obj.put("success", false);
            }
        }catch (Exception e){
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }

    @RequestMapping("/updateJunction")
    @ResponseBody
    public String updateJunction(HttpServletRequest request){
        JSONObject obj = new JSONObject();
        Junction junction = new Junction();
        try {
            String fid = request.getParameter("fid");//焊缝编号
            String fjunction = request.getParameter("fjunction");//焊缝编号
            String junction_length = request.getParameter("junction_length");//长度
            String junction_format = request.getParameter("junction_format");//规格
            String current_limit = request.getParameter("current_limit");//电流上限
            String current_lower_limit = request.getParameter("current_lower_limit");//电流下限
            String junction_name = request.getParameter("junction_name");//焊缝名称
            String FMAXVOLTAGE = request.getParameter("FMAXVOLTAGE");//电压上限
            String FMINVOLTAGE = request.getParameter("FMINVOLTAGE");//电压下限
            junction.setFid(Long.parseLong(fid));
            junction.setFjunction(fjunction);
            junction.setJunction_length(junction_length);
            junction.setJunction_format(junction_format);
            junction.setCurrent_limit(current_limit);
            junction.setCurrent_lower_limit(current_lower_limit);
            junction.setJunction_name(junction_name);
            junction.setFMAXVOLTAGE(FMAXVOLTAGE);
            junction.setFMINVOLTAGE(FMINVOLTAGE);
            int i = junctionService.updateJunction(junction);
            if (i != 0){
                obj.put("success", true);
            }else{
                obj.put("success", false);
            }
        }catch (Exception e){
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }

    @RequestMapping("/deleteJunction")
    @ResponseBody
    public String deleteJunction(HttpServletRequest request){
        JSONObject obj = new JSONObject();
        try {
            String fid = request.getParameter("fid");//焊缝编号
            int i = junctionService.deleteJunction(Integer.valueOf(fid));
            if (i != 0){
                obj.put("success", true);
            }else{
                obj.put("success", false);
            }
        }catch (Exception e){
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * 查询焊缝所有信息【电流电压上限】不分页
     * @param request
     * @return
     */
    @RequestMapping("/getJunctionAllInfo")
    @ResponseBody
    public String getJunctionAllInfo(HttpServletRequest request){
        JSONObject obj = new JSONObject();
        int supergage_status = 1; //默认展示
        try {
            //查询所有焊缝信息不分页
            List<Junction> junctionAllInfo = junctionService.getJunctionAllInfo();
            //查询超规范信息是否展示
            Parameter parameter = parameterService.getParameterBySupergage();
            if (null != parameter){
                supergage_status = parameter.getSUPERGAGE_STATUS();
            }
            obj.put("junctionAllInfo" ,junctionAllInfo);
            obj.put("supergage_status" ,supergage_status);
            obj.put("success", true);
        }catch (Exception e){
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }
}
