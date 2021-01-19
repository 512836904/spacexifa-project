package com;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Mysql {

    public java.sql.Statement stmt;
    public ArrayList<String> listarray1;
    public ArrayList<String> listarray2;
    public ArrayList<String> listarray3;
    public DB_Connectionmysql db;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Mysql() {
        db = new DB_Connectionmysql();
        this.db = db;
    }

    public void Mysqlbase(String str) {
        Date time;
        Timestamp timesql = null;
        if (str.length() == 596) {
            try {
                //西安数据验证
                String check1 = str.substring(0, 2);
                String check11 = str.substring(594, 596);
                if (check1.equals("7E") && check11.equals("7D")) {
                    TbLiveData liveData = new TbLiveData();
                    liveData.setFitemid(new BigDecimal(Long.parseLong(str.substring(592, 594))));//组织id
                    liveData.setFmachinemodel(Integer.valueOf(str.substring(12, 14), 16).toString());//焊机型号
                    liveData.setFgather_no(Integer.valueOf(str.substring(16, 20), 16).toString());//采集编号
                    liveData.setFwelder_no(Integer.valueOf(str.substring(40, 44), 16).toString());//焊工号
                    String nowdatetime = sdf.format(System.currentTimeMillis());//当前系统时间
                    for (int a = 0; a < 367; a += 182) {
                        String year = Integer.valueOf(str.subSequence(44 + a, 46 + a).toString(), 16).toString();   //年份
                        String month = Integer.valueOf(str.subSequence(46 + a, 48 + a).toString(), 16).toString();
                        String day = Integer.valueOf(str.subSequence(48 + a, 50 + a).toString(), 16).toString();
                        String hour = Integer.valueOf(str.subSequence(50 + a, 52 + a).toString(), 16).toString();
                        String minute = Integer.valueOf(str.subSequence(52 + a, 54 + a).toString(), 16).toString();
                        String second = Integer.valueOf(str.subSequence(54 + a, 56 + a).toString(), 16).toString();
                        String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
                        timesql = new Timestamp(time.getTime());
                        //焊机日期与系统日期不一致，修改为系统时间
                        if (!nowdatetime.equals(timesql.toString().substring(0,10))){
                            liveData.setFweldtime(sdftime.format(System.currentTimeMillis()));//焊机工作时间
                        }else {
                            liveData.setFweldtime(timesql.toString());//焊机工作时间
                        }
                        liveData.setFelectricity(new BigDecimal(Long.parseLong(str.substring(56 + a, 60 + a), 16)));//焊接电流
                        liveData.setFvoltage(new BigDecimal(Long.parseLong(str.substring(60 + a, 64 + a), 16)));//焊接电压
                        liveData.setFwirefeedrate(new BigDecimal(Long.parseLong(str.substring(64 + a, 68 + a), 16)));//送丝速度
                        liveData.setFjunction_no(str.substring(76 + a, 84 + a));    //焊口号
                        liveData.setFstatus(Long.parseLong(str.substring(84 + a, 86 + a), 16));
                        liveData.setFwirediameter(new BigDecimal(Long.parseLong(str.substring(86 + a, 88 + a), 16)));
                        liveData.setFmaterialgas( new BigDecimal(Long.parseLong(str.substring(88 + a, 90 + a), 16)));
                        liveData.setFmax_electricity(new BigDecimal(Long.parseLong(str.substring(90 + a, 94 + a), 16)));
                        liveData.setFmin_electricity(new BigDecimal(Long.parseLong(str.substring(94 + a, 98 + a), 16)));
                        liveData.setFmax_voltage(new BigDecimal(Long.parseLong(str.substring(98 + a, 102 + a), 16)));
                        liveData.setFmin_voltage(new BigDecimal(Long.parseLong(str.substring(102 + a, 106 + a), 16)));
                        liveData.setFchannel(new BigDecimal(Long.parseLong(str.substring(106 + a, 108 + a), 16)));
                        liveData.setFrateofflow(new BigDecimal(Long.parseLong(str.substring(108 + a, 112 + a), 16)));
                        //西安新增
                        liveData.setFlon_air_flow(new BigDecimal(Long.parseLong(str.substring(116 + a, 120 + a), 16)));//离子气流量
                        liveData.setFhatwirecurrent(new BigDecimal(Long.parseLong(str.substring(120 + a, 124 + a), 16)));//
                        liveData.setFpreheating_temperature(Integer.valueOf(str.substring(124 + a, 128 + a), 16).toString());
                        liveData.setFswing(Integer.valueOf(str.substring(128 + a, 132 + a), 16).toString());
                        liveData.setFvibrafrequency(new BigDecimal(Long.parseLong(str.substring(132 + a, 136 + a), 16)));
                        liveData.setFlaser_power(Integer.valueOf(str.substring(136 + a, 140 + a), 16).toString());
                        liveData.setFdefocus_amount(Integer.valueOf(str.substring(140 + a, 144 + a), 16).toString());
                        liveData.setFdefocus_quantity(Integer.valueOf(str.substring(144 + a, 148 + a), 16).toString());
                        liveData.setFpeak_electricity(new BigDecimal(Long.parseLong(str.substring(148 + a, 152 + a), 16)));
                        liveData.setFbase_electricity(new BigDecimal(Long.parseLong(str.substring(152 + a, 156 + a), 16)));
                        liveData.setFpeak_time(Integer.valueOf(str.substring(156 + a, 160 + a), 16).toString());
                        liveData.setFbase_time(Integer.valueOf(str.substring(160 + a, 164 + a), 16).toString());
                        liveData.setFaccelerat_voltage(new BigDecimal(Long.parseLong(str.substring(164 + a, 168 + a), 16)));
                        liveData.setFfocus_current(new BigDecimal(Long.parseLong(str.substring(168 + a, 172 + a), 16)));
                        liveData.setFelectron_beam(new BigDecimal(Long.parseLong(str.substring(172 + a, 176 + a), 16)));
                        liveData.setFscan_frequency(Integer.valueOf(str.substring(176 + a, 180 + a), 16).toString());
                        liveData.setFscan_amplitude(Integer.valueOf(str.substring(180 + a, 184 + a), 16).toString());
                        liveData.setFswing_speed(Integer.valueOf(str.substring(184 + a, 188 + a), 16).toString());
                        db.DB_Connectionmysqlrun(liveData,listarray1,listarray2,listarray3);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Mysqlbaseoutline(String str,Server server) {
        Date time;
        Timestamp timesql = null;
        if (str.length() == 596) {
            try {
                //西安数据验证
                String check1 = str.substring(0, 2);
                String check11 = str.substring(594, 596);
                if (check1.equals("7E") && check11.equals("7D")) {
                    TbLiveData liveData = new TbLiveData();
                    liveData.setFitemid(new BigDecimal(Long.parseLong(str.substring(592, 594))));//组织id
                    liveData.setFmachinemodel(Integer.valueOf(str.substring(12, 14), 16).toString());//焊机型号
                    liveData.setFgather_no(Integer.valueOf(str.substring(16, 20), 16).toString());//采集编号
                    liveData.setFwelder_no(Integer.valueOf(str.substring(40, 44), 16).toString());//焊工号
                    String nowdatetime = sdf.format(System.currentTimeMillis());//当前系统时间
                    for (int a = 0; a < 367; a += 182) {
                        String year = Integer.valueOf(str.subSequence(44 + a, 46 + a).toString(), 16).toString();   //年份
                        String month = Integer.valueOf(str.subSequence(46 + a, 48 + a).toString(), 16).toString();
                        String day = Integer.valueOf(str.subSequence(48 + a, 50 + a).toString(), 16).toString();
                        String hour = Integer.valueOf(str.subSequence(50 + a, 52 + a).toString(), 16).toString();
                        String minute = Integer.valueOf(str.subSequence(52 + a, 54 + a).toString(), 16).toString();
                        String second = Integer.valueOf(str.subSequence(54 + a, 56 + a).toString(), 16).toString();
                        String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
                        timesql = new Timestamp(time.getTime());
                        //liveData.setFweldtime(timesql.toString());//焊机工作时间
                        //焊机日期与系统日期不一致，修改为系统时间
                        if (!nowdatetime.equals(timesql.toString().substring(0,10))){
                            liveData.setFweldtime(sdftime.format(System.currentTimeMillis()));//焊机工作时间
                        }else {
                            liveData.setFweldtime(timesql.toString());//焊机工作时间
                        }
                        liveData.setFelectricity(new BigDecimal(Long.parseLong(str.substring(56 + a, 60 + a), 16)));//焊接电流
                        liveData.setFvoltage(new BigDecimal(Long.parseLong(str.substring(60 + a, 64 + a), 16)));//焊接电压
                        liveData.setFwirefeedrate(new BigDecimal(Long.parseLong(str.substring(64 + a, 68 + a), 16)));//送丝速度
                        liveData.setFjunction_no(str.substring(76 + a, 84 + a));    //焊口号
                        liveData.setFstatus(Long.parseLong(str.substring(84 + a, 86 + a), 16));
                        liveData.setFwirediameter(new BigDecimal(Long.parseLong(str.substring(86 + a, 88 + a), 16)));
                        liveData.setFmaterialgas( new BigDecimal(Long.parseLong(str.substring(88 + a, 90 + a), 16)));
                        liveData.setFmax_electricity(new BigDecimal(Long.parseLong(str.substring(90 + a, 94 + a), 16)));
                        liveData.setFmin_electricity(new BigDecimal(Long.parseLong(str.substring(94 + a, 98 + a), 16)));
                        liveData.setFmax_voltage(new BigDecimal(Long.parseLong(str.substring(98 + a, 102 + a), 16)));
                        liveData.setFmin_voltage(new BigDecimal(Long.parseLong(str.substring(102 + a, 106 + a), 16)));
                        liveData.setFchannel(new BigDecimal(Long.parseLong(str.substring(106 + a, 108 + a), 16)));
                        liveData.setFrateofflow(new BigDecimal(Long.parseLong(str.substring(108 + a, 112 + a), 16)));
                        //西安新增
                        liveData.setFlon_air_flow(new BigDecimal(Long.parseLong(str.substring(116 + a, 120 + a), 16)));//离子气流量
                        liveData.setFhatwirecurrent(new BigDecimal(Long.parseLong(str.substring(120 + a, 124 + a), 16)));//
                        liveData.setFpreheating_temperature(Integer.valueOf(str.substring(124 + a, 128 + a), 16).toString());
                        liveData.setFswing(Integer.valueOf(str.substring(128 + a, 132 + a), 16).toString());
                        liveData.setFvibrafrequency(new BigDecimal(Long.parseLong(str.substring(132 + a, 136 + a), 16)));
                        liveData.setFlaser_power(Integer.valueOf(str.substring(136 + a, 140 + a), 16).toString());
                        liveData.setFdefocus_amount(Integer.valueOf(str.substring(140 + a, 144 + a), 16).toString());
                        liveData.setFdefocus_quantity(Integer.valueOf(str.substring(144 + a, 148 + a), 16).toString());
                        liveData.setFpeak_electricity(new BigDecimal(Long.parseLong(str.substring(148 + a, 152 + a), 16)));
                        liveData.setFbase_electricity(new BigDecimal(Long.parseLong(str.substring(152 + a, 156 + a), 16)));
                        liveData.setFpeak_time(Integer.valueOf(str.substring(156 + a, 160 + a), 16).toString());
                        liveData.setFbase_time(Integer.valueOf(str.substring(160 + a, 164 + a), 16).toString());
                        liveData.setFaccelerat_voltage(new BigDecimal(Long.parseLong(str.substring(164 + a, 168 + a), 16)));
                        liveData.setFfocus_current(new BigDecimal(Long.parseLong(str.substring(168 + a, 172 + a), 16)));
                        liveData.setFelectron_beam(new BigDecimal(Long.parseLong(str.substring(172 + a, 176 + a), 16)));
                        liveData.setFscan_frequency(Integer.valueOf(str.substring(176 + a, 180 + a), 16).toString());
                        liveData.setFscan_amplitude(Integer.valueOf(str.substring(180 + a, 184 + a), 16).toString());
                        liveData.setFswing_speed(Integer.valueOf(str.substring(184 + a, 188 + a), 16).toString());
                        db.DB_Connectionmysqloutline(server,liveData,listarray1,listarray2,listarray3);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Mysqlplc(String str, ArrayList<String> listarrayplc) {
        int machine = Integer.valueOf(str.substring(2, 4));
        int d1000 = Integer.valueOf(str.substring(4, 8), 16);
        int d1001 = Integer.valueOf(str.substring(8, 12), 16);
        int d1002 = Integer.valueOf(str.substring(12, 16), 16);
        int d1003 = Integer.valueOf(str.substring(16, 20), 16);
        int d1004 = Integer.valueOf(str.substring(20, 24), 16);
        int d1005 = Integer.valueOf(str.substring(24, 28), 16);
        int d1006 = Integer.valueOf(str.substring(28, 32), 16);
        int d1007 = Integer.valueOf(str.substring(32, 36), 16);
        int d1008 = Integer.valueOf(str.substring(36, 40), 16);
        int d1009 = Integer.valueOf(str.substring(40, 44), 16);
        int d1010 = Integer.valueOf(str.substring(44, 48), 16);
        int d1011 = Integer.valueOf(str.substring(48, 52), 16);
        int d1012 = Integer.valueOf(str.substring(52, 56), 16);
        int d1013 = Integer.valueOf(str.substring(56, 60), 16);
        int d1014 = Integer.valueOf(str.substring(60, 64), 16);
        int d1015 = Integer.valueOf(str.substring(64, 68), 16);
        int d1016 = Integer.valueOf(str.substring(68, 72), 16);
        int d1017 = Integer.valueOf(str.substring(72, 76), 16);
        int d1018 = Integer.valueOf(str.substring(76, 80), 16);
        int d1019 = Integer.valueOf(str.substring(80, 84), 16);
        int d1020 = Integer.valueOf(str.substring(84, 88), 16);
        int d1021 = Integer.valueOf(str.substring(88, 92), 16);
        int d1022 = Integer.valueOf(str.substring(92, 96), 16);
        int d1023 = Integer.valueOf(str.substring(96, 100), 16);
        int d1024 = Integer.valueOf(str.substring(100, 104), 16);
        int d1025 = Integer.valueOf(str.substring(104, 108), 16);
        int d1026 = Integer.valueOf(str.substring(108, 112), 16);
        int d1027 = Integer.valueOf(str.substring(112, 116), 16);
        int d1028 = Integer.valueOf(str.substring(116, 120), 16);
        int d1029 = Integer.valueOf(str.substring(120, 124), 16);
        int d1030 = Integer.valueOf(str.substring(124, 128), 16);
        int d1031 = Integer.valueOf(str.substring(128, 132), 16);
        int d1032 = Integer.valueOf(str.substring(132, 136), 16);
        int d1033 = Integer.valueOf(str.substring(136, 140), 16);
        int d1034 = Integer.valueOf(str.substring(140, 144), 16);
        int d1035 = Integer.valueOf(str.substring(144, 148), 16);
        int d1036 = Integer.valueOf(str.substring(148, 152), 16);
        int d1037 = Integer.valueOf(str.substring(152, 156), 16);
        int d1038 = Integer.valueOf(str.substring(156, 160), 16);
        int d1039 = Integer.valueOf(str.substring(160, 164), 16);
        int d1040 = Integer.valueOf(str.substring(164, 168), 16);
        int d1041 = Integer.valueOf(str.substring(168, 172), 16);
        int d1042 = Integer.valueOf(str.substring(172, 176), 16);
        int d1043 = Integer.valueOf(str.substring(176, 180), 16);
        int d1044 = Integer.valueOf(str.substring(180, 184), 16);
        int d1045 = Integer.valueOf(str.substring(184, 188), 16);
        int d1046 = Integer.valueOf(str.substring(188, 192), 16);
        int d1047 = Integer.valueOf(str.substring(192, 196), 16);
        int d1048 = Integer.valueOf(str.substring(196, 200), 16);
        int d1049 = Integer.valueOf(str.substring(200, 204), 16);
        int d1050 = Integer.valueOf(str.substring(204, 208), 16);
        int d1051 = Integer.valueOf(str.substring(208, 212), 16);
        int d1052 = Integer.valueOf(str.substring(212, 216), 16);
        int d1053 = Integer.valueOf(str.substring(216, 220), 16);
        int d1054 = Integer.valueOf(str.substring(220, 224), 16);
        int d1055 = Integer.valueOf(str.substring(224, 228), 16);
        int d1056 = Integer.valueOf(str.substring(228, 232), 16);
        int d1057 = Integer.valueOf(str.substring(232, 236), 16);
        int d1058 = Integer.valueOf(str.substring(236, 240), 16);
        int d1059 = Integer.valueOf(str.substring(240, 244), 16);
        int d1060 = Integer.valueOf(str.substring(244, 248), 16);
        int d1061 = Integer.valueOf(str.substring(248, 252), 16);
        int d1062 = Integer.valueOf(str.substring(252, 256), 16);
        int d1063 = Integer.valueOf(str.substring(256, 260), 16);
        int d1064 = Integer.valueOf(str.substring(260, 264), 16);
        int d1065 = Integer.valueOf(str.substring(264, 268), 16);
        int d1066 = Integer.valueOf(str.substring(268, 272), 16);
        int d1067 = Integer.valueOf(str.substring(272, 276), 16);
        int d1068 = Integer.valueOf(str.substring(276, 280), 16);
        int d1069 = Integer.valueOf(str.substring(280, 284), 16);
        int d1070 = Integer.valueOf(str.substring(284, 288), 16);
        int d1071 = Integer.valueOf(str.substring(288, 292), 16);
        int d1072 = Integer.valueOf(str.substring(292, 296), 16);
        db.DB_Connectionmysqlplc(listarrayplc, machine, d1000, d1001, d1002, d1003, d1004, d1005, d1006, d1007, d1008, d1009, d1010, d1011, d1012, d1013, d1014, d1015, d1016, d1017, d1018, d1019, d1020, d1021, d1022, d1023, d1024, d1025, d1026, d1027, d1028, d1029, d1030, d1031, d1032, d1033, d1034, d1035, d1036, d1037, d1038, d1039, d1040, d1041, d1042, d1043, d1044, d1045, d1046, d1047, d1048, d1049, d1050, d1051, d1052, d1053, d1054, d1055, d1056, d1057, d1058, d1059, d1060, d1061, d1062, d1063, d1064, d1065, d1066, d1067, d1068, d1069, d1070, d1071, d1072);
    }

}
