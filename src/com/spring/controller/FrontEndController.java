package com.spring.controller;

import com.github.pagehelper.PageInfo;
import com.spring.dto.WeldDto;
import com.spring.model.DataStatistics;
import com.spring.model.Wps;
import com.spring.page.Page;
import com.spring.service.DataStatisticsService;
import com.spring.service.InsframeworkService;
import com.spring.service.LiveDataService;
import com.spring.service.WpsService;
import com.spring.util.IsnullUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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
    private static final SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");

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
        page = new Page(pageIndex, pageSize, total);
        BigInteger parent = null;
        try {
            String time1 = "";
            time1 = request.getParameter("startTime");

            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            }else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            List<DataStatistics> list = dss.getWorkRank(page, parent, time1);
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
                json.put("welderno", "");   //焊工编号
                json.put("name", "");   //焊工姓名
                json.put("item", "");   //组织机构
                json.put("hour", "0");
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("rows", ary);
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
        JSONObject json = new JSONObject();
        int itemtype = 22;
        String startTime = request.getParameter("startTime");
        String organization = request.getParameter("organization");
        if (null == startTime || "".equals(startTime) || "0".equals(startTime)) {
            startTime = sdfDay.format(System.currentTimeMillis());//当日
        } else {
            startTime = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
        }
        if ("0".equals(organization)){
            itemtype = 22;
        }else {
            itemtype = 23;  //班组
        }
        try {
            List<DataStatistics> loadRateList = dss.findLoadRateList(startTime, itemtype);
            if (null != loadRateList && loadRateList.size() > 0) {
                for (DataStatistics data : loadRateList) {
                    json.put("id", data.getId());//组织id
                    json.put("itemname", data.getName());
                    //规范符合率 = 焊接时长 / （焊接时长+超规范时长）
                    if (data.getWkhour() != 0 || data.getWnhour() != 0) {
                        json.put("hour", data.getWkhour()/(data.getWkhour()+data.getWnhour())*100);
                    } else {
                        json.put("hour", 0);
                    }
                    ary.add(json);
                }
            } else {
                json.put("id", "");
                json.put("itemname", "");
                json.put("hour", 0);
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("ary", ary);
        return obj.toString();
    }

    public static void main(String[] args) {
        BigDecimal bd = null;
        double aaa = 7.5336;
        int bbb = 2;
        bd = new BigDecimal(aaa / bbb);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        System.out.println(bd);
    }

    /**
     * 查询焊工人均工作时间（h）
     */
    @RequestMapping("/findAverageWorkingTime")
    @ResponseBody
    public String findAverageWorkingTime(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        String startTime = request.getParameter("startTime");
        String organization = request.getParameter("organization");
        int itemtype = 22;
        try {
            if (null == startTime || "".equals(startTime) || "0".equals(startTime)) {
                startTime = sdfDay.format(System.currentTimeMillis());  //当日
            } else {
                startTime = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            if ("0".equals(organization)){
                itemtype = 22;
            }else {
                itemtype = 23;  //班组
            }
            List<DataStatistics> list = dss.findAverageWorkingTime(startTime,itemtype);
            if (null != list && list.size() > 0) {
                for (DataStatistics data : list) {
                    json.put("id", data.getId());       //组织机构id
                    json.put("name", data.getName());   //组织机构名称
                    json.put("hour", data.getHour());   //总时长（h）
                    json.put("num", data.getNum());     //总人数
                    BigDecimal bd = null;
                    if (data.getNum() != 0 && data.getHour() != null) {
                        bd = new BigDecimal(data.getHour() / data.getNum());
                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                        json.put("rate", bd);     //人均时间
                    } else {
                        json.put("rate", 0.01);     //人均时间
                    }
                    ary.add(json);
                }
            } else {
                json.put("id", "");
                json.put("name", "");
                json.put("hour", "0");
                json.put("num", "0");
                json.put("rate", 0.01);
                ary.add(json);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();
        obj.put("ary", ary);
        return obj.toString();
    }

    /**
     * 查询工区电能、气体消耗量 ( KWH、L )
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
        String time1 = request.getParameter("startTime");
        String organization = request.getParameter("organization");
        page = new Page(pageIndex, pageSize, total);
        JSONObject obj = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject title = new JSONObject();
        WeldDto dto = new WeldDto();
        JSONArray titleary = new JSONArray();
        int itemtype = 22;//默认工区
        long total = 0;
        try {
            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            }else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            dto.setDtoTime1(time1);
            if ("0".equals(organization)){
                itemtype = 22;
            }else {
                itemtype = 23;  //班组
            }
            List<DataStatistics> list = dss.getItemMachineByItemType(page, itemtype); //根据组织机构统计焊机总数
            if (list != null) {
                PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
                total = pageinfo.getTotal();
            }
            for (DataStatistics i : list) {
                json.put("t0", i.getName());//所属班组
                //json.put("t1", i.getTotal());//设备总数
                //int machinenum = 0;
                //BigInteger starttime = null;
                DataStatistics weldtime = null;
                //DataStatistics junction = dss.getWorkJunctionNum(i.getId(), dto);//获取工作(焊接)的焊口数
                DataStatistics parameter = dss.getParameter();//获取参数
                BigInteger standytime = null;

                //machinenum = dss.getStartingUpMachineNum(i.getId(), dto);//获取开机焊机总数
                //starttime = dss.getStaringUpTime(i.getId(), dto);//获取开机总时长
               // json.put("t2", machinenum);//开机设备数
               // json.put("t7", getTimeStrBySecond(starttime));//工作时间
                standytime = dss.getStandytime(i.getId(), dto);//获取待机总时长
                weldtime = dss.getWorkTimeAndEleVol(i.getId(), dto);//获取焊接时长，平均电流电压(根据时间和组织id查询)
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

//                if (junction.getJunctionnum() != 0) {
//                    json.put("t5", junction.getJunctionnum());//焊接焊缝数
//                } else {
//                    json.put("t5", 0);
//                }
                if (i.getTotal() != 0 && weldtime != null) {
//                    DataStatistics machine = dss.getWorkMachineNum(i.getId(), dto);//获取工作(焊接)的焊机数
//                    if (machine != null && junction != null) {
//                        json.put("t3", machine.getMachinenum());//实焊设备数
//                        json.put("t6", getTimeStrBySecond(weldtime.getWorktime()));//焊接时间
//                        double weldingproductivity = (double) Math.round(weldtime.getWorktime().doubleValue() / starttime.doubleValue() * 100 * 100) / 100;
//                        if (i.getTotal() == 0) {
//                            json.put("t4", 0);//设备利用率 = 开机/总台数
//                        } else {
//                            double useratio = (double) Math.round(Double.valueOf(machinenum) / Double.valueOf(i.getTotal()) * 100 * 100) / 100;
//                            json.put("t4", useratio);//设备利用率 = 开机/总台数
//                        }
//                        json.put("t8", weldingproductivity);//焊接效率
//                    }
                    if (parameter != null) {
                        double wtime = weldtime.getWorktime().doubleValue() / 60;
//                        String[] str = parameter.getWireweight().split(",");
//                        double wireweight = Double.valueOf(str[0]);
//                        double wire = 0;
//                        if (weldtime != null) {
//                            wire = (double) Math.round(weldtime.getWirefeedrate() * 1000);
//                        }
                        double air = (double) Math.round(parameter.getAirflow() * wtime * 100) / 100;//气体消耗量=气体流量*焊接时间
                        //json.put("t9", wire);   //焊丝消耗量
                        json.put("t11", air);   //气体消耗量
                    }
                } else {
                    //json.put("t3", 0);//实焊设备数
                   // json.put("t6", "00:00:00");//焊接时间
                    //json.put("t4", 0);//设备利用率
                    //json.put("t8", 0);//焊接效率
                    //json.put("t9", 0);//焊丝消耗
                    json.put("t11", 0);//气体消耗
                }
                ary.add(json);
            }
            //表头
//            String[] str = {"所属班组", "设备总数", "开机设备数", "实焊设备数", "设备利用率(%)", "焊接焊缝数", "焊接时间", "工作时间", "焊接效率(%)", "焊丝消耗(G)", "电能消耗(KWH)", "气体消耗(L)"};
            String[] str = {"所属班组", "电能消耗(KWH)", "气体消耗(L)"};
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

    /**
     * 焊丝消耗量
     * @param request
     * @return
     */
    @RequestMapping("/findWireConsumption")
    @ResponseBody
    public String findWireConsumption(HttpServletRequest request) {
        if (iutil.isNull(request.getParameter("page"))) {
            pageIndex = Integer.parseInt(request.getParameter("page"));
        }
        if (iutil.isNull(request.getParameter("rows"))) {
            pageSize = Integer.parseInt(request.getParameter("rows"));
        }
        String time1 = request.getParameter("startTime");
        int itemtype = 22;
        String organization = request.getParameter("organization");
        page = new Page(pageIndex, pageSize, total);
        JSONObject obj = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject title = new JSONObject();
        WeldDto dto = new WeldDto();
        JSONArray titleary = new JSONArray();
        long total = 0;
        try {
            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            }else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            if ("0".equals(organization)){
                itemtype = 22;
            }else {
                itemtype = 23;  //班组
            }
            dto.setDtoTime1(time1);
            List<DataStatistics> list = dss.getItemMachineByItemType(page, itemtype); //根据组织机构统计焊机总数
            if (list != null) {
                PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
                total = pageinfo.getTotal();
            }
            for (DataStatistics i : list) {
                json.put("t0", i.getName());//所属班组
                DataStatistics weldtime = dss.getWorkTimeAndEleVol(i.getId(), dto);//获取焊接时长，平均电流电压(根据时间和组织id查询)
                if (weldtime != null) {
                    double wire = 0;
                    wire = (double) Math.round(weldtime.getWirefeedrate() * 1000);
                    json.put("t9", wire);   //焊丝消耗量
                } else {
                    json.put("t9", 0);//焊丝消耗
                }
                ary.add(json);
            }
            //表头
            String[] str = {"所属班组", "焊丝消耗(G)"};
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

    /**
     * 设备利用率
     * @param request
     * @return
     */
    @RequestMapping("/findEquipmentUseRatio")
    @ResponseBody
    public String findEquipmentUseRatio(HttpServletRequest request) {
        if (iutil.isNull(request.getParameter("page"))) {
            pageIndex = Integer.parseInt(request.getParameter("page"));
        }
        if (iutil.isNull(request.getParameter("rows"))) {
            pageSize = Integer.parseInt(request.getParameter("rows"));
        }
        String time1 = request.getParameter("startTime");
        int itemtype = 22;
        String organization = request.getParameter("organization");
        page = new Page(pageIndex, pageSize, total);
        JSONObject obj = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject title = new JSONObject();
        WeldDto dto = new WeldDto();
        JSONArray titleary = new JSONArray();
        long total = 0;
        try {
            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            }else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            if ("0".equals(organization)){
                itemtype = 22;
            }else {
                itemtype = 23;  //班组
            }
            dto.setDtoTime1(time1);
            List<DataStatistics> list = dss.getItemMachineByItemType(page, itemtype); //根据组织机构统计焊机总数
            if (list != null) {
                PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
                total = pageinfo.getTotal();
            }
            for (DataStatistics i : list) {
                json.put("t0", i.getName());//所属班组
                int machinenum = 0;
                machinenum = dss.getStartingUpMachineNum(i.getId(), dto);//获取开机焊机总数
                if (i.getTotal() != 0) {
                    DataStatistics machine = dss.getWorkMachineNum(i.getId(), dto);//获取工作(焊接)的焊机数
                    if (i.getTotal() == 0) {
                        json.put("t4", 0);//设备利用率 = 开机/总台数
                    } else {
                        double useratio = Math.round(machinenum / i.getTotal() * 100);
                        json.put("t4", useratio);//设备利用率 = 开机/总台数
                    }
                } else {
                    json.put("t4", 0);//设备利用率
                }
                ary.add(json);
            }
            //表头
            String[] str = {"所属班组", "设备利用率(%)"};
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
//        String time1 = request.getParameter("startTime");
        String time1 = "2020-08-25";
        try {
            if (null == time1 || "".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            }
            dto.setDtoTime1(time1);
            List<Wps> list = wpsService.findJobSetNumber(page, dto);
            if (null != list && list.size() > 0) {
                for (Wps li : list) {
                    json.put("JOB_NUMBER", li.getJOB_NUMBER());
                    json.put("SET_NUMBER", li.getSET_NUMBER());
                    json.put("PART_NAME", li.getPART_NAME());
                    json.put("wirefeedrate", li.getWirefeedrate().toString());
                    json.put("worktime", li.getWorktime().toString());
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
