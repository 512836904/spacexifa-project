package com;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.net.Socket;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 前端实时数据解析
 */
public class Websocket {

    private static String strsend = "";
    public static ArrayList<String> listarray1;
    public static ArrayList<String> listarray2;
    public static ArrayList<String> listarray3;
    public static ArrayList<String> taskarray = new ArrayList<String>();
    public static MyMqttClient mqtt;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
    private static final SimpleDateFormat sdftime = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

    public static class Websocketbase implements Runnable {

        private String str;

        public Websocketbase(String str) {
            this.str = str;
        }

        @Override
        public void run() {
            Date time;
            Timestamp timesql = null;
            String cardid = "0000";
            String wpsid = "0000";
            String productid = "0000";
            String workprocedureid = "0000";
            String workstepid = "0000";
            if (str.length() == 596) {
                try {
                    //前端实时数据处理
                    String check1 = str.substring(0, 2);
                    String check11 = str.substring(594, 596);
                    if (check1.equals("7E") && check11.equals("7D")) {
                        String history = Integer.valueOf(str.substring(10, 12), 16).toString();//判断当前数据是否是历史数据
                        if(Integer.valueOf(history)==34){
                            String weldid = "0000";//焊机id
                            String welderno = String.valueOf(Long.parseLong(str.substring(40, 44), 16));//焊工号
                            if (welderno.length() < 5) {
                                int lenth = 5 - welderno.length();
                                for (int i = 0; i < lenth; i++) {
                                    welderno = "0" + welderno;
                                }
                            }
                            String whiteListVersion = String.valueOf(Long.parseLong(str.substring(26, 30), 16));//白名单版本号
                            if (whiteListVersion.length() < 4) {
                                int lenth = 4 - whiteListVersion.length();
                                for (int i = 0; i < lenth; i++) {
                                    whiteListVersion = "0" + whiteListVersion;
                                }
                            }
                            String gatherno = String.valueOf(Long.parseLong(str.substring(16, 20), 16));//采集模块编号
                            if (gatherno.length() < 4) {
                                int lenth = 4 - gatherno.length();
                                for (int i = 0; i < lenth; i++) {
                                    gatherno = "0" + gatherno;
                                }
                            }
                            String itemins = Integer.valueOf(str.substring(592, 594)).toString();//组织id
                            if (itemins.length() < 4) {
                                int lenth = 4 - itemins.length();
                                for (int i = 0; i < lenth; i++) {
                                    itemins = "0" + itemins;
                                }
                            }
                            String weldmodel = Integer.valueOf(str.substring(12, 14), 16).toString();//焊机型号
                            if (weldmodel.length() < 4) {
                                int lenth = 4 - weldmodel.length();
                                for (int i = 0; i < lenth; i++) {
                                    weldmodel = "0" + weldmodel;
                                }
                            }

                            for (int a = 0; a < 365; a += 182) {
                                try {
                                    String ins = "0000";//焊机组织id
                                    String welderid = "0";
                                    String junctionNo = Long.valueOf(str.substring(76 + a, 84 + a), 16).toString();//焊口号(焊缝编号)
                                    if (junctionNo.length() < 8) {
                                        int lenth = 8 - junctionNo.length();
                                        for (int i = 0; i < lenth; i++) {
                                            junctionNo = "0" + junctionNo;
                                        }
                                    } else {
                                        junctionNo = "00000001";
                                    }
                                    String electricity = Integer.valueOf(str.substring(56 + a, 60 + a), 16).toString();//实际电流
                                    if (electricity.length() < 4) {
                                        int lenth = 4 - electricity.length();
                                        for (int i = 0; i < lenth; i++) {
                                            electricity = "0" + electricity;
                                        }
                                    }
                                    String voltage = Integer.valueOf(str.substring(60 + a, 64 + a), 16).toString();//实际电压
                                    if (voltage.length() < 4) {
                                        int lenth = 4 - voltage.length();
                                        for (int i = 0; i < lenth; i++) {
                                            voltage = "0" + voltage;
                                        }
                                    }
                                    String speed = Integer.valueOf(str.substring(64 + a, 68 + a), 16).toString();//送丝速度
                                    if (speed.length() < 4) {
                                        int lenth = 4 - speed.length();
                                        for (int i = 0; i < lenth; i++) {
                                            speed = "0" + speed;
                                        }
                                    }
                                    String setelectricity = Integer.valueOf(str.substring(68 + a, 72 + a), 16).toString();//给定电流
                                    if (setelectricity.length() < 4) {
                                        int lenth = 4 - setelectricity.length();
                                        for (int i = 0; i < lenth; i++) {
                                            setelectricity = "0" + setelectricity;
                                        }
                                    }
                                    String setvoltage = Integer.valueOf(str.substring(72 + a, 76 + a), 16).toString();//给定电压
                                    if (setvoltage.length() < 4) {
                                        int lenth = 4 - setvoltage.length();
                                        for (int i = 0; i < lenth; i++) {
                                            setvoltage = "0" + setvoltage;
                                        }
                                    }
                                    String status = Integer.valueOf(str.substring(84 + a, 86 + a), 16).toString();//报警信息
                                    if (status.length() < 2) {
                                        int lenth = 2 - status.length();
                                        for (int i = 0; i < lenth; i++) {
                                            status = "0" + status;
                                        }
                                    }
                                    String year = Integer.valueOf(str.substring(44 + a, 46 + a), 16).toString();
                                    String month = Integer.valueOf(str.substring(46 + a, 48 + a), 16).toString();
                                    String day = Integer.valueOf(str.substring(48 + a, 50 + a), 16).toString();
                                    String hour = Integer.valueOf(str.substring(50 + a, 52 + a), 16).toString();
                                    String minute = Integer.valueOf(str.substring(52 + a, 54 + a), 16).toString();
                                    String second = Integer.valueOf(str.substring(54 + a, 56 + a), 16).toString();
                                    String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                                    try {
                                        String date = year + "-" + month + "-" + day;
                                        String nowdatetime = sdf.format(System.currentTimeMillis());//当前系统时间
                                        if (!date.equals(nowdatetime)) {
                                            strdate = sdftime.format(System.currentTimeMillis());
                                        }
                                        time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
                                        timesql = new Timestamp(time.getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    String channel = Integer.valueOf(str.substring(106 + a, 108 + a), 16).toString();//通道号
                                    if (channel.length() < 4) {
                                        int lenth = 4 - channel.length();
                                        for (int i = 0; i < lenth; i++) {
                                            channel = "0" + channel;
                                        }
                                    }
                                    String maxelectricity = Integer.valueOf(str.substring(90 + a, 94 + a), 16).toString();
                                    if (maxelectricity.length() < 4) {
                                        int lenth = 4 - maxelectricity.length();
                                        for (int i = 0; i < lenth; i++) {
                                            maxelectricity = "0" + maxelectricity;
                                        }
                                    }
                                    String minelectricity = Integer.valueOf(str.substring(94 + a, 98 + a), 16).toString();
                                    if (minelectricity.length() < 4) {
                                        int lenth = 4 - minelectricity.length();
                                        for (int i = 0; i < lenth; i++) {
                                            minelectricity = "0" + minelectricity;
                                        }
                                    }
                                    String maxvoltage = Integer.valueOf(str.substring(98 + a, 102 + a), 16).toString();
                                    if (maxvoltage.length() < 4) {
                                        int lenth = 4 - maxvoltage.length();
                                        for (int i = 0; i < lenth; i++) {
                                            maxvoltage = "0" + maxvoltage;
                                        }
                                    }
                                    String minvoltage = Integer.valueOf(str.substring(102 + a, 106 + a), 16).toString();
                                    if (minvoltage.length() < 4) {
                                        int lenth = 4 - minvoltage.length();
                                        for (int i = 0; i < lenth; i++) {
                                            minvoltage = "0" + minvoltage;
                                        }
                                    }
                                    //焊工信息
                                    for (int i = 0; i < listarray1.size(); i += 3) {
                                        if (String.valueOf(("0090" + welderno)).equals(listarray1.get(i + 1))) {
                                            welderno = listarray1.get(i + 1);//焊工编号
                                            welderid = listarray1.get(i);//焊工id
                                            break;
                                        }
                                    }
                                    //焊机信息
                                    for (int i = 0; i < listarray2.size(); i += 4) {
                                        //采集模块编号判断
                                        if (String.valueOf(Integer.parseInt(gatherno)).equals(listarray2.get(i + 2))) {
                                            weldid = listarray2.get(i);//焊机id
                                            ins = listarray2.get(i + 3);//焊机组织id
                                            break;
                                        }
                                    }
                                    //判断手持终端是否下发了任务信息
                                    if (!taskarray.isEmpty()) {
                                        if (taskarray.contains("m-" + gatherno)) {
                                            int index = taskarray.indexOf("m-" + gatherno);
                                            //welderid = taskarray.get(index + 1);//焊工id
                                            cardid = taskarray.get(index + 2);//电子跟踪卡id
                                            wpsid = taskarray.get(index + 3);//工艺id
                                            productid = taskarray.get(index + 4);//产品号id
                                            workprocedureid = taskarray.get(index + 6);//工步号id
                                            workstepid = taskarray.get(index + 7);//焊缝号id
                                        }
                                    }
                                    if (welderid.length() < 4) {
                                        int lenth = 4 - welderid.length();
                                        for (int m = 0; m < lenth; m++) {
                                            welderid = "0" + welderid;
                                        }
                                    }
                                    if (weldid.length() < 4) {
                                        int lenth = 4 - weldid.length();
                                        for (int m = 0; m < lenth; m++) {
                                            weldid = "0" + weldid;
                                        }
                                    }
                                    if (cardid.length() < 4) {
                                        int lenth = 4 - cardid.length();
                                        for (int i1 = 0; i1 < lenth; i1++) {
                                            cardid = "0" + cardid;
                                        }
                                    }

                                    if (wpsid.length() < 4) {
                                        int lenth = 4 - wpsid.length();
                                        for (int i1 = 0; i1 < lenth; i1++) {
                                            wpsid = "0" + wpsid;
                                        }
                                    }
                                    if (productid.length() < 4) {
                                        int lenth = 4 - productid.length();
                                        for (int i1 = 0; i1 < lenth; i1++) {
                                            productid = "0" + productid;
                                        }
                                    }
                                    if (workprocedureid.length() < 4) {
                                        int lenth = 4 - workprocedureid.length();
                                        for (int i1 = 0; i1 < lenth; i1++) {
                                            workprocedureid = "0" + workprocedureid;
                                        }
                                    }
                                    if (workstepid.length() < 4) {
                                        int lenth = 4 - workstepid.length();
                                        for (int i1 = 0; i1 < lenth; i1++) {
                                            workstepid = "0" + workstepid;
                                        }
                                    }
                                    if (ins.length() < 4) {
                                        int lenth = 4 - ins.length();
                                        for (int i = 0; i < lenth; i++) {
                                            ins = "0" + ins;
                                        }
                                    }
                                    String gasflow = Integer.valueOf(str.substring(108 + a, 112 + a), 16).toString();
                                    if (gasflow.length() < 4) {
                                        int lenth = 4 - gasflow.length();
                                        for (int i = 0; i < lenth; i++) {
                                            gasflow = "0" + gasflow;
                                        }
                                    }
                                    String weld_speed = Integer.valueOf(str.substring(112 + a, 116 + a), 16).toString();
                                    if (weld_speed.length() < 4) {
                                        int lenth = 4 - weld_speed.length();
                                        for (int i = 0; i < lenth; i++) {
                                            weld_speed = "0" + weld_speed;
                                        }
                                    }
                                    String lon_air_flow = Integer.valueOf(str.substring(116 + a, 120 + a), 16).toString();
                                    if (lon_air_flow.length() < 4) {
                                        int lenth = 4 - lon_air_flow.length();
                                        for (int i = 0; i < lenth; i++) {
                                            lon_air_flow = "0" + lon_air_flow;
                                        }
                                    }
                                    String hatwire_current = Integer.valueOf(str.substring(120 + a, 124 + a), 16).toString();
                                    if (hatwire_current.length() < 4) {
                                        int lenth = 4 - hatwire_current.length();
                                        for (int i = 0; i < lenth; i++) {
                                            hatwire_current = "0" + hatwire_current;
                                        }
                                    }
                                    String laser_power = Integer.valueOf(str.substring(136 + a, 140 + a), 16).toString();
                                    if (laser_power.length() < 4) {
                                        int lenth = 4 - laser_power.length();
                                        for (int i = 0; i < lenth; i++) {
                                            laser_power = "0" + laser_power;
                                        }
                                    }

                                    strsend = strsend + welderid + weldid + gatherno + junctionNo + gasflow + ins + itemins + weldmodel + status + electricity +
                                            voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel + speed + cardid +
                                            wpsid + productid + workprocedureid + workstepid + whiteListVersion + lon_air_flow + hatwire_current + laser_power;

                                    String strstrsend = welderid + weldid + gatherno + junctionNo + gasflow + ins + itemins + weldmodel + status + electricity +
                                            voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel + speed + cardid +
                                            wpsid + productid + workprocedureid + workstepid + whiteListVersion + lon_air_flow + hatwire_current + laser_power;
                                    //焊工id、焊机id、采集编号、焊口号(8位)、保护气流量、焊机组织id、组织id（配置文件）、焊机型号（值）、报警信息、焊接电流、焊接电压、给定电流、给定电压、
                                    //焊机工作时间、最大电流、最小电流、最大电压、最小电压（87-91）、通道号、送丝速度、电子跟踪卡id（工作号id/工票id）【99-103】、
                                    //工艺id（103-107）、产品号id、工步号id、焊缝号id、焊机速度【修改：白名单版本号（119-123）】、离子气流量、热丝电流、激光功率
                                    //0000000000610000000100000000001700000000000000019001902021-01-12 16:57:32.0024001400240014000000000000000000000000000000000000000000000
                                    //0000000001140000000100000000001700000000000000019001902021-01-12 16:57:32.0024001400240014000000000000000000000000000000000000000000000
                                    //0000000000670000000100000000001700000000000000019001902021-01-12 16:57:32.0024001400240014000000000000000000000000000000000000000000000
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("采集编号：" + str.substring(16, 20));
                                    //String junctionNo = Integer.valueOf(str.substring(76 + a, 84 + a), 16).toString();
                                    System.out.println("字节码解析异常：" + e);
                                    continue;
                                }
                            }
                                //MQTT处理,发送到前端(length:405)
                                mqtt.publishMessage("weldmesrealdata", strsend, 0);
                                strsend = "";
                            }else{
                            strsend = "";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
