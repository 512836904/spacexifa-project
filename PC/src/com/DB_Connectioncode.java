package com;

import java.sql.*;
import java.util.ArrayList;

public class DB_Connectioncode {

    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public ArrayList<String> listarray4 = new ArrayList<String>();

    //    public Server server;
    public String inSql = "";
//    public String connet;

    public DB_Connectioncode() {

        Connection conn = null;
        Statement stmt = null;

        //查焊工
        inSql = "SELECT FID,FWELDER_NO,FOWNER FROM TB_WELDER";

        try {
            if (conn == null || conn.isClosed()) {
                conn = OracleDBConnection.getConnection();
            }
            if (stmt == null || stmt.isClosed()) {
                stmt = conn.createStatement();
            }
            ResultSet rs = stmt.executeQuery(inSql);
            while (rs.next()) {
                String welderid = rs.getString("FID");
                String fwelder_no = rs.getString("FWELDER_NO");
                String fowner = rs.getString("FOWNER");
                listarray1.add(welderid);
                listarray1.add(fwelder_no);
                listarray1.add(fowner);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询焊工信息异常：" + e.getMessage());
        } finally {
            OracleDBConnection.close(conn, stmt, null);
        }
        //查焊机和采集模块的关联信息
        inSql = "SELECT wm.FID fmachineId,g.FID,wm.FEQUIPMENT_NO,g.FGATHER_NO,wm.FINSFRAMEWORK_ID FROM tb_welding_machine wm LEFT JOIN TB_GATHER g ON wm.FGATHER_ID = g.FID";
        try {
            if (conn == null || conn.isClosed()) {
                conn = OracleDBConnection.getConnection();
            }
            if (stmt == null || stmt.isClosed()) {
                stmt = conn.createStatement();
            }
            ResultSet rs = stmt.executeQuery(inSql);
            while (rs.next()) {
                String fid = rs.getString("fmachineId");//焊机id
                String fequipment_no = rs.getString("fequipment_no");//焊机固号
                String fgather_no = String.valueOf(Integer.parseInt(rs.getString("fgather_no")));     //采集序号
                String fitemId = rs.getString("finsframework_id");  //焊机组织id
                listarray2.add(fid);
                listarray2.add(fequipment_no);
                listarray2.add((fgather_no));
                listarray2.add(fitemId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询焊机和采集模块信息异常：" + e.getMessage());
        } finally {
            OracleDBConnection.close(conn, stmt, null);
        }
        //查询焊缝的电流电压最大最小值
        inSql = "SELECT FID,JUNCTION_NAME,CURRENT_LIMIT,CURRENT_LOWER_LIMIT,FMAXVOLTAGE,FMINVOLTAGE FROM TB_JUNCTION";
        try {
            if (conn == null || conn.isClosed()) {
                conn = OracleDBConnection.getConnection();
            }
            if (stmt == null || stmt.isClosed()) {
                stmt = conn.createStatement();
            }
            ResultSet rs = stmt.executeQuery(inSql);
            while (rs.next()) {
                String weldjunctionid = rs.getString("FID");
                int fmax_electricity1 = rs.getInt("CURRENT_LIMIT");
                String fmax_electricity = String.valueOf(fmax_electricity1);
                if (fmax_electricity.length() != 3) {
                    int lenth = 3 - fmax_electricity.length();
                    for (int i = 0; i < lenth; i++) {
                        fmax_electricity = "0" + fmax_electricity;
                    }
                }
                int fmin_electricity1 = rs.getInt("CURRENT_LOWER_LIMIT");
                String fmin_electricity = String.valueOf(fmin_electricity1);
                if (fmin_electricity.length() != 3) {
                    int lenth = 3 - fmin_electricity.length();
                    for (int i = 0; i < lenth; i++) {
                        fmin_electricity = "0" + fmin_electricity;
                    }
                }
                int fmax_valtage1 = rs.getInt("FMAXVOLTAGE");
                String fmax_valtage = String.valueOf(fmax_valtage1);
                if (fmax_valtage.length() != 3) {
                    int lenth = 3 - fmax_valtage.length();
                    for (int i = 0; i < lenth; i++) {
                        fmax_valtage = "0" + fmax_valtage;
                    }
                }
                int fmin_valtage1 = rs.getInt("FMINVOLTAGE");
                String fmin_valtage = String.valueOf(fmin_valtage1);
                if (fmin_valtage.length() != 3) {
                    int lenth = 3 - fmin_valtage.length();
                    for (int i = 0; i < lenth; i++) {
                        fmin_valtage = "0" + fmin_valtage;
                    }
                }
                String weldjunction = rs.getString("JUNCTION_NAME");
                listarray3.add(weldjunction);//焊缝名称
                listarray3.add(fmax_electricity);//最大电流
                listarray3.add(fmin_electricity);
                listarray3.add(fmax_valtage);//最大电压
                listarray3.add(fmin_valtage);
                listarray3.add(weldjunctionid);//焊缝id
                listarray3.add("0");//组织机构id
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询焊缝信息异常：" + e.getMessage());
        } finally {
            OracleDBConnection.close(conn, stmt, null);
        }
        //查询参数表
        inSql = "SELECT fair_flow_volume from tb_parameter";
        try {
            if (conn == null || conn.isClosed()) {
                conn = OracleDBConnection.getConnection();
            }
            if (stmt == null || stmt.isClosed()) {
                stmt = conn.createStatement();
            }
            ResultSet rs1 = stmt.executeQuery(inSql);
            String airflow = "";
            while (rs1.next()) {
                airflow = rs1.getString("fair_flow_volume");
            }
            //查询焊机信息
            inSql = "SELECT g.FID,wm.FEQUIPMENT_NO FROM TB_WELDING_MACHINE wm LEFT JOIN TB_GATHER g ON wm.FGATHER_ID = g.FID";
            ResultSet rs = stmt.executeQuery(inSql);
            while (rs.next()) {
                String fid = rs.getString("fid");
                String fequipment_no = rs.getString("fequipment_no");
                listarray4.add(fid);
                listarray4.add(fequipment_no);
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add(airflow);
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add(airflow);
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add("0");
                listarray4.add(airflow);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询焊机信息异常：" + e.getMessage());
        } finally {
            OracleDBConnection.close(conn, stmt, null);
        }
    }

    public ArrayList<String> getId1() {
        return listarray1;
    }

    public void setId1(ArrayList<String> listarray1) {
        this.listarray1 = listarray1;
    }

    public ArrayList<String> getId2() {
        return listarray2;
    }

    public void setId2(ArrayList<String> listarray2) {
        this.listarray2 = listarray2;
    }

    public ArrayList<String> getId3() {
        return listarray3;
    }

    public void setId3(ArrayList<String> listarray3) {
        this.listarray3 = listarray3;
    }

    public ArrayList<String> getId4() {
        return listarray4;
    }
}




































/*import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Date;

public class com.DB_Connectioncode {
	public  String datasend="";

	public String getId() {
		return datasend;
	}
	public void setId(String datasend) {
		this.datasend = datasend;
	}
    public com.Server server;
    public String connet = "jdbc:mysql://121.196.222.216:3306/Weld?"
    + "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
    public String inSql = "";

	String Connection="jdbc:mysql://121.196.222.216:3306/Weld?"+

            "user=root&password=123456&characterEncoding=UTF8";

    String uri = "jdbc:mysql://121.196.222.216:3306/Weld?";

    String user = "user=root&password=123456&characterEncoding=UTF8";



     String connet = "jdbc:mysql://121.196.222.216:3306/Weld?"

             + "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";

    String inSql = null;

	private String code;

    public com.DB_Connectioncode(String code,String connet)

    {
    	String inSql;

    	this.code = code;

        java.sql.Connection conn = null;

        java.sql.Statement stmt =null;


        try {  

            Class.forName("oracle.jdbc.OracleDriver");  

        } catch (ClassNotFoundException e) {  

            System.out.println("Broken driver");

            e.printStackTrace();  

        }  

        //锟斤拷锟斤拷锟斤拷锟斤拷

         try {

             conn = DriverManager.getConnection(connet,com.Server.oracleUser,com.Server.oraclePassword);

             //锟斤拷取锟斤拷锟绞�

             stmt= conn.createStatement();



        } catch (SQLException e) {

            System.out.println("Broken conn");

            e.printStackTrace();

        }



         Date date = new Date();//锟斤拷锟较低呈憋拷锟�.

         String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//锟斤拷时锟斤拷锟绞阶拷锟斤拷煞锟斤拷锟絋imestamp要锟斤拷母锟绞�.

         Timestamp goodsC_date = Timestamp.valueOf(nowTime);//锟斤拷时锟斤拷转锟斤拷



        // if(state ==1)

             inSql = "select fmax_electricity, fmin_electricity, fmax_valtage, fmin_valtage from tb_welded_junction where fwelded_junction_no = " +code;

         else {

             inSql = "insert into sj(id,bed_Number,state,Dtime) values('"+ id +"','" + bedNumber + "','leave','" + goodsC_date + "')";

        }

         try {

        	ResultSet rs =stmt.executeQuery(inSql);

            while (rs.next()) {
            	int fmax_electricity1 = rs.getInt("fmax_electricity");
            	String fmax_electricity = String.valueOf(fmax_electricity1);
            	if(fmax_electricity.length()!=3){
            		int lenth=3-fmax_electricity.length();
            		for(int i=0;i<lenth;i++){
            			fmax_electricity="0"+fmax_electricity;
            		}
            	}
            	int fmin_electricity1 = rs.getInt("fmin_electricity");
            	String fmin_electricity = String.valueOf(fmin_electricity1);
            	if(fmin_electricity.length()!=3){
            		int lenth=3-fmin_electricity.length();
            		for(int i=0;i<lenth;i++){
            			fmin_electricity="0"+fmin_electricity;
            		}
            	}
            	int fmax_valtage1 = rs.getInt("fmax_valtage");
            	String fmax_valtage = String.valueOf(fmax_valtage1);
            	if(fmax_valtage.length()!=3){
            		int lenth=3-fmax_valtage.length();
            		for(int i=0;i<lenth;i++){
            			fmax_valtage="0"+fmax_valtage;
            		}
            	}
            	int fmin_valtage1 = rs.getInt("fmin_valtage");
            	String fmin_valtage = String.valueOf(fmin_valtage1);
            	if(fmin_valtage.length()!=3){
            		int lenth=3-fmin_valtage.length();
            		for(int i=0;i<lenth;i++){
            			fmin_valtage="0"+fmin_valtage;
            		}
            	}

            	datasend+=fmax_electricity+fmin_electricity+fmax_valtage+fmin_valtage;

            }

        } catch (SQLException e) {

            System.out.println("Broken insert");

            e.printStackTrace();

        } 



        try {

            stmt.close();

             conn.close(); 

        } catch (SQLException e) {

            System.out.println("Broken close");

            e.printStackTrace();

        }  

         return; 



    }


}*/