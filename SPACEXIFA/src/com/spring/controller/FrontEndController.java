package com.spring.controller;

import com.github.pagehelper.PageInfo;
import com.spring.dto.WeldDto;
import com.spring.model.DataStatistics;
import com.spring.model.Welder;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 所有大屏展示相关接口
 */
@Controller
@RequestMapping(value = "/frontEnd", produces = {"text/json;charset=UTF-8"})
public class FrontEndController {
    private Page page;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int total = 0;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");

    @Autowired
    private DataStatisticsService dss;
    @Autowired
    private WpsService wpsService;
    @Autowired
    private WelderService welderService;

    /**
     * 焊工工作时间排行
     */
    @RequestMapping("/getWorkRank")
    @ResponseBody
    public String getWpsList(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject json = new JSONObject();
        page = new Page(pageIndex, pageSize, total);
        BigInteger parent = null;
        try {
            String time1 = "";
            time1 = request.getParameter("startTime");
            String standbyTime = request.getParameter("standbyTime");

            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            } else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            List<DataStatistics> list = null;
            if (null != standbyTime && !"".equals(standbyTime) && "1".equals(standbyTime)){
                //查询待机时长
                list = dss.getStandbyRank(page, parent, time1);
            }else {
                //查询焊接时长
                list = dss.getWorkRank(page, parent, time1);
            }
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
     * 焊接工艺规范符合率
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
        if ("0".equals(organization)) {
            itemtype = 22;
        } else {
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
                        json.put("hour", data.getWkhour() / (data.getWkhour() + data.getWnhour()) * 100);
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
        String standbyTime = request.getParameter("standbyTime");
        int itemtype = 22;
        try {
            if (null == startTime || "".equals(startTime) || "0".equals(startTime)) {
                startTime = sdfDay.format(System.currentTimeMillis());  //当日
            } else {
                startTime = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            if ("0".equals(organization)) {
                itemtype = 22;
            } else {
                itemtype = 23;  //班组
            }
            List<DataStatistics> list = null;
            if (null != standbyTime && !"".equals(standbyTime) && "1".equals(standbyTime)){
                //如果为1，则查询待机焊工的人均工作时长
                list = dss.findStandbyWorkingTime(startTime, itemtype);
            } else {
                list = dss.findAverageWorkingTime(startTime, itemtype);
            }
            //统计组织机构焊工的数量
            List<DataStatistics> dataStatistics = dss.countWelderNumByIid();
            int weldernum = 0; //焊工数量
            if (null != list && list.size() > 0) {
                for (DataStatistics data : list) {
                    json.put("id", data.getId());       //组织机构id
                    json.put("name", data.getName());   //组织机构名称
                    if (null != dataStatistics && dataStatistics.size() > 0) {
                        for (DataStatistics da : dataStatistics) {
                            if (da.getId().equals(data.getId())) {
                                if (data.getType() == 22) {
                                    weldernum = da.getNum(); //工区：焊工数量
                                }
                                if (data.getType() == 23) {
                                    weldernum = da.getTotal();  //班组：焊工数量
                                }
                            }
                        }
                    }
                    BigDecimal bd = null;
                    if (weldernum != 0 && data.getHour() != null && data.getHour() != 0) {
                        bd = new BigDecimal(data.getHour() / weldernum);
                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                        json.put("rate", bd);     //人均时间
                    } else {
                        json.put("rate", 0);    //人均时间
                    }
                    ary.add(json);
                }
            } else {
                json.put("id", "");
                json.put("name", "");
                json.put("hour", "0");
                json.put("num", "0");
                json.put("rate", 0);
                ary.add(json);
            }
        } catch (Exception e) {
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
            } else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            dto.setDtoTime1(time1);
            if ("0".equals(organization)) {
                itemtype = 22;
            } else {
                itemtype = 23;  //班组
            }
            List<DataStatistics> list = dss.getItemMachineByItemType(dto, itemtype); //根据组织机构统计焊机总数
            if (list != null) {
                PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
                total = pageinfo.getTotal();
            }
            for (DataStatistics i : list) {
                json.put("t0", i.getName());//所属班组
                BigInteger standytime = null;
                BigInteger weldtime = null;
                //DataStatistics junction = dss.getWorkJunctionNum(i.getId(), dto);//获取工作(焊接)的焊口数
                DataStatistics parameter = dss.getParameter();//获取参数
                double standytimes = 0, worktimes = 0, electricpower = 0, avgVol = 0, avgEle = 0;
                standytime = i.getStandbytime();//获取待机总时长
                weldtime = i.getWorktime();//获取焊接时长，平均电流电压(根据时间和组织id查询)
                avgVol = i.getVoltage();
                avgEle = i.getElectricity();
                if (standytime != null) {
                    standytimes = standytime.doubleValue() / 60 / 60;
                    if (weldtime != null) {
                        worktimes = weldtime.doubleValue() / 60 / 60;
                        electricpower = (double) Math.round((worktimes * (avgEle * 1.25 * avgVol) / 1000 + standytimes * 1.25 * parameter.getStandbypower() / 1000) * 100) / 100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
                    } else {
                        electricpower = (double) Math.round((standytimes * 1.25 * parameter.getStandbypower() / 1000) * 100) / 100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
                    }
                }
                json.put("t10", (double) Math.round(electricpower / 0.8 * 100) / 100);//电能消耗

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
                        double wtime = weldtime.doubleValue() / 60;
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
//            String[] str = {"所属班组", "电能消耗(KWH)", "气体消耗(L)"};
//            for (int i = 0; i < str.length; i++) {
//                title.put("title", str[i]);
//                titleary.add(title);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    /**
     * 工区焊丝消耗量
     *
     * @param request
     * @return
     */
    @RequestMapping("/findWireConsumption")
    @ResponseBody
    public String findWireConsumption(HttpServletRequest request) {
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
            } else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            if ("0".equals(organization)) {
                itemtype = 22;
            } else {
                itemtype = 23;  //班组
            }
            // dto.setDtoTime1(time1);
            List<DataStatistics> list = dss.getWireMatral(time1, itemtype);
            // List<DataStatistics> list = dss.getItemMachineByItemType(page, itemtype); //根据组织机构统计焊机总
            for (DataStatistics i : list) {
                json.put("t0", i.getName());//所属班组
                json.put("t9", i.getWirefeedrate());//所有车间
                ary.add(json);
            }
            //表头
//            String[] str = {"所属班组", "焊丝消耗(G)"};
//            for (int i = 0; i < str.length; i++) {
//                title.put("title", str[i]);
//                titleary.add(title);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    /**
     * 设备利用率
     *
     * @param request
     * @return
     */
    @RequestMapping("/findEquipmentUseRatio")
    @ResponseBody
    public String findEquipmentUseRatio(HttpServletRequest request) {
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
        double total = 0.0, num = 0.0, hour = 0.0;
        try {
            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            } else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            if ("0".equals(organization)) {
                itemtype = 22;
            } else {
                itemtype = 23;  //班组
            }
            dto.setDtoTime1(time1);
            List<DataStatistics> list = dss.getMachineRadio(dto, itemtype); //根据组织机构统计焊机总数
            for (DataStatistics i : list) {
                json.put("t0", i.getName());//所属班组
                int machinenum = 0;
                total = i.getTotal();
                num = i.getNum();
                hour = i.getHour();
                if (i.getTotal() != 0) {
                    double useratio = Math.round(hour / total * 100);
                    json.put("t4", useratio);//设备利用率 = 开机/总台数
                } else {
                    json.put("t4", 0);//设备利用率
                }
                ary.add(json);
            }
            //表头
//            String[] str = {"所属班组", "设备利用率(%)"};
//            for (int i = 0; i < str.length; i++) {
//                title.put("title", str[i]);
//                titleary.add(title);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        page = new Page(pageIndex, pageSize, total);
        WeldDto dto = new WeldDto();
        String time1 = request.getParameter("startTime");
        try {
            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            } else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            dto.setDtoTime1(time1);
            List<Wps> list = wpsService.findJobSetNumber(page, dto);
            if (null != list && list.size() > 0) {
                for (Wps li : list) {
                    json.put("JOB_NUMBER", li.getJOB_NUMBER());
                    json.put("SET_NUMBER", li.getSET_NUMBER());
                    json.put("PART_NAME", li.getPART_NAME());
                    json.put("wirefeedrate", li.getWirefeedrate().toString());//焊材重量：g
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

    /**
     * 查询焊工焊材消耗量排行
     *
     * @param request
     * @return
     */
    @RequestMapping("/findWelderMaterialConsume")
    @ResponseBody
    public String findWelderMaterialConsume(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        String time1 = request.getParameter("startTime");
        try {
            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            } else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            List<DataStatistics> list = dss.findWelderMaterialConsume(time1);
            if (null != list && list.size() > 0) {
                for (DataStatistics li : list) {
                    json.put("name", li.getName());//焊工姓名
                    json.put("wirefeedrate", li.getWirefeedrate());//焊材消耗量：g
                    ary.add(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("name", "");
            json.put("wirefeedrate", "");
            ary.add(json);
        }
        JSONObject obj = new JSONObject();
        obj.put("ary", ary);
        return obj.toString();
    }

    /**
     * 查询超规范信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/findSupergageInfo")
    @ResponseBody
    public String findSupergageInfo(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        String time1 = request.getParameter("startTime");
        try {
            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            } else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            List<DataStatistics> list = dss.findSupergageInfo(time1);
            if (null != list && list.size() > 0) {
                for (DataStatistics li : list) {
                    json.put("name", li.getName());     //焊工姓名
                    json.put("insname", li.getInsname()); //组织机构名称：工区
                    json.put("starttime", li.getStarttime().split(" ")[0]);    //时间
                    json.put("job_number", li.getJOB_NUMBER());    //工作号
                    json.put("set_number", li.getSET_NUMBER());    //布套号
                    json.put("part_name", li.getPART_NAME());    //零件名称
                    ary.add(json);
                }
            } else {
                json.put("name", "");     //焊工姓名
                json.put("insname", ""); //组织机构名称：工区
                json.put("starttime", "");    //时间
                json.put("job_number", "");    //工作号
                json.put("set_number", "");    //布套号
                json.put("part_name", "");    //零件名称
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("name", "");     //焊工姓名
            json.put("insname", ""); //组织机构名称：工区
            json.put("starttime", "");    //时间
            json.put("job_number", "");    //工作号
            json.put("set_number", "");    //布套号
            json.put("part_name", "");    //零件名称
            ary.add(json);
        }
        JSONObject obj = new JSONObject();
        obj.put("ary", ary);
        return obj.toString();
    }

    /**
     * 查询超规范累计次数
     *
     * @param request
     * @return
     */
    @RequestMapping("/findSupergageCumulativeNumber")
    @ResponseBody
    public String findSupergageCumulativeNumber(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        int itemtype = 22;
        String time1 = request.getParameter("startTime");
        String organization = request.getParameter("organization");
        try {
            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            } else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            if ("0".equals(organization)) {
                itemtype = 22;
            } else {
                itemtype = 23;  //班组
            }
            List<DataStatistics> list = dss.findSupergageCumulativeNumber(time1, itemtype);
            if (null != list && list.size() > 0) {
                for (DataStatistics li : list) {
                    json.put("name", li.getName());//工区名称
                    json.put("num", li.getNum());//次数
                    ary.add(json);
                }
            } else {
                json.put("name", "");//工区名称
                json.put("num", "0");//次数
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("name", "");//工区名称
            json.put("num", "0");//次数
            ary.add(json);
        }
        JSONObject obj = new JSONObject();
        obj.put("ary", ary);
        return obj.toString();
    }

    /**
     * 查询工作号/布套号焊接工艺规范符合率
     *
     * @param request
     * @return
     */
    @RequestMapping("/findJobSetNormRate")
    @ResponseBody
    public String findJobSetNormRate(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        String time1 = request.getParameter("startTime");
        try {
            if (null == time1 || "".equals(time1) || "0".equals(time1)) {
                time1 = sdfDay.format(System.currentTimeMillis());   //默认当天时间
            } else {
                time1 = sdfMonth.format(System.currentTimeMillis()) + "-01";//当月
            }
            List<DataStatistics> list = dss.findJobSetNormRate(time1);
            if (null != list && list.size() > 0) {
                for (DataStatistics li : list) {
                    json.put("job_number", li.getJOB_NUMBER()); //工作号
                    json.put("set_number", li.getSET_NUMBER()); //布套号
                    json.put("part_name", li.getPART_NAME());   //零件名
                    BigDecimal bd = null;
                    //规范符合率 = 焊接时长 / （焊接时长 + 超规范时长）*100
                    if (li.getWkhour() != 0 || li.getWnhour() != 0) {
                        bd = new BigDecimal((li.getWkhour() / (li.getWkhour() + li.getWnhour())) * 100);
                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                        json.put("normRate", bd);
                    } else {
                        json.put("normRate", 0);
                    }
                    ary.add(json);
                }
            } else {
                json.put("job_number", ""); //工作号
                json.put("set_number", ""); //布套号
                json.put("part_name", "");   //零件名
                //规范符合率 = 焊接时长 / （焊接时长 + 超规范时长）*100
                json.put("normRate", 0);
                ary.add(json);
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.put("job_number", ""); //工作号
            json.put("set_number", ""); //布套号
            json.put("part_name", "");   //零件名
            //规范符合率 = 焊接时长 / （焊接时长 + 超规范时长）*100
            json.put("normRate", 0);
            ary.add(json);
        }
        JSONObject obj = new JSONObject();
        obj.put("ary", ary);
        return obj.toString();
    }

    /**
     * 工作号实时信息展示
     * 查询所有工作号和焊工班组信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/findWorkWelderInfo")
    @ResponseBody
    public String findWorkWelderInfo(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        //所有需要展示的工作号信息
        List<Wps> allWorkNumer = wpsService.findAllWorkNumer();
        //查询所有焊工及对应班组信息
        List<Welder> allWelderInfo = welderService.findAllWelderInfo();
        JSONObject obj = new JSONObject();
        obj.put("allWorkNumer", allWorkNumer);
        obj.put("allWelderInfo", allWelderInfo);
        return obj.toString();
    }
}
