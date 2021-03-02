package com;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class DB_Connectionmysql {

    public String select;
    public String datasend = "";
    //    public String connet;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public ArrayList<String> taskarray = new ArrayList<String>();

    public String getId() {
        return datasend;
    }

    public void setId(String datasend) {
        this.datasend = datasend;
    }

    public Server server;

    public int workbase = 1;
    public int workplc = 1;
    public int workoutline = 1;
    public int countbase1 = 1;
    public int countbase2 = 1;
    public int countbase3 = 1;
    public int countbase4 = 1;
    public int countplc1 = 1;
    public int countplc2 = 1;
    public int countplc3 = 1;
    public int countplc4 = 1;
    public int countoutlineA1 = 1;
    public int countoutlineA2 = 1;
    public int countoutlineA3 = 1;
    public int countoutlineA4 = 1;
    public int countoutlineB1 = 1;
    public int countoutlineB2 = 1;
    public int countoutlineB3 = 1;
    public int countoutlineB4 = 1;
    public String inSqlbaseb1 = "";
    public String inSqlbase1 = "";
    public String inSqlbase2 = "";
    public String inSqlbase3 = "";
    public String inSqlbase4 = "";
    public String inSqlplc1 = "";
    public String inSqlplc2 = "";
    public String inSqlplc3 = "";
    public String inSqlplc4 = "";
    public String inSqloutlineA1 = "";
    public String inSqloutlineA2 = "";
    public String inSqloutlineA3 = "";
    public String inSqloutlineA4 = "";
    public String inSqloutlineB1 = "";
    public String inSqloutlineB2 = "";
    public String inSqloutlineB3 = "";
    public String inSqloutlineB4 = "";
    public final String inSqlbase = " into tb_live_data(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,fstatus,"
            + "fwirefeedrate,FUploadDateTime,FWeldTime,fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,"
            + "fwelder_itemid,fjunction_itemid,fmachine_itemid,fmachinemodel,fwirediameter,fmaterialgas,frateofflow"
            + ",flon_air_flow,fhatwirecurrent,fpreheating_temperature,fswing,fvibrafrequency,flaser_power,fdefocus_amount,fdefocus_quantity,fpeak_electricity"
            + ",fbase_electricity,fpeak_time,fbase_time,faccelerat_voltage,ffocus_current,felectron_beam,fscan_frequency,fscan_amplitude,fswing_speed,fcard_id,"
            + "fwps_lib_id,fproduct_number_id,femployee_id,fstep_id) values";

    public final String inSqloutlineA = " into tb_dataA(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,fstatus," +
            "fwirefeedrate,FUploadDateTime,FWeldTime,fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage," +
            "fwelder_itemid,fjunction_itemid,fmachine_itemid,fmachinemodel,fwirediameter,fmaterialgas,frateofflow"
            + ",flon_air_flow,fhatwirecurrent,fpreheating_temperature,fswing,fvibrafrequency,flaser_power,fdefocus_amount,fdefocus_quantity,fpeak_electricity"
            + ",fbase_electricity,fpeak_time,fbase_time,faccelerat_voltage,ffocus_current,felectron_beam,fscan_frequency,fscan_amplitude,fswing_speed,fcard_id," +
            "fwps_lib_id,fproduct_number_id,femployee_id,fstep_id) values";


    public final String inSqloutlineB = " into tb_dataB(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,fstatus," +
            "fwirefeedrate,FUploadDateTime,FWeldTime,fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage," +
            "fwelder_itemid,fjunction_itemid,fmachine_itemid,fmachinemodel,fwirediameter,fmaterialgas,frateofflow"
            + ",flon_air_flow,fhatwirecurrent,fpreheating_temperature,fswing,fvibrafrequency,flaser_power,fdefocus_amount,fdefocus_quantity,fpeak_electricity"
            + ",fbase_electricity,fpeak_time,fbase_time,faccelerat_voltage,ffocus_current,felectron_beam,fscan_frequency,fscan_amplitude,fswing_speed,fcard_id," +
            "fwps_lib_id,fproduct_number_id,femployee_id,fstep_id) values";

    public final String inSqlplc = "insert into tb_live_data(fwelder_id,fgather_no,fjunction_id,felectricity,fvoltage,fstatus,FUploadDateTime,FWeldTime,fd1000," +
            "fd1001,fd1002,fd1003,fd1004,fd1005,fd1006,fd1007,fd1008,fd1009,fd1010,fd1011,fd1012,fd1013,fd1014,fd1015,fd1016,fd1017,fd1018,fd1019,fd1020,fd1021," +
            "fd1022,fd1023,fd1024,fd1025,fd1026,fd1027,fd1028,fd1029,fd1030,fd1031,fd1032,fd1033,fd1034,fd1035,fd1036,fd1037,fd1038,fd1039,fd1040,fd1041,fd1042," +
            "fd1043,fd1044,fd1045,fd1046,fd1047,fd1048,fd1049,fd1050,fd1051,fd1052,fd1053,fd1054,fd1055,fd1056,fd1057,fd1058,fd1059,fd1060,fd1061,fd1062,fd1063," +
            "fd1064,fd1065,fd1066,fd1067,fd1068,fd1069,fd1070,fd1071,fd1072) values";

    public DB_Connectionmysql() {
    }

    /**
     * sql执行
     */
    private void executeSQL(String sql) {
        Server.cachedThreadPool.execute(new SqlWorkInsert(sql));
    }

    public static class SqlWorkInsert implements Runnable {
        private String sql = "";

        public SqlWorkInsert(String sql) {
            this.sql = sql;
        }

        @Override
        public void run() {
            if (null != sql && !"".equals(sql)) {
                Connection connection = null;
                Statement statement = null;
                try {
                    if (connection == null || connection.isClosed()) {
                        connection = OracleDBConnection.getConnection();
                    }
                    if (statement == null || statement.isClosed()) {
                        statement = connection.createStatement();
                    }
                    synchronized (sql) {
                        statement.executeUpdate(sql);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    //释放连接，归还资源
                    OracleDBConnection.close(connection, statement, null);
                }
            }
        }
    }

    /**
     * sql 拼接
     *
     * @param liveData
     * @return
     */
    private String sqlJoint(TbLiveData liveData) {
        String str = "";
        str = "('" + liveData.getFwelder_id() + "','" + liveData.getFgather_no() + "','" + liveData.getFmachine_id() + "','" + liveData.getFjunction_id() + "','" +
                liveData.getFitemid() + "','" + liveData.getFelectricity() + "','" + liveData.getFvoltage() + "','" + liveData.getFstatus() + "','" + liveData.getFwirefeedrate() +
                "',to_date(substr('" + liveData.getFuploadDatetime() + "',1,19), 'yyyy-mm-dd hh24:mi:ss'),to_date(substr('" + liveData.getFweldtime() + "',1,19), 'yyyy-mm-dd hh24:mi:ss'),'" +
                liveData.getFwelder_no() + "','" + liveData.getFjunction_no() + "','" + liveData.getFweld_no() + "','" + liveData.getFchannel() + "','" + liveData.getFmax_electricity() +
                "','" + liveData.getFmin_electricity() + "','" + liveData.getFmax_voltage() + "','" + liveData.getFmin_voltage() + "','" + liveData.getFwelder_itemid() + "','" +
                liveData.getFjunction_itemid() + "','" + liveData.getFmachine_itemid() + "','" + liveData.getFmachinemodel() + "','" + liveData.getFwirediameter() + "','" +
                liveData.getFmaterialgas() + "','" + liveData.getFrateofflow() + "','" + liveData.getFlon_air_flow() + "','" + liveData.getFhatwirecurrent() + "','" +
                liveData.getFpreheating_temperature() + "','" + liveData.getFswing() + "','" + liveData.getFvibrafrequency() + "','" + liveData.getFlaser_power() + "','" +
                liveData.getFdefocus_amount() + "','" + liveData.getFdefocus_quantity() + "','" + liveData.getFpeak_electricity() + "','" + liveData.getFbase_electricity() + "','" +
                liveData.getFpeak_time() + "','" + liveData.getFbase_time() + "','" + liveData.getFaccelerat_voltage() + "','" + liveData.getFfocus_current() + "','" +
                liveData.getFelectron_beam() + "','" + liveData.getFscan_frequency() + "','" + liveData.getFscan_amplitude() + "','" + liveData.getFswing_speed() + "','" +
                liveData.getFcard_id() + "','" + liveData.getFwps_lib_id() + "','" + liveData.getFproduct_number_id() + "','" + liveData.getFemployee_id() + "','" + liveData.getFstep_id() + "')";
        return str;
    }

    public void DB_Connectionmysqlrun(TbLiveData liveData, ArrayList<String> listarray1, ArrayList<String> listarray2, ArrayList<String> listarray3) {

        String weldid = "0";
        Date date;
        String nowTime;
        Timestamp goodsC_date;          //上传时间
        String weldernum = "0000";      //焊工编号
        String junctionnum = "0000";    //焊口编号
        String gathernum = "0000";
        String weldnum = "0000";
        String ins = "0";
        String welderitemid = "0";
        BigDecimal cardid = BigDecimal.ZERO;
        int wpsid = 0;
        int productid = 0;
        int workprocedureid = 0;
        int employee_id = 0;
        int junction_id = 0;
        String welderid = "0";
        int junctionid = 0;

        switch (workbase) {
            case 1:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);
                liveData.setFuploadDatetime(goodsC_date.toString());
                if ("137".equals(liveData.getFmachinemodel())) { //137：WB_A350P，128：CPVE500
                    liveData.setFelectricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFelectricity().toString())) / 10));
                    liveData.setFmax_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_electricity().toString())) / 10));
                    liveData.setFmin_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_electricity().toString())) / 10));
                }
                liveData.setFvoltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFvoltage().toString())) / 10));//实际电压
                liveData.setFmax_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_voltage().toString())) / 10));//最大电压
                liveData.setFmin_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_voltage().toString())) / 10));//最小电压
                liveData.setFwirefeedrate(BigDecimal.valueOf((Double.parseDouble(liveData.getFwirefeedrate().toString())) / 10));//送丝速度
                liveData.setFrateofflow(BigDecimal.valueOf((Double.parseDouble(liveData.getFrateofflow().toString())) / 10));//气体流量
                //焊工信息
                for (int i = 0; i < listarray1.size(); i += 3) {
                    if (("0090" + liveData.getFwelder_no()).equals(listarray1.get(i + 1))) {
                        welderid = listarray1.get(i);//焊工id
                        weldernum = listarray1.get(i + 1);//焊工编号
                        welderitemid = listarray1.get(i + 2);//焊工组织id
                        break;
                    }
                }
                //焊机信息
                for (int i = 0; i < listarray2.size(); i += 4) {
                    //采集模块编号判断
                    if (liveData.getFgather_no().equals(listarray2.get(i + 2))) {
                        weldid = listarray2.get(i);//焊机id
                        weldnum = listarray2.get(i + 1);//焊机固号
                        gathernum = listarray2.get(i + 2);//采集编号
                        ins = listarray2.get(i + 3);//焊机组织id
                        break;
                    }
                }
                if (gathernum.length() < 4) {
                    int len = 4 - gathernum.length();
                    for (int i = 0; i < len; i++) {
                        gathernum = "0" + gathernum;
                    }
                }
                //判断手持终端是否下发了任务信息
                if (!taskarray.isEmpty()) {
                    if (taskarray.contains("m-" + gathernum)) {
                        int index = taskarray.indexOf("m-" + gathernum);
                        cardid = BigDecimal.valueOf(Long.parseLong(taskarray.get(index + 2)));//工作号id
                        wpsid = Integer.parseInt(taskarray.get(index + 3));//生产工艺id
                        productid = Integer.parseInt(taskarray.get(index + 4));//产品号id(返修状态)
                        employee_id = Integer.parseInt(taskarray.get(index + 5));
                        workprocedureid = Integer.parseInt(taskarray.get(index + 6));
                        junction_id = Integer.parseInt(taskarray.get(index + 7));//焊缝号id
                        //System.out.println("存实时表：" + gathernum + ",工作号id：" + cardid);
                    }
                }
                liveData.setFmachine_id(BigDecimal.valueOf(Long.parseLong(weldid)));
                liveData.setFweld_no(weldnum);
                liveData.setFwelder_id(BigDecimal.valueOf(Long.parseLong(welderid)));
                liveData.setFwelder_itemid(BigDecimal.valueOf(Long.parseLong(welderitemid)));
                liveData.setFmachine_itemid(BigDecimal.valueOf(Long.parseLong(ins)));
                liveData.setFjunction_itemid(BigDecimal.valueOf(0));
                liveData.setFcard_id(cardid);//电子跟踪卡
                liveData.setFstep_id(BigDecimal.valueOf(workprocedureid));//工步id
                liveData.setFproduct_number_id(BigDecimal.valueOf(productid));//产品id
                liveData.setFwps_lib_id(BigDecimal.valueOf(wpsid));//工艺规程id
                liveData.setFemployee_id(BigDecimal.valueOf(employee_id));//工序id
                liveData.setFjunction_id(BigDecimal.valueOf(junction_id));//焊缝id
                if (countbase1 == 1) {
                    inSqlbase1 = inSqlbase + sqlJoint(liveData);
                } else {
                    inSqlbase1 = inSqlbase1 + " " + inSqlbase + sqlJoint(liveData);
                }
                countbase1++;
                if (countbase1 == 100) {
                    //sql批量插入
                    inSqlbase1 = "insert all " + inSqlbase1 + "  select * from dual";
                    //sql执行
                    executeSQL(inSqlbase1);
                    workbase = workbase + 1;
                    if (workbase == 5) {
                        workbase = 1;
                    }
                    countbase1 = 1;
                    inSqlbase1 = "";
                }
                break;
            case 2:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);
                liveData.setFuploadDatetime(goodsC_date.toString());
                if ("137".equals(liveData.getFmachinemodel())) { //137：WB_A350P，128：CPVE500
                    liveData.setFelectricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFelectricity().toString())) / 10));
                    liveData.setFmax_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_electricity().toString())) / 10));
                    liveData.setFmin_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_electricity().toString())) / 10));
                }
                liveData.setFvoltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFvoltage().toString())) / 10));//实际电压
                liveData.setFmax_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_voltage().toString())) / 10));//最大电压
                liveData.setFmin_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_voltage().toString())) / 10));//最小电压
                liveData.setFwirefeedrate(BigDecimal.valueOf((Double.parseDouble(liveData.getFwirefeedrate().toString())) / 10));//送丝速度
                liveData.setFrateofflow(BigDecimal.valueOf((Double.parseDouble(liveData.getFrateofflow().toString())) / 10));//气体流量
                //焊工信息
                for (int i = 0; i < listarray1.size(); i += 3) {
                    if (("0090" + liveData.getFwelder_no()).equals(listarray1.get(i + 1))) {
                        weldernum = listarray1.get(i + 1);//焊工编号
                        welderid = listarray1.get(i);//焊工id
                        welderitemid = listarray1.get(i + 2);//焊工组织id
                        break;
                    }
                }
                //焊机信息
                for (int i = 0; i < listarray2.size(); i += 4) {
                    //采集模块编号判断
                    if (liveData.getFgather_no().equals(listarray2.get(i + 2))) {
                        weldid = listarray2.get(i);//焊机id
                        weldnum = listarray2.get(i + 1);//焊机固号
                        gathernum = listarray2.get(i + 2);//采集编号
                        ins = listarray2.get(i + 3);//焊机组织id
                        break;
                    }
                }
                if (gathernum.length() < 4) {
                    int len = 4 - gathernum.length();
                    for (int i = 0; i < len; i++) {
                        gathernum = "0" + gathernum;
                    }
                }
                if (!taskarray.isEmpty()) {
                    if (taskarray.contains("m-" + gathernum)) {
                        int index = taskarray.indexOf("m-" + gathernum);
                        //welderid = Integer.valueOf(taskarray.get(index + 1));
                        cardid = BigDecimal.valueOf(Long.parseLong(taskarray.get(index + 2)));//工作号id
                        wpsid = Integer.parseInt(taskarray.get(index + 3));
                        productid = Integer.parseInt(taskarray.get(index + 4));
                        employee_id = Integer.parseInt(taskarray.get(index + 5));
                        workprocedureid = Integer.parseInt(taskarray.get(index + 6));
                        junction_id = Integer.parseInt(taskarray.get(index + 7));
                    }
                }
                liveData.setFmachine_id(BigDecimal.valueOf(Long.parseLong(weldid)));
                liveData.setFweld_no(weldnum);
                liveData.setFwelder_id(BigDecimal.valueOf(Long.parseLong(welderid)));
                liveData.setFwelder_itemid(BigDecimal.valueOf(Long.parseLong(welderitemid)));
                liveData.setFmachine_itemid(BigDecimal.valueOf(Long.parseLong(ins)));
                liveData.setFjunction_itemid(BigDecimal.valueOf(0));
                liveData.setFcard_id(cardid);//电子跟踪卡
                liveData.setFstep_id(BigDecimal.valueOf(workprocedureid));//工步id
                liveData.setFproduct_number_id(BigDecimal.valueOf(productid));//产品id
                liveData.setFwps_lib_id(BigDecimal.valueOf(wpsid));//工艺规程id
                liveData.setFemployee_id(BigDecimal.valueOf(employee_id));//工序id
                liveData.setFjunction_id(BigDecimal.valueOf(junction_id));
                if (countbase2 == 1) {
                    inSqlbase2 = inSqlbase + sqlJoint(liveData);
                } else {
                    inSqlbase2 = inSqlbase2 + " " + inSqlbase + sqlJoint(liveData);
                }
                countbase2++;
                if (countbase2 == 100) {
                    //sql批量插入
                    inSqlbase2 = "insert all " + inSqlbase2 + "  select * from dual";
                    //sql执行
                    executeSQL(inSqlbase2);
                    workbase = workbase + 1;
                    if (workbase == 5) {
                        workbase = 1;
                    }
                    countbase2 = 1;
                    inSqlbase2 = "";
                }
                break;
            case 3:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);
                liveData.setFuploadDatetime(goodsC_date.toString());
                if ("137".equals(liveData.getFmachinemodel())) { //137：WB_A350P，128：CPVE500
                    liveData.setFelectricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFelectricity().toString())) / 10));
                    liveData.setFmax_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_electricity().toString())) / 10));
                    liveData.setFmin_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_electricity().toString())) / 10));
                }
                liveData.setFvoltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFvoltage().toString())) / 10));//实际电压
                liveData.setFmax_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_voltage().toString())) / 10));//最大电压
                liveData.setFmin_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_voltage().toString())) / 10));//最小电压
                liveData.setFwirefeedrate(BigDecimal.valueOf((Double.parseDouble(liveData.getFwirefeedrate().toString())) / 10));//送丝速度
                liveData.setFrateofflow(BigDecimal.valueOf((Double.parseDouble(liveData.getFrateofflow().toString())) / 10));//气体流量
                //焊工信息
                for (int i = 0; i < listarray1.size(); i += 3) {
                    if (("0090" + liveData.getFwelder_no()).equals(listarray1.get(i + 1))) {
                        weldernum = listarray1.get(i + 1);//焊工编号
                        welderid = listarray1.get(i);//焊工id
                        welderitemid = listarray1.get(i + 2);//焊工组织id
                        break;
                    }
                }
                //焊机信息
                for (int i = 0; i < listarray2.size(); i += 4) {
                    //采集模块编号判断
                    if (liveData.getFgather_no().equals(listarray2.get(i + 2))) {
                        weldid = listarray2.get(i);//焊机id
                        weldnum = listarray2.get(i + 1);//焊机固号
                        gathernum = listarray2.get(i + 2);//采集编号
                        ins = listarray2.get(i + 3);//焊机组织id
                        break;
                    }
                }
                if (gathernum.length() < 4) {
                    int len = 4 - gathernum.length();
                    for (int i = 0; i < len; i++) {
                        gathernum = "0" + gathernum;
                    }
                }
                if (!taskarray.isEmpty()) {
                    if (taskarray.contains("m-" + gathernum)) {
                        int index = taskarray.indexOf("m-" + gathernum);
                        //welderid = Integer.valueOf(taskarray.get(index + 1));
                        cardid = BigDecimal.valueOf(Long.parseLong(taskarray.get(index + 2)));//工作号id
                        wpsid = Integer.parseInt(taskarray.get(index + 3));
                        productid = Integer.parseInt(taskarray.get(index + 4));
                        employee_id = Integer.parseInt(taskarray.get(index + 5));
                        workprocedureid = Integer.parseInt(taskarray.get(index + 6));
                        junction_id = Integer.parseInt(taskarray.get(index + 7));
                    }
                }
                liveData.setFmachine_id(BigDecimal.valueOf(Long.parseLong(weldid)));
                liveData.setFweld_no(weldnum);
                liveData.setFwelder_id(BigDecimal.valueOf(Long.parseLong(welderid)));
                liveData.setFwelder_itemid(BigDecimal.valueOf(Long.parseLong(welderitemid)));
                liveData.setFmachine_itemid(BigDecimal.valueOf(Long.parseLong(ins)));
                liveData.setFjunction_itemid(BigDecimal.valueOf(0));
                liveData.setFcard_id(cardid);//电子跟踪卡
                liveData.setFstep_id(BigDecimal.valueOf(workprocedureid));//工步id
                liveData.setFproduct_number_id(BigDecimal.valueOf(productid));//产品id
                liveData.setFwps_lib_id(BigDecimal.valueOf(wpsid));//工艺规程id
                liveData.setFemployee_id(BigDecimal.valueOf(employee_id));//工序id
                liveData.setFjunction_id(BigDecimal.valueOf(junction_id));
                if (countbase3 == 1) {
                    inSqlbase3 = inSqlbase + sqlJoint(liveData);
                } else {
                    inSqlbase3 = inSqlbase3 + " " + inSqlbase + sqlJoint(liveData);
                }
                countbase3++;
                if (countbase3 == 100) {
                    //sql批量插入
                    inSqlbase3 = "insert all " + inSqlbase3 + "  select * from dual";
                    //sql执行
                    executeSQL(inSqlbase3);
                    workbase = workbase + 1;
                    if (workbase == 5) {
                        workbase = 1;
                    }
                    countbase3 = 1;
                    inSqlbase3 = "";
                }
                break;
            case 4:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);
                liveData.setFuploadDatetime(goodsC_date.toString());
                if ("137".equals(liveData.getFmachinemodel())) { //137：WB_A350P，128：CPVE500
                    liveData.setFelectricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFelectricity().toString())) / 10));
                    liveData.setFmax_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_electricity().toString())) / 10));
                    liveData.setFmin_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_electricity().toString())) / 10));
                }
                liveData.setFvoltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFvoltage().toString())) / 10));//实际电压
                liveData.setFmax_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_voltage().toString())) / 10));//最大电压
                liveData.setFmin_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_voltage().toString())) / 10));//最小电压
                liveData.setFwirefeedrate(BigDecimal.valueOf((Double.parseDouble(liveData.getFwirefeedrate().toString())) / 10));//送丝速度
                liveData.setFrateofflow(BigDecimal.valueOf((Double.parseDouble(liveData.getFrateofflow().toString())) / 10));//气体流量
                //焊工信息
                for (int i = 0; i < listarray1.size(); i += 3) {
                    if (("0090" + liveData.getFwelder_no()).equals(listarray1.get(i + 1))) {
                        weldernum = listarray1.get(i + 1);//焊工编号
                        welderid = listarray1.get(i);//焊工id
                        welderitemid = listarray1.get(i + 2);//焊工组织id
                        break;
                    }
                }
                //焊机信息
                for (int i = 0; i < listarray2.size(); i += 4) {
                    //采集模块编号判断
                    if (liveData.getFgather_no().equals(listarray2.get(i + 2))) {
                        weldid = listarray2.get(i);//焊机id
                        weldnum = listarray2.get(i + 1);//焊机固号
                        gathernum = listarray2.get(i + 2);//采集编号
                        ins = listarray2.get(i + 3);//焊机组织id
                        break;
                    }
                }
                if (gathernum.length() < 4) {
                    int len = 4 - gathernum.length();
                    for (int i = 0; i < len; i++) {
                        gathernum = "0" + gathernum;
                    }
                }
                if (!taskarray.isEmpty()) {
                    if (taskarray.contains("m-" + gathernum)) {
                        int index = taskarray.indexOf("m-" + gathernum);
                        //welderid = Integer.valueOf(taskarray.get(index + 1));
                        cardid = BigDecimal.valueOf(Long.parseLong(taskarray.get(index + 2)));//工作号id
                        wpsid = Integer.parseInt(taskarray.get(index + 3));
                        productid = Integer.parseInt(taskarray.get(index + 4));
                        employee_id = Integer.parseInt(taskarray.get(index + 5));
                        workprocedureid = Integer.parseInt(taskarray.get(index + 6));
                        junction_id = Integer.parseInt(taskarray.get(index + 7));
                    }
                }
                liveData.setFmachine_id(BigDecimal.valueOf(Long.parseLong(weldid)));
                liveData.setFweld_no(weldnum);
                liveData.setFwelder_id(BigDecimal.valueOf(Long.parseLong(welderid)));
                liveData.setFwelder_itemid(BigDecimal.valueOf(Long.parseLong(welderitemid)));
                liveData.setFmachine_itemid(BigDecimal.valueOf(Long.parseLong(ins)));
                liveData.setFjunction_itemid(BigDecimal.valueOf(0));
                liveData.setFcard_id(cardid);//电子跟踪卡
                liveData.setFstep_id(BigDecimal.valueOf(workprocedureid));//工步id
                liveData.setFproduct_number_id(BigDecimal.valueOf(productid));//产品id
                liveData.setFwps_lib_id(BigDecimal.valueOf(wpsid));//工艺规程id
                liveData.setFemployee_id(BigDecimal.valueOf(employee_id));//工序id
                liveData.setFjunction_id(BigDecimal.valueOf(junction_id));
                if (countbase4 == 1) {
                    inSqlbase4 = inSqlbase + sqlJoint(liveData);
                } else {
                    inSqlbase4 = inSqlbase4 + " " + inSqlbase + sqlJoint(liveData);
                }
                countbase4++;
                if (countbase4 == 100) {
                    countbase4 = 1;
                    //sql批量插入
                    inSqlbase4 = "insert all " + inSqlbase4 + "  select * from dual";
                    //sql执行
                    executeSQL(inSqlbase4);
                    workbase = workbase + 1;
                    if (workbase == 5) {
                        workbase = 1;
                    }
                    countbase4 = 1;
                    inSqlbase4 = "";
                }
                break;
        }
    }

    public void DB_Connectionmysqloutline(Server server, TbLiveData liveData, ArrayList<String> listarray1, ArrayList<String> listarray2, ArrayList<String> listarray3) {

        String weldid = "0";
        Date date;
        String nowTime;
        Timestamp goodsC_date;          //上传时间
        String weldernum = "0000";      //焊工编号
        String junctionnum = "0000";    //焊口编号
        String gathernum = "0000";
        String weldnum = "0000";
        String ins = "0";
        String welderitemid = "0";
        BigDecimal cardid = BigDecimal.ZERO;
        int wpsid = 0;
        int productid = 0;
        int workprocedureid = 0;
        int employee_id = 0;
        int junction_id = 0;
        String welderid = "0";
        int junctionid = 0;

        switch (workoutline) {
            case 1:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);
                liveData.setFuploadDatetime(goodsC_date.toString());
                if ("137".equals(liveData.getFmachinemodel())) { //137：WB_A350P，128：CPVE500
                    liveData.setFelectricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFelectricity().toString())) / 10));
                    liveData.setFmax_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_electricity().toString())) / 10));
                    liveData.setFmin_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_electricity().toString())) / 10));
                }
                liveData.setFvoltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFvoltage().toString())) / 10));//实际电压
                liveData.setFmax_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_voltage().toString())) / 10));//最大电压
                liveData.setFmin_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_voltage().toString())) / 10));//最小电压
                liveData.setFwirefeedrate(BigDecimal.valueOf((Double.parseDouble(liveData.getFwirefeedrate().toString())) / 10));//送丝速度
                liveData.setFrateofflow(BigDecimal.valueOf((Double.parseDouble(liveData.getFrateofflow().toString())) / 10));//气体流量
                //焊工信息
                for (int i = 0; i < listarray1.size(); i += 3) {
                    if (("0090" + liveData.getFwelder_no()).equals(listarray1.get(i + 1))) {
                        weldernum = listarray1.get(i + 1);//焊工编号
                        welderid = listarray1.get(i);//焊工id
                        welderitemid = listarray1.get(i + 2);//焊工组织id
                        break;
                    }
                }
                //焊机信息
                for (int i = 0; i < listarray2.size(); i += 4) {
                    //采集模块编号判断
                    if (liveData.getFgather_no().equals(listarray2.get(i + 2))) {
                        weldid = listarray2.get(i);//焊机id
                        weldnum = listarray2.get(i + 1);//焊机固号
                        gathernum = listarray2.get(i + 2);//采集编号
                        ins = listarray2.get(i + 3);//焊机组织id
                        break;
                    }
                }
                if (gathernum.length() < 4) {
                    int len = 4 - gathernum.length();
                    for (int i = 0; i < len; i++) {
                        gathernum = "0" + gathernum;
                    }
                }
                //判断手持终端是否下发了任务信息
                if (!taskarray.isEmpty()) {
                    if (taskarray.contains("m-" + gathernum)) {
                        int index = taskarray.indexOf("m-" + gathernum);
                        cardid = BigDecimal.valueOf(Long.parseLong(taskarray.get(index + 2)));//工作号id
                        wpsid = Integer.parseInt(taskarray.get(index + 3));
                        productid = Integer.parseInt(taskarray.get(index + 4));
                        employee_id = Integer.parseInt(taskarray.get(index + 5));
                        workprocedureid = Integer.parseInt(taskarray.get(index + 6));
                        junction_id = Integer.parseInt(taskarray.get(index + 7));
                    }
                }
                liveData.setFmachine_id(BigDecimal.valueOf(Long.parseLong(weldid)));
                liveData.setFweld_no(weldnum);
                liveData.setFwelder_id(BigDecimal.valueOf(Long.parseLong(welderid)));
                liveData.setFwelder_itemid(BigDecimal.valueOf(Long.parseLong(welderitemid)));
                liveData.setFmachine_itemid(BigDecimal.valueOf(Long.parseLong(ins)));
                liveData.setFjunction_itemid(BigDecimal.valueOf(0));
                liveData.setFcard_id(cardid);//电子跟踪卡
                liveData.setFstep_id(BigDecimal.valueOf(workprocedureid));//工步id
                liveData.setFproduct_number_id(BigDecimal.valueOf(productid));//产品id
                liveData.setFwps_lib_id(BigDecimal.valueOf(wpsid));//工艺规程id
                liveData.setFemployee_id(BigDecimal.valueOf(employee_id));//工序id
                liveData.setFjunction_id(BigDecimal.valueOf(junction_id));
                if (server.outlinestatus.equals("A")) {
                    if (countoutlineA1 == 1) {
                        inSqloutlineA1 = inSqloutlineA + sqlJoint(liveData);
                    } else {
                        inSqloutlineA1 = inSqloutlineA1 + " " + inSqloutlineA + sqlJoint(liveData);
                    }
                    countoutlineA1++;
                } else if (server.outlinestatus.equals("B")) {
                    if (countoutlineB1 == 1) {
                        inSqloutlineB1 = inSqloutlineB + sqlJoint(liveData);
                    } else {
                        inSqloutlineB1 = inSqloutlineB1 + " " + inSqloutlineB + sqlJoint(liveData);
                    }
                    countoutlineB1++;
                }
                if (countoutlineA1 == 100 || countoutlineB1 == 100) {
                    if (inSqloutlineA1.length() != 0) {
                        inSqloutlineA1 = "insert all " + inSqloutlineA1 + "  select * from dual";
                        executeSQL(inSqloutlineA1);
                        countoutlineA1 = 1;
                        inSqloutlineA1 = "";
                    }
                    if (inSqloutlineB1.length() != 0) {
                        inSqloutlineB1 = "insert all " + inSqloutlineB1 + "  select * from dual";
                        executeSQL(inSqloutlineB1);
                        countoutlineB1 = 1;
                        inSqloutlineB1 = "";
                    }
                    workoutline = workoutline + 1;
                    if (workoutline == 5) {
                        workoutline = 1;
                    }
                }
                break;
            case 2:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);
                liveData.setFuploadDatetime(goodsC_date.toString());
                if ("137".equals(liveData.getFmachinemodel())) { //137：WB_A350P，128：CPVE500
                    liveData.setFelectricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFelectricity().toString())) / 10));
                    liveData.setFmax_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_electricity().toString())) / 10));
                    liveData.setFmin_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_electricity().toString())) / 10));
                }
                liveData.setFvoltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFvoltage().toString())) / 10));//实际电压
                liveData.setFmax_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_voltage().toString())) / 10));//最大电压
                liveData.setFmin_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_voltage().toString())) / 10));//最小电压
                liveData.setFwirefeedrate(BigDecimal.valueOf((Double.parseDouble(liveData.getFwirefeedrate().toString())) / 10));//送丝速度
                liveData.setFrateofflow(BigDecimal.valueOf((Double.parseDouble(liveData.getFrateofflow().toString())) / 10));//气体流量
                //焊工信息
                for (int i = 0; i < listarray1.size(); i += 3) {
                    if (("0090" + liveData.getFwelder_no()).equals(listarray1.get(i + 1))) {
                        weldernum = listarray1.get(i + 1);//焊工编号
                        welderid = listarray1.get(i);//焊工id
                        welderitemid = listarray1.get(i + 2);//焊工组织id
                        break;
                    }
                }
                //焊机信息
                for (int i = 0; i < listarray2.size(); i += 4) {
                    //采集模块编号判断
                    if (liveData.getFgather_no().equals(listarray2.get(i + 2))) {
                        weldid = listarray2.get(i);//焊机id
                        weldnum = listarray2.get(i + 1);//焊机固号
                        gathernum = listarray2.get(i + 2);//采集编号
                        ins = listarray2.get(i + 3);//焊机组织id
                        break;
                    }
                }
                if (gathernum.length() < 4) {
                    int len = 4 - gathernum.length();
                    for (int i = 0; i < len; i++) {
                        gathernum = "0" + gathernum;
                    }
                }
                //判断手持终端是否下发了任务信息
                if (!taskarray.isEmpty()) {
                    if (taskarray.contains("m-" + gathernum)) {
                        int index = taskarray.indexOf("m-" + gathernum);
                        cardid = BigDecimal.valueOf(Long.parseLong(taskarray.get(index + 2)));//工作号id
                        wpsid = Integer.parseInt(taskarray.get(index + 3));
                        productid = Integer.parseInt(taskarray.get(index + 4));
                        employee_id = Integer.parseInt(taskarray.get(index + 5));
                        workprocedureid = Integer.parseInt(taskarray.get(index + 6));
                        junction_id = Integer.parseInt(taskarray.get(index + 7));
                    }
                }
                liveData.setFmachine_id(BigDecimal.valueOf(Long.parseLong(weldid)));
                liveData.setFweld_no(weldnum);
                liveData.setFwelder_id(BigDecimal.valueOf(Long.parseLong(welderid)));
                liveData.setFwelder_itemid(BigDecimal.valueOf(Long.parseLong(welderitemid)));
                liveData.setFmachine_itemid(BigDecimal.valueOf(Long.parseLong(ins)));
                liveData.setFjunction_itemid(BigDecimal.valueOf(0));
                liveData.setFcard_id(cardid);//电子跟踪卡
                liveData.setFstep_id(BigDecimal.valueOf(workprocedureid));//工步id
                liveData.setFproduct_number_id(BigDecimal.valueOf(productid));//产品id
                liveData.setFwps_lib_id(BigDecimal.valueOf(wpsid));//工艺规程id
                liveData.setFemployee_id(BigDecimal.valueOf(employee_id));//工序id
                liveData.setFjunction_id(BigDecimal.valueOf(junction_id));
                if (server.outlinestatus.equals("A")) {
                    if (countoutlineA2 == 1) {
                        inSqloutlineA2 = inSqloutlineA + sqlJoint(liveData);
                    } else {
                        inSqloutlineA2 = inSqloutlineA2 + " " + inSqloutlineA + sqlJoint(liveData);
                    }
                    countoutlineA2++;
                } else if (server.outlinestatus.equals("B")) {
                    if (countoutlineB2 == 1) {
                        inSqloutlineB2 = inSqloutlineB + sqlJoint(liveData);
                    } else {
                        inSqloutlineB2 = inSqloutlineB2 + " " + inSqloutlineB + sqlJoint(liveData);
                    }
                    countoutlineB2++;
                }
                if (countoutlineA2 == 100 || countoutlineB2 == 100) {
                    //sql批量插入
                    if (inSqloutlineA2.length() != 0) {
                        inSqloutlineA2 = "insert all " + inSqloutlineA2 + "  select * from dual";
                        executeSQL(inSqloutlineA2);
                        countoutlineA2 = 1;
                        inSqloutlineA2 = "";
                    }
                    if (inSqloutlineB2.length() != 0) {
                        inSqloutlineB2 = "insert all " + inSqloutlineB2 + "  select * from dual";
                        executeSQL(inSqloutlineB2);
                        countoutlineB2 = 1;
                        inSqloutlineB2 = "";
                    }
                    workoutline = workoutline + 1;
                    if (workoutline == 5) {
                        workoutline = 1;
                    }
                }
                break;
            case 3:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);
                liveData.setFuploadDatetime(goodsC_date.toString());
                if ("137".equals(liveData.getFmachinemodel())) { //137：WB_A350P，128：CPVE500
                    liveData.setFelectricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFelectricity().toString())) / 10));
                    liveData.setFmax_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_electricity().toString())) / 10));
                    liveData.setFmin_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_electricity().toString())) / 10));
                }
                liveData.setFvoltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFvoltage().toString())) / 10));//实际电压
                liveData.setFmax_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_voltage().toString())) / 10));//最大电压
                liveData.setFmin_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_voltage().toString())) / 10));//最小电压
                liveData.setFwirefeedrate(BigDecimal.valueOf((Double.parseDouble(liveData.getFwirefeedrate().toString())) / 10));//送丝速度
                liveData.setFrateofflow(BigDecimal.valueOf((Double.parseDouble(liveData.getFrateofflow().toString())) / 10));//气体流量
                //焊工信息
                for (int i = 0; i < listarray1.size(); i += 3) {
                    if (("0090" + liveData.getFwelder_no()).equals(listarray1.get(i + 1))) {
                        weldernum = listarray1.get(i + 1);//焊工编号
                        welderid = listarray1.get(i);//焊工id
                        welderitemid = listarray1.get(i + 2);//焊工组织id
                        break;
                    }
                }
                //焊机信息
                for (int i = 0; i < listarray2.size(); i += 4) {
                    //采集模块编号判断
                    if (liveData.getFgather_no().equals(listarray2.get(i + 2))) {
                        weldid = listarray2.get(i);//焊机id
                        weldnum = listarray2.get(i + 1);//焊机固号
                        gathernum = listarray2.get(i + 2);//采集编号
                        ins = listarray2.get(i + 3);//焊机组织id
                        break;
                    }
                }
                if (gathernum.length() < 4) {
                    int len = 4 - gathernum.length();
                    for (int i = 0; i < len; i++) {
                        gathernum = "0" + gathernum;
                    }
                }
                //判断手持终端是否下发了任务信息
                if (!taskarray.isEmpty()) {
                    if (taskarray.contains("m-" + gathernum)) {
                        int index = taskarray.indexOf("m-" + gathernum);
                        cardid = BigDecimal.valueOf(Long.parseLong(taskarray.get(index + 2)));//工作号id
                        wpsid = Integer.parseInt(taskarray.get(index + 3));
                        productid = Integer.parseInt(taskarray.get(index + 4));
                        employee_id = Integer.parseInt(taskarray.get(index + 5));
                        workprocedureid = Integer.parseInt(taskarray.get(index + 6));
                        junction_id = Integer.parseInt(taskarray.get(index + 7));
                    }
                }
                liveData.setFmachine_id(BigDecimal.valueOf(Long.parseLong(weldid)));
                liveData.setFweld_no(weldnum);
                liveData.setFwelder_id(BigDecimal.valueOf(Long.parseLong(welderid)));
                liveData.setFwelder_itemid(BigDecimal.valueOf(Long.parseLong(welderitemid)));
                liveData.setFmachine_itemid(BigDecimal.valueOf(Long.parseLong(ins)));
                liveData.setFjunction_itemid(BigDecimal.valueOf(0));
                liveData.setFcard_id(cardid);//电子跟踪卡
                liveData.setFstep_id(BigDecimal.valueOf(workprocedureid));//工步id
                liveData.setFproduct_number_id(BigDecimal.valueOf(productid));//产品id
                liveData.setFwps_lib_id(BigDecimal.valueOf(wpsid));//工艺规程id
                liveData.setFemployee_id(BigDecimal.valueOf(employee_id));//工序id
                liveData.setFjunction_id(BigDecimal.valueOf(junction_id));
                if (server.outlinestatus.equals("A")) {
                    if (countoutlineA3 == 1) {
                        inSqloutlineA3 = inSqloutlineA + sqlJoint(liveData);
                    } else {
                        inSqloutlineA3 = inSqloutlineA3 + " " + inSqloutlineA + sqlJoint(liveData);
                    }
                    countoutlineA3++;
                } else if (server.outlinestatus.equals("B")) {
                    if (countoutlineB3 == 1) {
                        inSqloutlineB3 = inSqloutlineB + sqlJoint(liveData);
                    } else {
                        inSqloutlineB3 = inSqloutlineB3 + " " + inSqloutlineB + sqlJoint(liveData);
                    }
                    countoutlineB3++;
                }
                if (countoutlineA3 == 100 || countoutlineB3 == 100) {
                    //sql批量插入
                    if (inSqloutlineA1.length() != 0) {
                        inSqloutlineA3 = "insert all " + inSqloutlineA3 + "  select * from dual";
                        executeSQL(inSqloutlineA3);
                        countoutlineA3 = 1;
                        inSqloutlineA3 = "";
                    }
                    if (inSqloutlineB3.length() != 0) {
                        inSqloutlineB3 = "insert all " + inSqloutlineB3 + "  select * from dual";
                        executeSQL(inSqloutlineB3);
                        countoutlineB3 = 1;
                        inSqloutlineB3 = "";
                    }
                    workoutline = workoutline + 1;
                    if (workoutline == 5) {
                        workoutline = 1;
                    }
                }
            case 4:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);
                liveData.setFuploadDatetime(goodsC_date.toString());
                if ("137".equals(liveData.getFmachinemodel())) { //137：WB_A350P，128：CPVE500
                    liveData.setFelectricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFelectricity().toString())) / 10));
                    liveData.setFmax_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_electricity().toString())) / 10));
                    liveData.setFmin_electricity(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_electricity().toString())) / 10));
                }
                liveData.setFvoltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFvoltage().toString())) / 10));//实际电压
                liveData.setFmax_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmax_voltage().toString())) / 10));//最大电压
                liveData.setFmin_voltage(BigDecimal.valueOf((Double.parseDouble(liveData.getFmin_voltage().toString())) / 10));//最小电压
                liveData.setFwirefeedrate(BigDecimal.valueOf((Double.parseDouble(liveData.getFwirefeedrate().toString())) / 10));//送丝速度
                liveData.setFrateofflow(BigDecimal.valueOf((Double.parseDouble(liveData.getFrateofflow().toString())) / 10));//气体流量
                //焊工信息
                for (int i = 0; i < listarray1.size(); i += 3) {
                    if (("0090" + liveData.getFwelder_no()).equals(listarray1.get(i + 1))) {
                        weldernum = listarray1.get(i + 1);//焊工编号
                        welderid = listarray1.get(i);//焊工id
                        welderitemid = listarray1.get(i + 2);//焊工组织id
                        break;
                    }
                }
                //焊机信息
                for (int i = 0; i < listarray2.size(); i += 4) {
                    //采集模块编号判断
                    if (liveData.getFgather_no().equals(listarray2.get(i + 2))) {
                        weldid = listarray2.get(i);//焊机id
                        weldnum = listarray2.get(i + 1);//焊机固号
                        gathernum = listarray2.get(i + 2);//采集编号
                        ins = listarray2.get(i + 3);//焊机组织id
                        break;
                    }
                }
                if (gathernum.length() < 4) {
                    int len = 4 - gathernum.length();
                    for (int i = 0; i < len; i++) {
                        gathernum = "0" + gathernum;
                    }
                }
                //判断手持终端是否下发了任务信息
                if (!taskarray.isEmpty()) {
                    if (taskarray.contains("m-" + gathernum)) {
                        int index = taskarray.indexOf("m-" + gathernum);
                        cardid = BigDecimal.valueOf(Long.parseLong(taskarray.get(index + 2)));//工作号id
                        wpsid = Integer.parseInt(taskarray.get(index + 3));
                        productid = Integer.parseInt(taskarray.get(index + 4));
                        employee_id = Integer.parseInt(taskarray.get(index + 5));
                        workprocedureid = Integer.parseInt(taskarray.get(index + 6));
                        junction_id = Integer.parseInt(taskarray.get(index + 7));
                    }
                }
                liveData.setFmachine_id(BigDecimal.valueOf(Long.parseLong(weldid)));
                liveData.setFweld_no(weldnum);
                liveData.setFwelder_id(BigDecimal.valueOf(Long.parseLong(welderid)));
                liveData.setFwelder_itemid(BigDecimal.valueOf(Long.parseLong(welderitemid)));
                liveData.setFmachine_itemid(BigDecimal.valueOf(Long.parseLong(ins)));
                liveData.setFjunction_itemid(BigDecimal.valueOf(0));
                liveData.setFcard_id(cardid);//电子跟踪卡
                liveData.setFstep_id(BigDecimal.valueOf(workprocedureid));//工步id
                liveData.setFproduct_number_id(BigDecimal.valueOf(productid));//产品id
                liveData.setFwps_lib_id(BigDecimal.valueOf(wpsid));//工艺规程id
                liveData.setFemployee_id(BigDecimal.valueOf(employee_id));//工序id
                liveData.setFjunction_id(BigDecimal.valueOf(junction_id));
                if (server.outlinestatus.equals("A")) {
                    if (countoutlineA4 == 1) {
                        inSqloutlineA4 = inSqloutlineA + sqlJoint(liveData);
                    } else {
                        inSqloutlineA4 = inSqloutlineA4 + " " + inSqloutlineA + sqlJoint(liveData);
                    }
                    countoutlineA4++;
                } else if (server.outlinestatus.equals("B")) {
                    if (countoutlineB4 == 1) {
                        inSqloutlineB4 = inSqloutlineB + sqlJoint(liveData);
                    } else {
                        inSqloutlineB4 = inSqloutlineB4 + " " + inSqloutlineB + sqlJoint(liveData);
                    }
                    countoutlineB4++;
                }
                if (countoutlineA4 == 100 || countoutlineB4 == 100) {
                    //sql批量插入
                    if (inSqloutlineA4.length() != 0) {
                        inSqloutlineA4 = "insert all " + inSqloutlineA4 + "  select * from dual";
                        executeSQL(inSqloutlineA4);
                        countoutlineA4 = 1;
                        inSqloutlineA4 = "";
                    }
                    if (inSqloutlineB4.length() != 0) {
                        inSqloutlineB4 = "insert all " + inSqloutlineB4 + "  select * from dual";
                        executeSQL(inSqloutlineB4);
                        countoutlineB4 = 1;
                        inSqloutlineB4 = "";
                    }
                    workoutline = workoutline + 1;
                    if (workoutline == 5) {
                        workoutline = 1;
                    }
                }
                break;
        }
    }

    public void DB_Connectionmysqlplc(ArrayList<String> listarrayplc, int machine, int d1000, int d1001, int d1002, int d1003, int d1004, int d1005, int d1006, int d1007, int d1008, int d1009, int d1010, int d1011, int d1012, int d1013, int d1014, int d1015, int d1016, int d1017, int d1018, int d1019, int d1020, int d1021, int d1022, int d1023, int d1024, int d1025, int d1026, int d1027, int d1028, int d1029, int d1030, int d1031, int d1032, int d1033, int d1034, int d1035, int d1036, int d1037, int d1038, int d1039, int d1040, int d1041, int d1042, int d1043, int d1044, int d1045, int d1046, int d1047, int d1048, int d1049, int d1050, int d1051, int d1052, int d1053, int d1054, int d1055, int d1056, int d1057, int d1058, int d1059, int d1060, int d1061, int d1062, int d1063, int d1064, int d1065, int d1066, int d1067, int d1068, int d1069, int d1070, int d1071, int d1072) {

        Date date;
        String nowTime;
        Timestamp goodsC_date;
        int plcstatus;
        Integer welderid = null;
        switch (Integer.valueOf(workplc)) {
            case 1:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);

                if (listarrayplc.contains(Integer.toString(machine))) {
                    welderid = Integer.valueOf(listarrayplc.get(listarrayplc.indexOf(Integer.toString(machine)) + 1));
                } else {
                    welderid = 0;
                }

                if (Integer.valueOf(d1016) != 0 || Integer.valueOf(d1026) != 0 || Integer.valueOf(d1036) != 0 || Integer.valueOf(d1046) != 0) {
                    plcstatus = 3;
                } else {
                    plcstatus = 0;
                }

                if (countplc1 == 1) {
                    inSqlplc1 = inSqlplc + "('" + welderid + "','" + machine + "','" + "0" + "','" + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
                } else {
                    inSqlplc1 = inSqlplc1 + ",('" + welderid + "','" + machine + "','" + "0" + "','" + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
                }

                countplc1++;

                if (countplc1 == 100) {

                    Connection connection = null;
                    Statement statement = null;
                    try {
                        connection = OracleDBConnection.getConnection();
                        statement = connection.createStatement();
                        statement.executeUpdate(inSqlplc1);
                        workplc = workplc + 1;
                        if (workplc == 5) {
                            workplc = 1;
                        }
                        countplc1 = 1;
                        inSqlplc1 = "";
                    } catch (SQLException e) {
                        countplc1 = 1;
                        inSqlplc1 = "";
                        System.out.println("Broken insert");
                        e.printStackTrace();
                    } finally {
                        OracleDBConnection.close(connection, statement, null);
                    }
                }
                break;

            case 2:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);

                if (listarrayplc.contains(Integer.toString(machine))) {
                    welderid = Integer.valueOf(listarrayplc.get(listarrayplc.indexOf(Integer.toString(machine)) + 1));
                } else {
                    welderid = 0;
                }

                if (Integer.valueOf(d1016) != 0 || Integer.valueOf(d1026) != 0 || Integer.valueOf(d1036) != 0 || Integer.valueOf(d1046) != 0) {
                    plcstatus = 3;
                } else {
                    plcstatus = 0;
                }

                if (countplc2 == 1) {
                    inSqlplc2 = inSqlplc + "('" + welderid + "','" + machine + "','" + "0" + "','" + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
                } else {
                    inSqlplc2 = inSqlplc2 + ",('" + welderid + "','" + machine + "','" + "0" + "','" + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
                }

                countplc2++;

                if (countplc2 == 100) {
                    Connection connection = null;
                    Statement statement = null;
                    try {
                        connection = OracleDBConnection.getConnection();
                        statement = connection.createStatement();
                        statement.executeUpdate(inSqlplc2);
                        workplc = workplc + 1;
                        if (workplc == 5) {
                            workplc = 1;
                        }
                        countplc2 = 1;
                        inSqlplc2 = "";
                    } catch (SQLException e) {
                        countplc2 = 1;
                        inSqlplc2 = "";
                        System.out.println("Broken insert");
                        e.printStackTrace();
                    } finally {
                        OracleDBConnection.close(connection, statement, null);
                    }
                }
                break;

            case 3:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);

                if (listarrayplc.contains(Integer.toString(machine))) {
                    welderid = Integer.valueOf(listarrayplc.get(listarrayplc.indexOf(Integer.toString(machine)) + 1));
                } else {
                    welderid = 0;
                }

                if (Integer.valueOf(d1016) != 0 || Integer.valueOf(d1026) != 0 || Integer.valueOf(d1036) != 0 || Integer.valueOf(d1046) != 0) {
                    plcstatus = 3;
                } else {
                    plcstatus = 0;
                }

                if (countplc3 == 1) {
                    inSqlplc3 = inSqlplc + "('" + welderid + "','" + machine + "','" + "0" + "','" + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
                } else {
                    inSqlplc3 = inSqlplc3 + ",('" + welderid + "','" + machine + "','" + "0" + "','" + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
                }

                countplc3++;

                if (countplc3 == 100) {
                    Connection connection = null;
                    Statement statement = null;
                    try {
                        connection = OracleDBConnection.getConnection();
                        statement = connection.createStatement();
                        statement.executeUpdate(inSqlplc3);
                        workplc = workplc + 1;
                        if (workplc == 5) {
                            workplc = 1;
                        }
                        countplc3 = 1;
                        inSqlplc3 = "";
                    } catch (SQLException e) {
                        countplc3 = 1;
                        inSqlplc3 = "";
                        System.out.println("Broken insert");
                        e.printStackTrace();
                    } finally {
                        OracleDBConnection.close(connection, statement, null);
                    }
                }
                break;

            case 4:
                date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                goodsC_date = Timestamp.valueOf(nowTime);

                if (listarrayplc.contains(Integer.toString(machine))) {
                    welderid = Integer.valueOf(listarrayplc.get(listarrayplc.indexOf(Integer.toString(machine)) + 1));
                } else {
                    welderid = 0;
                }

                if (Integer.valueOf(d1016) != 0 || Integer.valueOf(d1026) != 0 || Integer.valueOf(d1036) != 0 || Integer.valueOf(d1046) != 0) {
                    plcstatus = 3;
                } else {
                    plcstatus = 0;
                }

                if (countplc4 == 1) {
                    inSqlplc4 = inSqlplc + "('" + welderid + "','" + machine + "','" + "0" + "','" + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
                } else {
                    inSqlplc4 = inSqlplc4 + ",('" + welderid + "','" + machine + "','" + "0" + "','" + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
                }

                countplc4++;

                if (countplc4 == 100) {
                    Connection connection = null;
                    Statement statement = null;
                    try {
                        connection = OracleDBConnection.getConnection();
                        statement = connection.createStatement();
                        statement.executeUpdate(inSqlplc4);
                        workplc = workplc + 1;
                        if (workplc == 5) {
                            workplc = 1;
                        }
                        countplc4 = 1;
                        inSqlplc4 = "";
                    } catch (SQLException e) {
                        countplc4 = 1;
                        inSqlplc4 = "";
                        System.out.println("Broken insert");
                        e.printStackTrace();
                    } finally {
                        OracleDBConnection.close(connection, statement, null);
                    }
                }
                break;
        }
    }
}