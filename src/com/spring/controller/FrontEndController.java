package com.spring.controller;

import com.spring.model.DataStatistics;
import com.spring.service.DataStatisticsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * UI接口
 * author:zhushanlong
 */
@Controller
@RequestMapping(value = "/frontEnd", produces = {"text/json;charset=UTF-8"})
public class FrontEndController {

    @Autowired
    private DataStatisticsService dss;

    /**
     * 查询焊工及设备状态
     */
    @RequestMapping("/findWelderAndFacility")
    @ResponseBody
    public String getWpsList(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject json = new JSONObject();
        try {
            String parentid = request.getParameter("parent");
            BigInteger parent = null;
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<DataStatistics> list = dss.getWorkRank(parent, sdf.format(date));
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    json.put("rownum", i + 1);
                    json.put("welderno", list.get(i).getWelderno());
                    json.put("name", list.get(i).getName());
                    json.put("item", list.get(i).getInsname());
                    json.put("hour", (double) Math.round(list.get(i).getHour() * 100) / 100);
                    ary.add(json);
                }
            } else {
                json.put("rownum", "");
                json.put("welderno", "");
                json.put("name", "");
                json.put("item", "");
                json.put("hour", "");
                ary.add(json);
            }
            obj.put("rows", ary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
