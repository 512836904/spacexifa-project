package com;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.CharsetUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Server implements Runnable {

    //private List<Handler> handlers = new ArrayList<Handler>();
    //public com.MyMqttClient mqtt = new com.MyMqttClient();
    public MyMqttClient mqtt = new MyMqttClient(this);
    public static final String SERVERIP = "121.196.222.216";
    public static final int SERVERPORT = 5555;
    public static final int SERVERPORTWEB = 5554;
    public static String oracleUser = "";
    public static String oraclePassword = "";
    public String str = "";
    public Socket socket = null;
    public Socket websocketlink = null;
    public ServerSocket serverSocket = null;
    public boolean webtype = false;
    public int sqlwritetype = 0;
    public int websendtype = 0;
    public int sockettype = 0;
    public String ip = null;
    public String ip1 = null;
    public String connet1 = "jdbc:oracle:thin:@";
    public String connet2 = ":1521/orcl";
    public String connet3 = "?user=";
    public String connet4 = "&password=";
    public String connet5 = "&useUnicode=true&autoReconnect=true&characterEncoding=UTF8";
    public String connet;
    public byte b[];
    public DB_Connectioncode check;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public ArrayList<String> listarray4 = new ArrayList<String>();
    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
    public HashMap<String, SocketChannel> websocketlist = new HashMap<>();
    public HashMap<String, SocketChannel> clientList = new HashMap<>();
    public int socketcount = 0;
    public int websocketcount = 0;
    public int clientcount = 0;
    public Selector selector = null;
    public ServerSocketChannel ssc = null;
    public Client client = new Client(this);
    public NettyServerHandler NS = new NettyServerHandler(this);
    private NettyWebSocketHandler NWS = new NettyWebSocketHandler(this);
    private Connection c;
    public java.sql.Connection conn = null;
    public java.sql.Statement stmt = null;
    private long time;
    private long time1;
    private ArrayList<String> dbdata;
    public String outlinestatus = "A";
    public static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    //创建缓存线程池，处理PC实时数据
    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    public String getconnet() {
        return connet;
    }
    public ArrayList<String> getlistarray1() {
        return listarray1;
    }
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");;


    public void run() {

        //读取IPconfig配置文件获取ip地址和数据库配置
        try {
            File file = new File("PC/IPconfig.txt");
            String filePath = file.getCanonicalPath();

            FileInputStream in = new FileInputStream(filePath);
            InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
            BufferedReader bufReader = new BufferedReader(inReader);
            String line = null;
            int writetime = 0;

            while ((line = bufReader.readLine()) != null) {
                if (writetime == 0) {
                    ip = line;
                    writetime++;
                } else {
                    ip1 = line;
                    writetime = 0;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] values = ip.split(",");
        connet = connet1 + values[0] + connet2;
        oracleUser = values[2];
        oraclePassword = values[3];

        //开启线程每小时更新三张状态表
        Date date = new Date();
        String nowtime = DateTools.format("HH:mm:ss", date);
        String[] timesplit = nowtime.split(":");
        String hour = timesplit[0];

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour)); // 控制时
        calendar.set(Calendar.MINUTE, 17);    // 控制分
        calendar.set(Calendar.SECOND, 00);    // 控制秒
        Date a = new Date();
        time = calendar.getTime().getTime() / 1000 - a.getTime() / 1000;

        //断网续传数据每天更新三张状态表
        Date date1 = new Date();
        String nowtime1 = DateTools.format("yyyy-MM-dd", date);
        String[] timesplit1 = nowtime1.split("-");
        String day1 = timesplit1[2];

        Calendar calendar1 = Calendar.getInstance();

        calendar1.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day1) + 1); // 控制天
        calendar1.set(Calendar.HOUR_OF_DAY, 00); // 控制时
        calendar1.set(Calendar.MINUTE, 00);    // 控制分
        calendar1.set(Calendar.SECOND, 00);    // 控制秒
        Date a1 = new Date();
        time1 = (calendar1.getTime().getTime() - a1.getTime()) / 1000;

        /**
         * 周期性线程池，每天更新四张状态表
         */
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String Afirsttime = null;
                String Bfirsttime = null;
                String Alasttime = null;
                String Blasttime = null;
                Connection connection = null;
                Statement statement = null;
                try {
                    connection = OracleDBConnection.getConnection();
                    statement = connection.createStatement();
                    //基本版
                    //获取上次统计时间，为空插入赋默认值
                    Date date = new Date();
                    String nowtimefor = DateTools.format("yyyy-MM-dd", date);
                    String nowtime = DateTools.format("HH:mm:ss", date);
                    String[] timesplit = nowtime.split(":");
                    String hour = timesplit[0];
                    String time2 = nowtimefor + " " + hour + ":00:00";
                    Date d1 = new Date((DateTools.parse("yyyy-MM-dd HH:mm:ss", time2).getTime()) - 3600000);

                    String sqlAfirst = "SELECT * FROM (SELECT tb_dataA.FWELDTIME FROM tb_dataA ORDER BY tb_dataA.FWELDTIME desc) WHERE 1 >= ROWNUM";
                    String sqlBfirst = "SELECT * FROM (SELECT tb_dataB.FWELDTIME FROM tb_dataB ORDER BY tb_dataB.FWELDTIME desc) WHERE 1 >= ROWNUM";
                    String sqlAlast = "SELECT * FROM  (SELECT tb_dataA.FWELDTIME FROM tb_dataA ORDER BY tb_dataA.FWELDTIME asc) WHERE 1 >= ROWNUM";
                    String sqlBlast = "SELECT * FROM  (SELECT tb_dataB.FWELDTIME FROM tb_dataB ORDER BY tb_dataB.FWELDTIME asc) WHERE 1 >= ROWNUM";
                    String sqlAtrun = "TRUNCATE TABLE tb_dataA";
                    String sqlBtrun = "TRUNCATE TABLE tb_dataB";

                    //清空实时数据表
                    String sqltruncate = "TRUNCATE TABLE TB_LIVE_DATA";
                    statement.executeQuery(sqltruncate);

                    if (outlinestatus.equals("A")) {
                        ResultSet rs1 = statement.executeQuery(sqlAfirst);
                        while (rs1.next()) {
                            Alasttime = rs1.getString("FWELDTIME");
                        }
                        ResultSet rs3 = statement.executeQuery(sqlAlast);
                        while (rs3.next()) {
                            Afirsttime = rs3.getString("FWELDTIME");
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (null != Alasttime && !"null".equals(Alasttime) && null != Afirsttime && !"null".equals(Afirsttime)) {
                            long Alast = sdf.parse(Alasttime).getTime() + 1000;
                            long Afirst = sdf.parse(Afirsttime).getTime();
                            //统计四张状态表
                            for (long i = Afirst; i < Alast; i += 3600000) {
                                String datebuf1 = DateTools.format("yyyy-MM-dd HH:mm:ss", new Date(i));
                                String datebuf2 = DateTools.format("yyyy-MM-dd HH:mm:ss", new Date(i + 3600000));

                                String sqlstandby = "INSERT INTO tb_standby(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,frateofflow,fstandbytime," +
                                        "fstarttime,fendtime,fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage," +
                                        "fwelder_itemid,fjunction_itemid,fmachine_itemid,fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,fstatus) " +
                                        "SELECT td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,AVG(td.felectricity),AVG(td.fvoltage),AVG(td.frateofflow)," +
                                        "COUNT(td.fid),to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss'),td.fwelder_no," +
                                        "td.fjunction_no,td.fweld_no,td.fchannel,td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid," +
                                        "td.fjunction_itemid,td.fmachine_itemid,AVG(td.fwirefeedrate),td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus FROM TB_DATAA td " +
                                        "WHERE td.fstatus = '0' " +
                                        "AND (td.FWELDTIME BETWEEN to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss') AND to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss')) " +
                                        "GROUP BY td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,td.fwelder_no,td.fjunction_no,td.fweld_no,td.fchannel," +
                                        "td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid," +
                                        "td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus";

                                String sqlwork = "INSERT INTO tb_work(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,frateofflow,fworktime,fstarttime,fendtime," +
                                        "fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid,fjunction_itemid," +
                                        "fmachine_itemid,fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,fstatus) " +
                                        "SELECT td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,AVG(td.felectricity),AVG(td.fvoltage),AVG(td.frateofflow),COUNT(td.fid)," +
                                        "to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss'),td.fwelder_no," +
                                        "td.fjunction_no,td.fweld_no,td.fchannel,td.fmax_electricity,td.fmin_electricity,td.fmax_voltage," +
                                        "td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid,AVG(td.fwirefeedrate),td.fmachinemodel," +
                                        "td.fwirediameter,td.fmaterialgas,td.fstatus FROM TB_DATAA td " +
                                        "WHERE (td.fstatus = '3' OR fstatus= '5' OR fstatus= '7' OR fstatus= '99') " +
                                        "AND td.FWELDTIME BETWEEN to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss') AND to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss') " +
                                        "GROUP BY td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,td.fwelder_no,td.fjunction_no,td.fweld_no,td.fchannel," +
                                        "td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid," +
                                        "td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus";

                                String sqlwarn = "INSERT INTO tb_warn(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,frateofflow,fwarntime,fstarttime," +
                                        "fendtime,fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid," +
                                        "fjunction_itemid,fmachine_itemid,fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,fstatus) " +
                                        "SELECT td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,AVG(td.felectricity),AVG(td.fvoltage),AVG(td.frateofflow),COUNT(td.fid)," +
                                        "to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss'),td.fwelder_no,td.fjunction_no," +
                                        "td.fweld_no,td.fchannel,td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid," +
                                        "td.fmachine_itemid,AVG(td.fwirefeedrate),td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus FROM TB_DATAA td " +
                                        "WHERE td.fstatus != '0' AND td.fstatus != '3' AND td.fstatus != '5' AND td.fstatus != '7' " +
                                        "AND td.FWELDTIME BETWEEN to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss') AND to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss') " +
                                        "GROUP BY td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,td.fwelder_no,td.fjunction_no,td.fweld_no,td.fchannel," +
                                        "td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid," +
                                        "td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus";


                                String sqlalarm = "INSERT INTO tb_alarm(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,frateofflow,falarmtime,fstarttime,fendtime," +
                                        "fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid,fjunction_itemid," +
                                        "fmachine_itemid,fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,fstatus) " +
                                        "SELECT td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,AVG(td.felectricity),AVG(td.fvoltage),AVG(td.frateofflow),COUNT(td.fid)," +
                                        "to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss'),td.fwelder_no,td.fjunction_no,td.fweld_no," +
                                        "td.fchannel,td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid," +
                                        "AVG(td.fwirefeedrate),td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus FROM TB_DATAA td " +
                                        "WHERE (fstatus= '98' OR fstatus= '99') " +
                                        "AND td.FWELDTIME BETWEEN to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss') AND to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss') " +
                                        "GROUP BY td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,td.fwelder_no,td.fjunction_no,td.fweld_no,td.fchannel," +
                                        "td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid," +
                                        "td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus";

                                statement.executeUpdate(sqlstandby);
                                statement.executeUpdate(sqlwork);
                                statement.executeUpdate(sqlwarn);
                                statement.executeUpdate(sqlalarm);
                            }
                            statement.executeUpdate(sqlAtrun);
                        }
                        outlinestatus = "B";
                    } else if (outlinestatus.equals("B")) {
                        ResultSet rs2 = statement.executeQuery(sqlBfirst);
                        while (rs2.next()) {
                            Blasttime = rs2.getString("FWELDTIME");
                        }
                        ResultSet rs4 = statement.executeQuery(sqlBlast);
                        while (rs4.next()) {
                            Bfirsttime = rs4.getString("FWELDTIME");
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (null != Blasttime && !"null".equals(Blasttime) && null != Bfirsttime || !"null".equals(Bfirsttime)) {
                            long Blast = sdf.parse(Blasttime).getTime() + 1000;
                            long Bfirst = sdf.parse(Bfirsttime).getTime();
                            //统计四张状态表
                            for (long i = Bfirst; i < Blast; i += 3600000) {
                                String datebuf1 = DateTools.format("yyyy-MM-dd HH:mm:ss", new Date(i));
                                String datebuf2 = DateTools.format("yyyy-MM-dd HH:mm:ss", new Date(i + 3600000));

                                String sqlstandby = "INSERT INTO tb_standby(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,frateofflow,fstandbytime," +
                                        "fstarttime,fendtime,fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage," +
                                        "fwelder_itemid,fjunction_itemid,fmachine_itemid,fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,fstatus) " +
                                        "SELECT td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,AVG(td.felectricity),AVG(td.fvoltage),AVG(td.frateofflow)," +
                                        "COUNT(td.fid),to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss'),td.fwelder_no," +
                                        "td.fjunction_no,td.fweld_no,td.fchannel,td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid," +
                                        "td.fjunction_itemid,td.fmachine_itemid,AVG(td.fwirefeedrate),td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus FROM TB_DATAB td " +
                                        "WHERE td.fstatus = '0' " +
                                        "AND (td.FWELDTIME BETWEEN to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss') AND to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss')) " +
                                        "GROUP BY td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,td.fwelder_no,td.fjunction_no,td.fweld_no,td.fchannel," +
                                        "td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid," +
                                        "td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus";

                                String sqlwork = "INSERT INTO tb_work(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,frateofflow,fworktime,fstarttime,fendtime," +
                                        "fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid,fjunction_itemid," +
                                        "fmachine_itemid,fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,fstatus) " +
                                        "SELECT td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,AVG(td.felectricity),AVG(td.fvoltage),AVG(td.frateofflow),COUNT(td.fid)," +
                                        "to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss'),td.fwelder_no," +
                                        "td.fjunction_no,td.fweld_no,td.fchannel,td.fmax_electricity,td.fmin_electricity,td.fmax_voltage," +
                                        "td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid,AVG(td.fwirefeedrate),td.fmachinemodel," +
                                        "td.fwirediameter,td.fmaterialgas,td.fstatus FROM TB_DATAB td " +
                                        "WHERE (td.fstatus = '3' OR fstatus= '5' OR fstatus= '7' OR fstatus= '99') " +
                                        "AND td.FWELDTIME BETWEEN to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss') AND to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss') " +
                                        "GROUP BY td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,td.fwelder_no,td.fjunction_no,td.fweld_no,td.fchannel," +
                                        "td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid," +
                                        "td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus";

                                String sqlwarn = "INSERT INTO tb_warn(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,frateofflow,fwarntime,fstarttime," +
                                        "fendtime,fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid," +
                                        "fjunction_itemid,fmachine_itemid,fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,fstatus) " +
                                        "SELECT td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,AVG(td.felectricity),AVG(td.fvoltage),AVG(td.frateofflow),COUNT(td.fid)," +
                                        "to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss'),td.fwelder_no,td.fjunction_no," +
                                        "td.fweld_no,td.fchannel,td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid," +
                                        "td.fmachine_itemid,AVG(td.fwirefeedrate),td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus FROM TB_DATAB td " +
                                        "WHERE td.fstatus != '0' AND td.fstatus != '3' AND td.fstatus != '5' AND td.fstatus != '7' " +
                                        "AND td.FWELDTIME BETWEEN to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss') AND to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss') " +
                                        "GROUP BY td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,td.fwelder_no,td.fjunction_no,td.fweld_no,td.fchannel," +
                                        "td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid," +
                                        "td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus";


                                String sqlalarm = "INSERT INTO tb_alarm(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,frateofflow,falarmtime,fstarttime,fendtime," +
                                        "fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid,fjunction_itemid," +
                                        "fmachine_itemid,fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,fstatus) " +
                                        "SELECT td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,AVG(td.felectricity),AVG(td.fvoltage),AVG(td.frateofflow),COUNT(td.fid)," +
                                        "to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss'),td.fwelder_no,td.fjunction_no,td.fweld_no," +
                                        "td.fchannel,td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid," +
                                        "AVG(td.fwirefeedrate),td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus FROM TB_DATAB td " +
                                        "WHERE (fstatus= '98' OR fstatus= '99') " +
                                        "AND td.FWELDTIME BETWEEN to_date('" + datebuf1 + "','yyyy-mm-dd hh24:mi:ss') AND to_date('" + datebuf2 + "','yyyy-mm-dd hh24:mi:ss') " +
                                        "GROUP BY td.fwelder_id,td.fgather_no,td.fmachine_id,td.fjunction_id,td.fitemid,td.fwelder_no,td.fjunction_no,td.fweld_no,td.fchannel," +
                                        "td.fmax_electricity,td.fmin_electricity,td.fmax_voltage,td.fmin_voltage,td.fwelder_itemid,td.fjunction_itemid,td.fmachine_itemid," +
                                        "td.fmachinemodel,td.fwirediameter,td.fmaterialgas,td.fstatus";

                                statement.executeUpdate(sqlstandby);
                                statement.executeUpdate(sqlwork);
                                statement.executeUpdate(sqlwarn);
                                statement.executeUpdate(sqlalarm);
                            }
                            statement.executeUpdate(sqlBtrun);
                        }
                        outlinestatus = "A";
                    }
                    Thread.sleep(1000);
                } catch (SQLException e) {
                    System.out.println("Broken conn");
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    OracleDBConnection.close(connection, statement, null);
                }
            }
        }, time1, (60 * 60 * 24), TimeUnit.SECONDS);

        /**
         * 周期性线程池每小时更新work等四张数据工作表
         */
        executorService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                Connection connection = null;
                Statement statement = null;
                try {
                    connection = OracleDBConnection.getConnection();
                    statement = connection.createStatement();
                    //基本版
                    //获取上次统计时间，为空插入赋默认值
                    Date date = new Date();
                    String nowtimefor = DateTools.format("yyyy-MM-dd", date);
                    String nowtime = DateTools.format("HH:mm:ss", date);
                    String[] timesplit = nowtime.split(":");
                    String hour = timesplit[0];
                    String time2 = nowtimefor + " " + hour + ":00:00"; //当前小时
                    Date d1 = new Date((DateTools.parse("yyyy-MM-dd HH:mm:ss", time2).getTime()) - 3600000);
                    String time3 = DateTools.format("yyyy-MM-dd HH:mm:ss", d1); //当前时间一个小时前

                    LocalDateTime localDateTime = LocalDateTime.now().minusHours(12);
                    String twelveHourBefore = dateTimeFormatter.format(localDateTime);

                    /**
                     * 开始时间超过12个小时，自动结束掉任务
                     */
                    String updateTaskResultSql = "UPDATE TB_TASKRESULT SET FREALENDTIME = SYSDATE,FOPERATETYPE = 2 " +
                            "WHERE FOPERATETYPE = 1 AND FREALSTARTTIME <= TO_DATE('"+twelveHourBefore+"', 'yyyy-mm-dd hh24:mi:ss')";
                    statement.executeQuery(updateTaskResultSql);

                    String timework = null;
                    String timestandby = null;
                    String timealarm = null;
                    String timewarn = null;
                    String sqlfirstwork = "select * from (SELECT tb_work.fUploadDataTime FROM tb_work ORDER BY tb_work.fUploadDataTime DESC) WHERE ROWNUM=1";
                    String sqlfirststandby = "select * from (SELECT tb_standby.fUploadDataTime FROM tb_standby ORDER BY tb_standby.fUploadDataTime DESC) WHERE ROWNUM=1";
                    String sqlfirstalarm = "select * from (SELECT tb_alarm.fUploadDataTime FROM tb_alarm ORDER BY tb_alarm.fUploadDataTime DESC) WHERE ROWNUM=1";
                    String sqlfirstwarn = "select * from (SELECT tb_warn.fUploadDataTime FROM tb_warn ORDER BY tb_warn.fUploadDataTime DESC) WHERE ROWNUM=1";

                    ResultSet rs1 = statement.executeQuery(sqlfirstwork);
                    while (rs1.next()) {
                        timework = rs1.getString("fUploadDataTime");
                    }

//                    System.out.println("当前时间:" + time2);
//                    System.out.println("一个小时前:" + time3);

                    ResultSet rs2 = statement.executeQuery(sqlfirststandby);
                    while (rs2.next()) {
                        timestandby = rs2.getString("fUploadDataTime");
                    }
                    ResultSet rs3 = statement.executeQuery(sqlfirstalarm);
                    while (rs3.next()) {
                        timealarm = rs3.getString("fUploadDataTime");
                    }
                    ResultSet rs4 = statement.executeQuery(sqlfirstwarn);
                    while (rs4.next()) {
                        timewarn = rs4.getString("fUploadDataTime");
                    }

                    if (null == timework || "".equals(timework)) {
                        timework = time3;
                    }
                    if (null == timestandby || "".equals(timestandby)) {
                        timestandby = time3;
                    }
                    if (null == timealarm || "".equals(timealarm)) {
                        timealarm = time3;
                    }
                    if (null == timewarn || "".equals(timewarn)) {
                        timewarn = time3;
                    }

                    //统计四张状态表
                    String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
                            + "tb_standby.fitemid,tb_standby.felectricity,tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime,tb_standby.fwelder_no,tb_standby.fjunction_no,tb_standby.fweld_no,tb_standby.fchannel,tb_standby.fmax_electricity,tb_standby.fmin_electricity,tb_standby.fmax_voltage,tb_standby.fmin_voltage,tb_standby.fwelder_itemid,tb_standby.fjunction_itemid,tb_standby.fmachine_itemid,tb_standby.fwirefeedrate,tb_standby.fmachinemodel,tb_standby.fwirediameter,tb_standby.fmaterialgas,tb_standby.fstatus"
                            + ",tb_standby.flon_air_flow,tb_standby.fhatwirecurrent,tb_standby.fpreheating_temperature,tb_standby.fswing,tb_standby.fvibrafrequency,tb_standby.flaser_power,tb_standby.fdefocus_amount,tb_standby.fdefocus_quantity,tb_standby.fpeak_electricity,tb_standby.fbase_electricity,tb_standby.fpeak_time,tb_standby.fbase_time,tb_standby.faccelerat_voltage,tb_standby.ffocus_current,tb_standby.felectron_beam,tb_standby.fscan_frequency,tb_standby.fscan_amplitude,tb_standby.fswing_speed,tb_standby.fcard_id,tb_standby.fwps_lib_id,tb_standby.fproduct_number_id,tb_standby.femployee_id,tb_standby.fstep_id,tb_standby.FUPLOADDATATIME) "
                            + "SELECT tb_live_data.fwelder_id,MAX(tb_live_data.fgather_no),tb_live_data.fmachine_id,tb_live_data.fjunction_id,MAX(tb_live_data.fitemid),AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),MAX(to_date('" + time3 + "' , 'yyyy-mm-dd hh24:mi:ss')),MAX(to_date('" + time2 + "' , 'yyyy-mm-dd hh24:mi:ss')),"
                            + "MAX(tb_live_data.fwelder_no),MAX(tb_live_data.fjunction_no),MAX(tb_live_data.fweld_no),MAX(tb_live_data.fchannel),MAX(tb_live_data.fmax_electricity),MAX(tb_live_data.fmin_electricity),MAX(tb_live_data.fmax_voltage),MAX(tb_live_data.fmin_voltage),MAX(tb_live_data.fwelder_itemid),MAX(tb_live_data.fjunction_itemid),MAX(tb_live_data.fmachine_itemid),AVG(tb_live_data.fwirefeedrate),MAX(tb_live_data.fmachinemodel),MAX(tb_live_data.fwirediameter),MAX(tb_live_data.fmaterialgas),tb_live_data.fstatus,MAX(tb_live_data.flon_air_flow),MAX(tb_live_data.fhatwirecurrent),MAX(tb_live_data.fpreheating_temperature),MAX(tb_live_data.fswing),MAX(tb_live_data.fvibrafrequency),MAX(tb_live_data.flaser_power),MAX(tb_live_data.fdefocus_amount),MAX(tb_live_data.fdefocus_quantity),MAX(tb_live_data.fpeak_electricity),MAX(tb_live_data.fbase_electricity),MAX(tb_live_data.fpeak_time),MAX(tb_live_data.fbase_time),MAX(tb_live_data.faccelerat_voltage),MAX(tb_live_data.ffocus_current),MAX(tb_live_data.felectron_beam),MAX(tb_live_data.fscan_frequency),MAX(tb_live_data.fscan_amplitude),MAX(tb_live_data.fswing_speed),tb_live_data.fcard_id,tb_live_data.fwps_lib_id,tb_live_data.fproduct_number_id,tb_live_data.femployee_id,tb_live_data.fstep_id,SYSDATE FROM tb_live_data"
                            + " WHERE tb_live_data.fstatus = '0' AND (tb_live_data.FWeldTime BETWEEN to_date('" + timestandby + "' , 'yyyy-mm-dd hh24:mi:ss') AND to_date('" + time2 + "' , 'yyyy-mm-dd hh24:mi:ss')) "
                            + "GROUP BY tb_live_data.fwelder_id,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fcard_id,tb_live_data.fwps_lib_id,tb_live_data.fproduct_number_id,tb_live_data.femployee_id,tb_live_data.fstep_id,tb_live_data.fstatus";

                    String sqlwork = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
                            + "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime,tb_work.fwelder_no,tb_work.fjunction_no,tb_work.fweld_no,tb_work.fchannel,tb_work.fmax_electricity,tb_work.fmin_electricity,tb_work.fmax_voltage,tb_work.fmin_voltage,tb_work.fwelder_itemid,tb_work.fjunction_itemid,tb_work.fmachine_itemid,tb_work.fwirefeedrate,tb_work.fmachinemodel,tb_work.fwirediameter,tb_work.fmaterialgas,tb_work.fstatus"
                            + ",tb_work.flon_air_flow,tb_work.fhatwirecurrent,tb_work.fpreheating_temperature,tb_work.fswing,tb_work.fvibrafrequency,tb_work.flaser_power,tb_work.fdefocus_amount,tb_work.fdefocus_quantity,tb_work.fpeak_electricity,tb_work.fbase_electricity,tb_work.fpeak_time,tb_work.fbase_time,tb_work.faccelerat_voltage,tb_work.ffocus_current,tb_work.felectron_beam,tb_work.fscan_frequency,tb_work.fscan_amplitude,tb_work.fswing_speed,tb_work.fcard_id,tb_work.fwps_lib_id,tb_work.fproduct_number_id,tb_work.femployee_id,tb_work.fstep_id,tb_work.FUPLOADDATATIME) "
                            + "SELECT tb_live_data.fwelder_id,MAX(tb_live_data.fgather_no),tb_live_data.fmachine_id,tb_live_data.fjunction_id,MAX(tb_live_data.fitemid),AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),MAX(to_date('" + time3 + "' , 'yyyy-mm-dd hh24:mi:ss')),MAX(to_date('" + time2 + "' , 'yyyy-mm-dd hh24:mi:ss')),"
                            + "MAX(tb_live_data.fwelder_no),MAX(tb_live_data.fjunction_no),MAX(tb_live_data.fweld_no),MAX(tb_live_data.fchannel),MAX(tb_live_data.fmax_electricity),MAX(tb_live_data.fmin_electricity),MAX(tb_live_data.fmax_voltage),MAX(tb_live_data.fmin_voltage),MAX(tb_live_data.fwelder_itemid),MAX(tb_live_data.fjunction_itemid),MAX(tb_live_data.fmachine_itemid),AVG(tb_live_data.fwirefeedrate),MAX(tb_live_data.fmachinemodel),MAX(tb_live_data.fwirediameter),MAX(tb_live_data.fmaterialgas),tb_live_data.fstatus,MAX(tb_live_data.flon_air_flow),MAX(tb_live_data.fhatwirecurrent),MAX(tb_live_data.fpreheating_temperature),MAX(tb_live_data.fswing),MAX(tb_live_data.fvibrafrequency),MAX(tb_live_data.flaser_power),MAX(tb_live_data.fdefocus_amount),MAX(tb_live_data.fdefocus_quantity),MAX(tb_live_data.fpeak_electricity),MAX(tb_live_data.fbase_electricity),MAX(tb_live_data.fpeak_time),MAX(tb_live_data.fbase_time),MAX(tb_live_data.faccelerat_voltage),MAX(tb_live_data.ffocus_current),MAX(tb_live_data.felectron_beam),MAX(tb_live_data.fscan_frequency),MAX(tb_live_data.fscan_amplitude),MAX(tb_live_data.fswing_speed),tb_live_data.fcard_id,tb_live_data.fwps_lib_id,tb_live_data.fproduct_number_id,tb_live_data.femployee_id,tb_live_data.fstep_id,SYSDATE FROM tb_live_data"
                            + " WHERE (tb_live_data.fstatus = '3' OR fstatus= '5' OR fstatus= '7' OR fstatus= '99') AND (tb_live_data.FWeldTime BETWEEN to_date('" + timework + "' , 'yyyy-mm-dd hh24:mi:ss') AND to_date('" + time2 + "' , 'yyyy-mm-dd hh24:mi:ss')) "
                            + "GROUP BY tb_live_data.fwelder_id,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fcard_id,tb_live_data.fwps_lib_id,tb_live_data.fproduct_number_id,tb_live_data.femployee_id,tb_live_data.fstep_id,tb_live_data.fstatus";

                    String sqlwarn = "INSERT INTO tb_warn(tb_warn.fwelder_id,tb_warn.fgather_no,tb_warn.fmachine_id,tb_warn.fjunction_id,"
                            + "tb_warn.fitemid,tb_warn.felectricity,tb_warn.fvoltage,tb_warn.frateofflow,tb_warn.fwarntime,tb_warn.fstarttime,tb_warn.fendtime,tb_warn.fwelder_no,tb_warn.fjunction_no,tb_warn.fweld_no,tb_warn.fchannel,tb_warn.fmax_electricity,tb_warn.fmin_electricity,tb_warn.fmax_voltage,tb_warn.fmin_voltage,tb_warn.fwelder_itemid,tb_warn.fjunction_itemid,tb_warn.fmachine_itemid,tb_warn.fwirefeedrate,tb_warn.fmachinemodel,tb_warn.fwirediameter,tb_warn.fmaterialgas,tb_warn.fstatus"
                            + ",tb_warn.flon_air_flow,tb_warn.fhatwirecurrent,tb_warn.fpreheating_temperature,tb_warn.fswing,tb_warn.fvibrafrequency,tb_warn.flaser_power,tb_warn.fdefocus_amount,tb_warn.fdefocus_quantity,tb_warn.fpeak_electricity,tb_warn.fbase_electricity,tb_warn.fpeak_time,tb_warn.fbase_time,tb_warn.faccelerat_voltage,tb_warn.ffocus_current,tb_warn.felectron_beam,tb_warn.fscan_frequency,tb_warn.fscan_amplitude,tb_warn.fswing_speed,tb_warn.fcard_id,tb_warn.fwps_lib_id,tb_warn.fproduct_number_id,tb_warn.femployee_id,tb_warn.fstep_id,tb_warn.FUPLOADDATATIME) "
                            + "SELECT tb_live_data.fwelder_id,MAX(tb_live_data.fgather_no),tb_live_data.fmachine_id,tb_live_data.fjunction_id,MAX(tb_live_data.fitemid),AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),MAX(to_date('" + time3 + "' , 'yyyy-mm-dd hh24:mi:ss')),MAX(to_date('" + time2 + "' , 'yyyy-mm-dd hh24:mi:ss')),"
                            + "MAX(tb_live_data.fwelder_no),MAX(tb_live_data.fjunction_no),MAX(tb_live_data.fweld_no),MAX(tb_live_data.fchannel),MAX(tb_live_data.fmax_electricity),MAX(tb_live_data.fmin_electricity),MAX(tb_live_data.fmax_voltage),MAX(tb_live_data.fmin_voltage),MAX(tb_live_data.fwelder_itemid),MAX(tb_live_data.fjunction_itemid),MAX(tb_live_data.fmachine_itemid),AVG(tb_live_data.fwirefeedrate),MAX(tb_live_data.fmachinemodel),MAX(tb_live_data.fwirediameter),MAX(tb_live_data.fmaterialgas),tb_live_data.fstatus,MAX(tb_live_data.flon_air_flow),MAX(tb_live_data.fhatwirecurrent),MAX(tb_live_data.fpreheating_temperature),MAX(tb_live_data.fswing),MAX(tb_live_data.fvibrafrequency),MAX(tb_live_data.flaser_power),MAX(tb_live_data.fdefocus_amount),MAX(tb_live_data.fdefocus_quantity),MAX(tb_live_data.fpeak_electricity),MAX(tb_live_data.fbase_electricity),MAX(tb_live_data.fpeak_time),MAX(tb_live_data.fbase_time),MAX(tb_live_data.faccelerat_voltage),MAX(tb_live_data.ffocus_current),MAX(tb_live_data.felectron_beam),MAX(tb_live_data.fscan_frequency),MAX(tb_live_data.fscan_amplitude),MAX(tb_live_data.fswing_speed),tb_live_data.fcard_id,tb_live_data.fwps_lib_id,tb_live_data.fproduct_number_id,tb_live_data.femployee_id,tb_live_data.fstep_id,SYSDATE FROM tb_live_data"
                            + " WHERE (tb_live_data.fstatus != '0' or tb_live_data.fstatus != '3' or tb_live_data.fstatus != '5' or tb_live_data.fstatus != '7') AND (tb_live_data.FWeldTime BETWEEN to_date('" + timewarn + "' , 'yyyy-mm-dd hh24:mi:ss') AND to_date('" + time2 + "' , 'yyyy-mm-dd hh24:mi:ss')) "
                            + "GROUP BY tb_live_data.fwelder_id,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fcard_id,tb_live_data.fwps_lib_id,tb_live_data.fproduct_number_id,tb_live_data.femployee_id,tb_live_data.fstep_id,tb_live_data.fstatus";

                    String sqlalarm = "INSERT INTO tb_alarm(tb_alarm.fwelder_id,tb_alarm.fgather_no,tb_alarm.fmachine_id,tb_alarm.fjunction_id,tb_alarm.fitemid,"
                            + "tb_alarm.felectricity,tb_alarm.fvoltage,tb_alarm.frateofflow,tb_alarm.falarmtime,tb_alarm.fstarttime,tb_alarm.fendtime,tb_alarm.fwelder_no,tb_alarm.fjunction_no,tb_alarm.fweld_no,tb_alarm.fchannel,tb_alarm.fmax_electricity,tb_alarm.fmin_electricity,tb_alarm.fmax_voltage,tb_alarm.fmin_voltage,tb_alarm.fwelder_itemid,tb_alarm.fjunction_itemid,tb_alarm.fmachine_itemid,tb_alarm.fwirefeedrate,tb_alarm.fmachinemodel,tb_alarm.fwirediameter,tb_alarm.fmaterialgas,tb_alarm.fstatus"
                            + ",tb_alarm.flon_air_flow,tb_alarm.fhatwirecurrent,tb_alarm.fpreheating_temperature,tb_alarm.fswing,tb_alarm.fvibrafrequency,tb_alarm.flaser_power,tb_alarm.fdefocus_amount,tb_alarm.fdefocus_quantity,tb_alarm.fpeak_electricity,tb_alarm.fbase_electricity,tb_alarm.fpeak_time,tb_alarm.fbase_time,tb_alarm.faccelerat_voltage,tb_alarm.ffocus_current,tb_alarm.felectron_beam,tb_alarm.fscan_frequency,tb_alarm.fscan_amplitude,tb_alarm.fswing_speed,tb_alarm.fcard_id,tb_alarm.fwps_lib_id,tb_alarm.fproduct_number_id,tb_alarm.femployee_id,tb_alarm.fstep_id,tb_alarm.FUPLOADDATATIME) "
                            + "SELECT tb_live_data.fwelder_id,MAX(tb_live_data.fgather_no),tb_live_data.fmachine_id,tb_live_data.fjunction_id,MAX(tb_live_data.fitemid),AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),MAX(to_date('" + time3 + "' , 'yyyy-mm-dd hh24:mi:ss')),MAX(to_date('" + time2 + "' , 'yyyy-mm-dd hh24:mi:ss')),"
                            + "MAX(tb_live_data.fwelder_no),MAX(tb_live_data.fjunction_no),MAX(tb_live_data.fweld_no),MAX(tb_live_data.fchannel),MAX(tb_live_data.fmax_electricity),MAX(tb_live_data.fmin_electricity),MAX(tb_live_data.fmax_voltage),MAX(tb_live_data.fmin_voltage),MAX(tb_live_data.fwelder_itemid),MAX(tb_live_data.fjunction_itemid),MAX(tb_live_data.fmachine_itemid),AVG(tb_live_data.fwirefeedrate),MAX(tb_live_data.fmachinemodel),MAX(tb_live_data.fwirediameter),MAX(tb_live_data.fmaterialgas),tb_live_data.fstatus,MAX(tb_live_data.flon_air_flow),MAX(tb_live_data.fhatwirecurrent),MAX(tb_live_data.fpreheating_temperature),MAX(tb_live_data.fswing),MAX(tb_live_data.fvibrafrequency),MAX(tb_live_data.flaser_power),MAX(tb_live_data.fdefocus_amount),MAX(tb_live_data.fdefocus_quantity),MAX(tb_live_data.fpeak_electricity),MAX(tb_live_data.fbase_electricity),MAX(tb_live_data.fpeak_time),MAX(tb_live_data.fbase_time),MAX(tb_live_data.faccelerat_voltage),MAX(tb_live_data.ffocus_current),MAX(tb_live_data.felectron_beam),MAX(tb_live_data.fscan_frequency),MAX(tb_live_data.fscan_amplitude),MAX(tb_live_data.fswing_speed),tb_live_data.fcard_id,tb_live_data.fwps_lib_id,tb_live_data.fproduct_number_id,tb_live_data.femployee_id,tb_live_data.fstep_id,SYSDATE FROM tb_live_data"
                            + " WHERE (tb_live_data.fstatus= '98' OR tb_live_data.fstatus= '99')"
                            + " AND (tb_live_data.FWeldTime BETWEEN to_date('" + timealarm + "' , 'yyyy-mm-dd hh24:mi:ss') AND to_date('" + time2 + "' , 'yyyy-mm-dd hh24:mi:ss')) "
                            + "GROUP BY tb_live_data.fwelder_id,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fcard_id,tb_live_data.fwps_lib_id,tb_live_data.fproduct_number_id,tb_live_data.femployee_id,tb_live_data.fstep_id,tb_live_data.fstatus";

                    statement.executeUpdate(sqlstandby);
                    statement.executeUpdate(sqlwork);
                    statement.executeUpdate(sqlwarn);
                    statement.executeUpdate(sqlalarm);

                } catch (SQLException e) {
                    System.out.println("Broken conn");
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    OracleDBConnection.close(connection, statement, null);
                }
            }
        }, time, 60 * 60, TimeUnit.SECONDS);

        //获取最新焊口和焊机统计时间
        check = new DB_Connectioncode();
        NS.websocket.dbdata = this.dbdata;

        listarray1 = check.getId1();
        listarray2 = check.getId2();
        listarray3 = check.getId3();
        listarray4 = check.getId4();

        System.out.println("listarray1:" + listarray1);
        System.out.println("listarray2:" + listarray2);
        System.out.println("listarray3:" + listarray3);
        System.out.println("listarray4:" + listarray4);

        NS.mysql.listarray1 = this.listarray1;
        NS.mysql.listarray2 = this.listarray2;
        NS.mysql.listarray3 = this.listarray3;
        NS.websocket.listarray1 = this.listarray1;
        NS.websocket.listarray2 = this.listarray2;
        NS.websocket.listarray3 = this.listarray3;
