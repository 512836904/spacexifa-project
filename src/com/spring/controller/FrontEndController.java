package com.spring.controller;

import com.github.pagehelper.PageInfo;
import com.spring.dto.ModelDto;
import com.spring.dto.WeldDto;
import com.spring.model.DataStatistics;
import com.spring.model.LiveData;
import com.spring.model.WeldedJunction;
import com.spring.model.Wps;
import com.spring.page.Page;
import com.spring.service.*;
import com.spring.util.IsnullUtil;
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
    private Page page;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int total = 0;
    IsnullUtil iutil = new IsnullUtil();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private DataStatisticsService dss;
    @Autowired
    private LiveDataService ls;
    @Autowired
    private InsframeworkService im;
    @Autowired
    private WpsService wpsService;

    /**
     * 焊工工作时间排行
     */
    @RequestMapping("/getWorkRank")
    @ResponseBody
    public String getWpsList(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject json = new JSONObject();
        if (iutil.isNull(request.getParameter("page"))) {
            pageIndex = Integer.parseInt(request.getParameter("page"));
        }
        if (iutil.isNull(request.getParameter("rows"))) {
            pageSize = Integer.parseInt(request.getParameter("rows"));
        }
        String time1 = request.getParameter("dtoTime1");
        String time2 = request.getParameter("dtoTime2");
        page = new Page(pageIndex, pageSize, total);
        try {
            BigInteger parent = null;
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<DataStatistics> list = dss.getWorkRank(page, parent, sdf.format(date));
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

    /**
     * 焊接规范符合率
     *
     * @param request
     * @return
     */
    @RequestMapping("/getLoadRate")
    @ResponseBody
    public String getLoadRate(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONArray timeary = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject timejson = new JSONObject();
        try {
//            String parentid = request.getParameter("parent");
            String time1 = request.getParameter("time1");
            String time2 = request.getParameter("time2");
            WeldDto dto = new WeldDto();
            BigInteger parent = null;
            if (null != time1 && !"".equals(time1)) {
                dto.setDtoTime1(time1.substring(0, 10));
            }
            if (null != time2 && !"".equals(time2)) {
                dto.setDtoTime2(time2.substring(0, 10));
            }
            dto.setDay("day");
            List<DataStatistics> ilist = dss.getItemWeldTime(dto);  //正常焊接时长
            List<DataStatistics> olist = dss.getItemOverProofTime(dto); //超规范焊接时长
            List<ModelDto> time = ls.getAllTimes(dto);
            List<LiveData> insf = ls.getAllInsf(parent, 23);
            List<DataStatistics> temp = ilist;
            for (int i = 0; i < ilist.size(); i++) {
                double num = 100;
                temp.get(i).setInsname(ilist.get(i).getName());
                temp.get(i).setTime(ilist.get(i).getTime());
                for (int o = 0; o < olist.size(); o++) {
                    if (ilist.get(i).getId().equals(olist.get(o).getId()) && ilist.get(i).getTime().equals(olist.get(o).getTime())) {
                        num = (double) Math.round(((ilist.get(i).getHour() - olist.get(o).getHour()) / ilist.get(i).getHour()) * 100 * 100) / 100;
                    }
                }
                temp.get(i).setHour(num);
            }
            for (ModelDto t : time) {
                timejson.put("weldtime", t.getWeldTime());//日期
                timeary.add(timejson);
            }
            for (LiveData item : insf) {
                json.put("itemname", item.getFname());//班组
                double[] num = new double[time.size()];
                for (int i = 0; i < time.size(); i++) {
                    num[i] = 0;
                    for (DataStatistics t : temp) {
                        if (time.get(i).getWeldTime().equals(t.getTime()) && item.getFname().equals(t.getName())) {
                            num[i] = t.getHour();
                        }
                    }
                }
                json.put("hour", num);
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("time", timeary);
        obj.put("ary", ary);
        return obj.toString();
    }

    /**
     * 查询焊工人均工作时间（h）
     */
    @RequestMapping("/findAverageWorkingTime")
    @ResponseBody
    public String findAverageWorkingTime(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        List<DataStatistics> list = dss.findAverageWorkingTime();
        if (null != list && list.size() > 0) {
            for (DataStatistics data : list) {
                json.put("id", data.getId());       //组织机构id
                json.put("name", data.getName());   //组织机构名称
                json.put("hour", data.getHour());   //总时长（h）
                json.put("num", data.getNum());     //总人数
                ary.add(json);
            }
        } else {
            json.put("id", "");
            json.put("name", "");
            json.put("hour", "");
            json.put("num", "");
            ary.add(json);
        }
        JSONObject obj = new JSONObject();
        obj.put("ary", ary);
        return obj.toString();
    }

    /**
     * 查询工区电能、气体消耗量 ( KWH、L )、耗材消耗量、设备使用率
     */
    @RequestMapping("/findElectricityAndGas")
    @ResponseBody
    public String findElectricityAndGas(HttpServletRequest request) {
        if (iutil.isNull(request.getParameter("page"))) {
            pageIndex = Integer.parseInt(request.getParameter("page"));
        }
        if (iutil.isNull(request.getParameter("rows"))) {
            pageSize = Integer.parseInt(request.getParameter("rows"));
        }
        String time1 = request.getParameter("dtoTime1");
        String time2 = request.getParameter("dtoTime2");
        page = new Page(pageIndex, pageSize, total);
        JSONObject obj = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject title = new JSONObject();
        WeldDto dto = new WeldDto();
        JSONArray titleary = new JSONArray();
        long total = 0;
        try {
            if (iutil.isNull(time1)) {
                dto.setDtoTime1(time1);
            }
            if (iutil.isNull(time2)) {
                dto.setDtoTime2(time2);
            }
            List<DataStatistics> list = dss.getItemMachineCount(page, im.getUserInsframework()); //焊机总数
            if (list != null) {
                PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
                total = pageinfo.getTotal();
            }
            for (DataStatistics i : list) {
                json.put("t0", i.getName());//所属班组
                json.put("t1", i.getTotal());//设备总数
                int machinenum = 0;
                BigInteger starttime = null;
                DataStatistics weldtime = null;
                DataStatistics junction = dss.getWorkJunctionNum(i.getId(), dto);//获取工作(焊接)的焊口数
                DataStatistics parameter = dss.getParameter();//获取参数
                BigInteger standytime = null;

                machinenum = dss.getStartingUpMachineNum(i.getId(), dto);//获取开机焊机总数
                starttime = dss.getStaringUpTime(i.getId(), dto);//获取开机总时长
                json.put("t2", machinenum);//开机设备数
                json.put("t7", getTimeStrBySecond(starttime));//工作时间
                standytime = dss.getStandytime(i.getId(), dto);//获取待机总时长
                weldtime = dss.getWorkTimeAndEleVol(i.getId(), dto);//获取焊接时长，平均电流电压
                double standytimes = 0, time = 0, electric = 0;
                if (standytime != null) {
                    standytimes = standytime.doubleValue() / 60 / 60;
                }
                if (weldtime != null) {
                    electric = (double) Math.round((weldtime.getWorktime().doubleValue() / 60 / 60 * (weldtime.getElectricity() * 1.25 * weldtime.getVoltage()) / 1000 + standytimes * 1.25 * parameter.getStandbypower() / 1000) * 100) / 100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
                } else {
                    electric = (double) Math.round((time + standytimes * 1.25 * parameter.getStandbypower() / 1000) * 100) / 100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
                }
                json.put("t10", (double) Math.round(electric / 0.8 * 100) / 100);//电能消耗

                if (junction.getJunctionnum() != 0) {
                    json.put("t5", junction.getJunctionnum());//焊接焊缝数
                } else {
                    json.put("t5", 0);
                }
                if (i.getTotal() != 0 && weldtime != null) {
                    DataStatistics machine = dss.getWorkMachineNum(i.getId(), dto);//获取工作(焊接)的焊机数
                    if (machine != null && junction != null) {
                        json.put("t3", machine.getMachinenum());//实焊设备数
                        json.put("t6", getTimeStrBySecond(weldtime.getWorktime()));//焊接时间
                        double useratio = (double) Math.round(Double.valueOf(machinenum) / Double.valueOf(i.getTotal()) * 100 * 100) / 100;
                        double weldingproductivity = (double) Math.round(weldtime.getWorktime().doubleValue() / starttime.doubleValue() * 100 * 100) / 100;
                        json.put("t4", useratio);//设备利用率
                        json.put("t8", weldingproductivity);//焊接效率
                    }
                    if (parameter != null) {
                        double wtime = weldtime.getWorktime().doubleValue() / 60;
                        String[] str = parameter.getWireweight().split(",");
                        double wireweight = Double.valueOf(str[0]);
                        double wire = 0;
                        if (weldtime != null) {
//							wire = (double)Math.round(wireweight*(Integer.valueOf(String.valueOf(weldtime.getWorktime()))*(parameter.getSpeed()/60))*100000)/100; //焊丝消耗量=焊丝长度*焊丝密度
                            //wire = (double)Math.round(wireweight*weldtime.getWirefeedrate()*100000)/100; //焊丝消耗量=焊丝长度*焊丝密度
                            wire = (double) Math.round(weldtime.getWirefeedrate() * 1000);
                        }
                        double air = (double) Math.round(parameter.getAirflow() * wtime * 100) / 100;//气体消耗量=气体流量*焊接时间
                        json.put("t9", wire);//焊丝消耗
                        json.put("t11", air);//气体消耗
                    }
                } else {
                    json.put("t3", 0);//实焊设备数
                    json.put("t6", "00:00:00");//焊接时间
                    json.put("t4", 0);//设备利用率
                    json.put("t8", 0);//焊接效率
                    json.put("t2", 0);//开机设备数
                    json.put("t9", 0);//焊丝消耗
                    json.put("t11", 0);//气体消耗
                }
                ary.add(json);
            }
            //表头
            String[] str = {"所属班组", "设备总数", "开机设备数", "实焊设备数", "设备利用率(%)", "焊接焊缝数", "焊接时间", "工作时间", "焊接效率(%)", "焊丝消耗(G)", "电能消耗(KWH)", "气体消耗(L)"};
            for (int i = 0; i < str.length; i++) {
                title.put("title", str[i]);
                titleary.add(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("total", total);
        obj.put("ary", titleary);
        obj.put("rows", ary);
        return obj.toString();
    }

    public String getTimeStrBySecond(BigInteger timeParam) {
        if (timeParam == null) {
            return "00:00:00";
        }
        BigInteger[] str = timeParam.divideAndRemainder(new BigInteger("60"));//divideAndRemainder返回数组。第一个是商第二个时取模
        BigInteger second = str[1];
        BigInteger minuteTemp = timeParam.divide(new BigInteger("60"));//subtract：BigInteger相减，multiply：BigInteger相乘，divide : BigInteger相除
        if (minuteTemp.compareTo(new BigInteger("0")) > 0) {//compareTo：比较BigInteger类型的大小，大则返回1，小则返回-1 ，等于则返回0
            BigInteger[] minstr = minuteTemp.divideAndRemainder(new BigInteger("60"));
            BigInteger minute = minstr[1];
            BigInteger hour = minuteTemp.divide(new BigInteger("60"));
            if (hour.compareTo(new BigInteger("0")) > 0) {
                return (hour.compareTo(new BigInteger("9")) > 0 ? (hour + "") : ("0" + hour)) + ":" + (minute.compareTo(new BigInteger("9")) > 0 ? (minute + "") : ("0" + minute))
                        + ":" + (second.compareTo(new BigInteger("9")) > 0 ? (second + "") : ("0" + second));
            } else {
                return "00:" + (minute.compareTo(new BigInteger("9")) > 0 ? (minute + "") : ("0" + minute)) + ":"
                        + (second.compareTo(new BigInteger("9")) > 0 ? (second + "") : ("0" + second));
            }
        } else {
            return "00:00:" + (second.compareTo(new BigInteger("9")) > 0 ? (second + "") : ("0" + second));
        }
    }

    /**
     * 查询工作号、部套号信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/findJobSetNumber")
    @ResponseBody
    public String findJobSetNumber(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        if (iutil.isNull(request.getParameter("page"))) {
            pageIndex = Integer.parseInt(request.getParameter("page"));
        }
        if (iutil.isNull(request.getParameter("rows"))) {
            pageSize = Integer.parseInt(request.getParameter("rows"));
        }
        page = new Page(pageIndex, pageSize, total);
        WeldDto dto = new WeldDto();
        Date date = new Date(System.currentTimeMillis());
        dto.setDtoTime1(sdfDay.format(date));
        dto.setDtoTime2(null);
        try {
            List<Wps> list = wpsService.findJobSetNumber(page, dto);
            if (null != list && list.size() > 0) {
                for (Wps li : list) {
                    json.put("JOB_NUMBER", li.getJOB_NUMBER());
                    json.put("SET_NUMBER", li.getSET_NUMBER());
                    json.put("PART_NAME", li.getPART_NAME());
                    json.put("wirefeedrate", li.getWirefeedrate());
                    json.put("worktime", li.getWorktime());
                    ary.add(json);
                }
            } else {
                json.put("JOB_NUMBER", "");  //工作号
                json.put("SET_NUMBER", "");  //部套号
                json.put("PART_NAME", "");  //零件名
                json.put("wirefeedrate", "");  //总耗材消耗
                json.put("worktime", "");   //工作共时长
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("JOB_NUMBER", "");  //工作号
            json.put("SET_NUMBER", "");  //部套号
            json.put("PART_NAME", "");  //零件名
            json.put("wirefeedrate", "");  //总耗材消耗
            json.put("worktime", "");   //工作共时长
            ary.add(json);
        }
        JSONObject obj = new JSONObject();
        obj.put("ary", ary);
        return obj.toString();
    }
}
