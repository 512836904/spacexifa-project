package com.shth.spacexifa.controller;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shth.spacexifa.model.Parameter;
import com.shth.spacexifa.service.ParameterService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/pmt", produces = {"text/json;charset=UTF-8"})
public class ParameterController {
    @Autowired
    private ParameterService parameterService;

    @RequestMapping("/getParameterAll")
    @ResponseBody
    public String getParameter(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            List<Parameter> findAll = parameterService.getAllParameter();
            for (Parameter pmt : findAll) {
                json.put("id", pmt.getId());
                json.put("folt", pmt.getFolt());
                json.put("fvv", pmt.getFvv());
                json.put("fcn", pmt.getFcn());
                json.put("fst", pmt.getFst());
                json.put("fsft", pmt.getFsft());
                json.put("fct", pmt.getFct());
                json.put("fww", pmt.getFww());
                json.put("fafv", pmt.getFafv());
                json.put("fspeed", pmt.getFspeed());
                json.put("fwc", pmt.getFwc());
                json.put("fsp", pmt.getFsp());
                json.put("fds", pmt.getFds());
                json.put("fas", pmt.getFas());
                json.put("fns", pmt.getFns());
                json.put("numversion", pmt.getNumversion());
                ary.add(json);
            }
            obj.put("success", true);
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/editParameter")
    @ResponseBody
    public String editParameter(HttpServletRequest request) {
        Parameter para = new Parameter();
        JSONObject obj = new JSONObject();
        try {
            String xxx = request.getParameter("id");
            para.setId(new BigInteger(request.getParameter("id")));
            para.setFcn(request.getParameter("companyName"));
//			para.setFvv(Integer.parseInt(request.getParameter("check")));
//			para.setFst(request.getParameter("hour1")+":"+request.getParameter("minute1")+":"+request.getParameter("second1"));
//			para.setFsft(request.getParameter("hour2")+":"+request.getParameter("minute2")+":"+request.getParameter("second2"));
//			para.setFct(request.getParameter("hour3")+":"+request.getParameter("minute3")+":"+request.getParameter("second3"));
//			para.setFolt(new BigInteger(request.getParameter("times")));
//			para.setFww(request.getParameter("one")+","+request.getParameter("two")+","+request.getParameter("six")+","+request.getParameter("eight"));
            para.setFafv(request.getParameter("airflow"));
//			para.setFspeed(request.getParameter("speed"));
//			para.setFwc(request.getParameter("weld"));
//			para.setFsp(request.getParameter("wait"));
//			para.setFds(request.getParameter("day"));
//			para.setFas(request.getParameter("after"));
//			para.setFns(request.getParameter("night"));
            parameterService.UpdateParameter(para);
            obj.put("success", true);
        } catch (Exception e) {
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
        }
        return obj.toString();
    }

    @RequestMapping("/getParameterBySupergage")
    @ResponseBody
    public String getParameterBySupergage(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        int supergage_status = 0;
        BigInteger paramterId = BigInteger.ZERO;
        try {
            Parameter parameter = parameterService.getParameterBySupergage();
            if (null != parameter) {
                supergage_status = parameter.getSUPERGAGE_STATUS();
                paramterId = parameter.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("supergage_status", supergage_status);
        jsonObject.put("paramterId", paramterId);
        return jsonObject.toString();
    }

    public boolean isNotEmpty(String object){
        if (null != object && !"".equals(object)){
            return true;
        }
        return false;
    }

    @RequestMapping("/updateParameterBySupergage")
    @ResponseBody
    public String updateParameterBySupergage(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Parameter parameter = new Parameter();
        int result = 0;
        try {
            //如果超规范信息展示状态为空，默认展示【1】
            String supergage_status = request.getParameter("supergage_status");
            String paramterId = request.getParameter("paramterId");
            if (isNotEmpty(supergage_status) && isNotEmpty(paramterId)) {
                parameter.setSUPERGAGE_STATUS(Integer.parseInt(supergage_status));
            } else {
                parameter.setSUPERGAGE_STATUS(1);
            }
            parameter.setId(BigInteger.valueOf(Long.parseLong(paramterId)));
            result = parameterService.updateParameterBySupergage(parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != 0) {
            jsonObject.put("success", true);
        } else {
            jsonObject.put("success", false);
        }
        return jsonObject.toString();
    }
}