//        NS.android.listarray1 = this.listarray1;
//        NS.android.listarray2 = this.listarray2;
        NS.listarray1 = this.listarray1;
        NS.listarray2 = this.listarray2;
        NS.listarray3 = this.listarray3;
        NS.listarray4 = this.listarray4;

        //开启线程每小时更新焊口数据
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    DB_Connectioncode check = new DB_Connectioncode();

                    listarray1 = check.getId1();
                    listarray2 = check.getId2();
                    listarray3 = check.getId3();

                    NS.mysql.listarray1 = listarray1;
                    NS.mysql.listarray2 = listarray2;
                    NS.mysql.listarray3 = listarray3;
                    NS.android.listarray1 = listarray1;
                    NS.android.listarray2 = listarray2;
                    NS.listarray1 = listarray1;
                    NS.listarray2 = listarray2;
                    NS.listarray3 = listarray3;
                    NS.listarray4 = listarray4;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }, 0, 3600, TimeUnit.SECONDS);

        //工作线程
        new Thread(socketstart).start();
        //new Thread(websocketstart).start();
        //new Thread(sockettran).start();
        //new com.Email().run();
        //new com.UpReport();

        mqtt.init("");
        NS.mqtt = mqtt;
        NS.websocket.mqtt = mqtt;
        mqtt.subTopic("weldmeswebdatadown");    //索取规范
        mqtt.subTopic("weldmes/downparams");    //下发规范
        mqtt.subTopic("spacexifa-downparams");    //控制命令和密码下发规范
        mqtt.subTopic("padDataSetLiveData");    //手持终端存实时表
        mqtt.subTopic("hand-held-terminal-askFor");    //手持终端索取
        mqtt.subTopic("hand-held-terminal-issue");    //手持终端下发
        mqtt.subTopic("whiteList-dataIssue");    //焊工白名单下发
        //mqtt.subTopic("control-command-issue");    //手持终端控制命令下发
    }

    //开启5551端口获取焊机数据
    public Runnable socketstart = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .childHandler(NS);

                b = b.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                    @Override
                    public void initChannel(SocketChannel chsoc) throws Exception {
                        chsoc.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        chsoc.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                        chsoc.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                        chsoc.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                        chsoc.pipeline().addLast(
                                new ReadTimeoutHandler(100),
                                new WriteTimeoutHandler(100),
                                NS);
                        synchronized (socketlist) {
                            socketcount++;
                            socketlist.put(Integer.toString(socketcount), chsoc);
                            NS.socketlist = socketlist;
                            NWS.socketlist = socketlist;
                            mqtt.socketlist = socketlist;
                        }
                    }
                });

                //绑定端口，等待同步成功
                ChannelFuture f;
                f = b.bind(5551).sync();
                //等待服务端关闭监听端口
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                //释放线程池资源
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
    };

    //开启5550端口处理网页实时数据
    public Runnable websocketstart = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup(128);

            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap
                        .group(bossGroup, workerGroup)
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .channel(NioServerSocketChannel.class)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childHandler(new ChannelInitializer<SocketChannel>() {

                            @Override
                            protected void initChannel(SocketChannel chweb) throws Exception {
                                // TODO Auto-generated method stub
                                chweb.pipeline().addLast("httpServerCodec", new HttpServerCodec());
                                chweb.pipeline().addLast("chunkedWriteHandler", new ChunkedWriteHandler());
                                chweb.pipeline().addLast("httpObjectAggregator", new HttpObjectAggregator(1024 * 1024 * 1024));
                                chweb.pipeline().addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("ws://119.3.100.103:5550/SerialPortDemo/ws/张三", null, true, 65535));
                                chweb.pipeline().addLast("myWebSocketHandler", NWS);
                                synchronized (this) {
                                    websocketcount++;
                                    websocketlist.put(Integer.toString(websocketcount), chweb);
                                    NS.websocketlist = websocketlist;
                                }

                                //System.out.println(chweb);
                            }

                        });

                Channel ch = serverBootstrap.bind(5550).sync().channel();
                ch.closeFuture().sync();

				/*ChannelFuture channelFuture = serverBootstrap.bind(5550).sync();
	            channelFuture.channel().closeFuture().sync();*/

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
    };

    //多层级转发
    public Runnable sockettran = new Runnable() {

        @Override
        public void run() {
            if (ip1 != null) {
                client.run();
            }
        }
    };

    public static void main(String[] args) throws IOException {
        Thread desktopServerThread = new Thread(new Server());
        desktopServerThread.start();
    }

}