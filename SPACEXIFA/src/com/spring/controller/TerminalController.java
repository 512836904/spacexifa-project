package com.spring.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spring.model.Junction;
import com.spring.model.ProductionCraft;
import com.spring.model.WeldingMachine;
import com.spring.model.Wps;
import com.spring.service.JunctionService;
import com.spring.service.ProductionCraftService;
import com.spring.service.WeldingMachineService;
import com.spring.service.WpsService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;


@Controller
@RequestMapping(value = "/terminal", produces = {"text/json;charset=UTF-8"})
public class TerminalController {

	private static Connection conn;
	private static Statement stmt;
	private static ResultSet rs;
	private static final String mesurl = "jdbc:oracle:thin:@192.168.11.60:1521/MESDB";	//哈电机MES数据库地址
	private static final String mesuser = "X5USER";
	private static final String mespassword = "X5_USER_1234";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private WpsService wpsService;
	@Autowired
	private WeldingMachineService wmm;
	@Autowired
	private JunctionService junctionService;
	@Autowired
	private ProductionCraftService craftService;

	/**
	 * 手持终端根据IP地址获取采集编号
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getRequestPath")
	public void getRequestPath(HttpServletRequest request, HttpServletResponse response) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();//获得客户端的IP地址
			String remoteHost = request.getRemoteHost();//获得客户端电脑的名字
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				//根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		String macAddress = getMACAddress(ipAddress); //根据IP查询mac地址
		System.out.println("设备MAC地址："+macAddress);
		JSONObject object = new JSONObject();
		//根据手持终端IP地址查询焊机信息
		if (null != ipAddress && !"".equals(ipAddress)) {
			List<WeldingMachine> machineByFip = wmm.findMachineByFip(ipAddress);
			if (null != machineByFip && machineByFip.size() > 0) {
				object.put("fgather_no", machineByFip.get(0).getFgather_no());//采集编号
				object.put("equipmentNo", machineByFip.get(0).getEquipmentNo());//焊机名称
			} else {
				object.put("fgather_no", "");
				object.put("equipmentNo", "");
			}
		} else {
			object.put("fgather_no", "");
			object.put("equipmentNo", "");
		}
		String respondata = JSON.toJSONString(object);
		//构造回调函数格式jsonpCallback(数据)
		try {
			String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback + "(" + respondata + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据IP获得MAC地址
	 * @param ip
	 * @return
	 */
	public String getMACAddress(String ip) {
		String str = "";
		String macAddress = "";
		try {
			Process p = Runtime.getRuntime().exec("nbtstat -a " + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC") > 1) {
						macAddress = str.substring(str.indexOf("=") + 2, str.length());
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return macAddress;
	}

	/**
	 * 根据工票编号查询MES工作号信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/findWorkticketByno")
	public void findWorkticketByno(HttpServletRequest request, HttpServletResponse response) {
		String worksheetcode = request.getParameter("worksheetcode");
		String sql = "";
		String mesworkno = "";
		String comcode = "";
		String partnumber = "";
		String materialname = "";
		String workcode = "";
		if (worksheetcode != null && !"".equals(worksheetcode)) {
			sql = "select * from  userredf.mes_workticket_v where worksheetcode=" + "'" + worksheetcode + "'";
			try {
				Class.forName("oracle.jdbc.OracleDriver");
				if (conn == null || conn.isClosed()) {
					conn = DriverManager.getConnection(mesurl, mesuser, mespassword);
				}
				if (stmt == null || stmt.isClosed()) {
					stmt = conn.createStatement();
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					mesworkno = rs.getString("mesworkno");//工作号
					 comcode = rs.getString("comcode");
					 partnumber = rs.getString("partnumber");
					 materialname = rs.getString("materialname");
					 workcode = rs.getString("worksheetcode");//工票号
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		JSONObject object = new JSONObject();
		object.put("mesworkno", mesworkno);
		object.put("comcode", comcode);
		object.put("partnumber", partnumber);
		object.put("materialname", materialname);
		String respondata = JSON.toJSONString(object);
		try {
			String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback + "(" + respondata + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存工作号、焊缝、工艺等信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/saveWorkAndJunction")
	public void saveWorkAndJunction(HttpServletRequest request, HttpServletResponse response) {
		String cardnumber = request.getParameter("cardnumber");//工票编号
		String productNumber = request.getParameter("productNumber");//工作号
		String fwelding_area = request.getParameter("fwelding_area");//布套号
		String productDrawNo = request.getParameter("productDrawNo");//零件图号
		String productName = request.getParameter("productName");//零件名
		String wpsparameter = request.getParameter("wpsparameter");//工艺参数
		String fgatherno = request.getParameter("fgatherno");//采集编号
		String junctionName = "";//焊缝名称
		String fname = "";//工艺名
		String preheat = "";//预热
		String interlamination = "";//层间
		String welding_material = "";//焊材
		String wire_diameter = "";//焊丝直径
		String wide_swing = "";//摆宽
		String rests = "";//其他
		String maxelectricity = "";//大电流
		String minelectricity = "";//小电流
		String maxvoltage = "";//大电压
		String minvoltage = "";//小电压
		String maxsolder = "";//大焊速
		String minsolder = "";//小焊速
		int junctiob_id = 0; //焊缝id
		String machinemodel = ""; //焊机型号
		BigInteger produ_id = BigInteger.ZERO;//生产工艺id
		BigInteger welded_jun_id = BigInteger.ZERO;//电子跟踪卡id
		int task_id = 0;    //任务id
		int add_id = 0;  //跟踪卡关联工艺
		if (wpsparameter != null && !"".equals(wpsparameter)) {
			String[] split = wpsparameter.split(",");
			for (String str : split) {
				String[] split1 = str.split(":");
				if (split1[0].contains("焊缝")) {
					junctionName = split1[1];
				}
				if (split1[0].contains("WPS")) {
					fname = split1[1];
				}
				if (split1[0].contains("预热")) {
					preheat = split1[1];
				}
				if (split1[0].contains("层间")) {
					interlamination = split1[1];
				}
				if (split1[0].contains("焊材")) {
					welding_material = split1[1];
				}
				if (split1[0].contains("焊丝直径")) {
					wire_diameter = split1[1];
				}
				if (split1[0].contains("摆宽")) {
					wide_swing = split1[1];
				}
				if (split1[0].contains("其他")) {
					rests = split1[1];
				}
				if (split1[0].contains("电流")) {
					String[] ele = split1[1].split("-");
					minelectricity = ele[0];
					maxelectricity = ele[1];
				}
				if (split1[0].contains("电压")) {
					String[] vol = split1[1].split("-");
					minvoltage = vol[0];
					maxvoltage = vol[1];
				}
				if (split1[0].contains("焊速")) {
					String[] vol = split1[1].split("-");
					minsolder = vol[0];
					maxsolder = vol[1];
				}
			}
		}
		//1.根据焊缝名称查焊缝表，有返回id，没有新增并返回id
		if (null != junctionName && !"".equals(junctionName)) {
			List<Junction> junctiobByName = junctionService.findJunctiobByName(junctionName);
			if (null != junctiobByName && junctiobByName.size() > 0) {
				junctiob_id = (int) junctiobByName.get(0).getFid();//焊缝id
			} else {
				//新增
				Junction junction = new Junction();
				junction.setCurrent_limit(maxelectricity);
				junction.setCurrent_lower_limit(minelectricity);
				junction.setFMAXVOLTAGE(maxvoltage);
				junction.setFMINVOLTAGE(minvoltage);
				junction.setJunction_name(junctionName);
				junctiob_id = junctionService.addJunction(junction);
			}
		}
		//2.根据焊缝id查询生产工艺表，有：根据id更新数据，没有：新增工艺返回id
		if (junctiob_id != 0) {
			ProductionCraft productionByjid = craftService.findProductionByjid(BigInteger.valueOf(junctiob_id));
			if (null != productionByjid) {
				productionByjid.setFNAME(fname);
				productionByjid.setPREHEAT(preheat);
				productionByjid.setINTERLAMINATION(interlamination);
				productionByjid.setWELDING_MATERIAL(welding_material);
				productionByjid.setWIRE_DIAMETER(wire_diameter);
				productionByjid.setWIDE_SWING(BigInteger.valueOf(Long.parseLong(wide_swing)));
				productionByjid.setRESTS(rests);
				productionByjid.setELECTRICITY_FLOOR(BigInteger.valueOf(Long.parseLong(minelectricity)));
				productionByjid.setELECTRICITY_UPPER(BigInteger.valueOf(Long.parseLong(maxelectricity)));
				productionByjid.setVOLTAGE_FLOOR(BigInteger.valueOf(Long.parseLong(minvoltage)));
				productionByjid.setVOLTAGE_UPPER(BigInteger.valueOf(Long.parseLong(maxvoltage)));
				productionByjid.setSOLDER_SPEED_FLOOR(BigInteger.valueOf(Long.parseLong(minsolder)));
				productionByjid.setSOLDER_SPEED_UPPER(BigInteger.valueOf(Long.parseLong(maxsolder)));
				productionByjid.setDATA_SOURCES(BigInteger.valueOf(2));//数据来源：终端扫码
				productionByjid.setFJUNCTION(BigInteger.valueOf(junctiob_id));
				craftService.updateProductionCraft(productionByjid);
				produ_id = productionByjid.getFID();//工艺id
			} else {
				ProductionCraft craft = new ProductionCraft();
				craft.setFNAME(fname);
				craft.setPREHEAT(preheat);
				craft.setINTERLAMINATION(interlamination);
				craft.setWELDING_MATERIAL(welding_material);
				craft.setWIRE_DIAMETER(wire_diameter);
				craft.setWIDE_SWING(BigInteger.valueOf(Long.parseLong(wide_swing)));
				craft.setRESTS(rests);
				craft.setELECTRICITY_FLOOR(BigInteger.valueOf(Long.parseLong(minelectricity)));
				craft.setELECTRICITY_UPPER(BigInteger.valueOf(Long.parseLong(maxelectricity)));
				craft.setVOLTAGE_FLOOR(BigInteger.valueOf(Long.parseLong(minvoltage)));
				craft.setVOLTAGE_UPPER(BigInteger.valueOf(Long.parseLong(maxvoltage)));
				craft.setSOLDER_SPEED_FLOOR(BigInteger.valueOf(Long.parseLong(minsolder)));
				craft.setSOLDER_SPEED_UPPER(BigInteger.valueOf(Long.parseLong(maxsolder)));
				craft.setDATA_SOURCES(BigInteger.valueOf(2));//数据来源：终端扫码
				craft.setFJUNCTION(BigInteger.valueOf(junctiob_id));
				craft.setCREATE_TIME(sdf.format(System.currentTimeMillis()));
				craftService.addProductionCraft(craft);
				produ_id = craft.getFID();//工艺id
			}
		}
		//3.新增电子跟踪卡，根据工票编号查询，有，更新，没有：新增返回id
		if (null != cardnumber && !"".equals(cardnumber)) {
			Wps wps = wpsService.findWeldedJunctionByNum(cardnumber);
			if (null != wps) {
				wps.setJOB_NUMBER(productNumber);
				wps.setSET_NUMBER(fwelding_area);
				wps.setPART_DRAWING_NUMBER(productDrawNo);
				wps.setPART_NAME(productName);
				wps.setCraft_param(wpsparameter);
				wpsService.updateWpsLibrary(wps);
				welded_jun_id = wps.getFid();    //电子跟踪卡id
			} else {
				Wps wp = new Wps();
				wp.setJOB_NUMBER(productNumber);
				wp.setSET_NUMBER(fwelding_area);
				wp.setPART_DRAWING_NUMBER(productDrawNo);
				wp.setPART_NAME(productName);
				wp.setCraft_param(wpsparameter);
				wp.setWorkticket_number(cardnumber);
				wpsService.addWpsLibrary(wp);
				welded_jun_id = wp.getFid();    //电子跟踪卡id
			}
		}
		//4.绑定电子跟踪卡和工艺（一对一）
		if (!welded_jun_id.equals(BigInteger.ZERO) && !produ_id.equals(BigInteger.ZERO)) {
			add_id = craftService.addLiarbryJunction(welded_jun_id, produ_id);
		}
		//5.根据采集编号查询焊机id
		if (null != fgatherno && !"".equals(fgatherno)) {
			WeldingMachine machineByGatherNo = wmm.findMachineByGatherNo(fgatherno);
			// 6.新增任务执行表数据
			if (null != machineByGatherNo) {
				machinemodel = machineByGatherNo.getModel();//焊机型号
				Wps wps = new Wps();
				wps.setFwelded_junction_no(String.valueOf(welded_jun_id));//电子跟踪卡id
				wps.setMacid(machineByGatherNo.getId());//焊机id
				wps.setJunction_id(junctiob_id);
				task_id = wpsService.overCard(wps);
			}
		}
		boolean flag = false;
		if (junctiob_id != 0 && !produ_id.equals(BigInteger.ZERO) && !welded_jun_id.equals(BigInteger.ZERO) && task_id != 0 && add_id != 0) {
			flag = true;
		}
		JSONObject object = new JSONObject();
		object.put("junctiob_id", junctiob_id);//焊缝id
		object.put("produ_id", produ_id);//生产工艺id
		object.put("welded_jun_id", welded_jun_id);//电子跟踪卡id
		object.put("task_id", task_id);//任务执行id
		object.put("machinemodel", machinemodel);//焊机型号
		object.put("flag", flag);//保存结果
		String respondata = JSON.toJSONString(object);
		try {
			String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback + "(" + respondata + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据工艺名查询焊缝
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/findJunctionByFname")
	public void findJunctionByFname(HttpServletRequest request, HttpServletResponse response) {
		String fname = request.getParameter("fname");
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		if (null != fname && !"".equals(fname)) {
			List<Junction> junctionList = junctionService.findJunctionByFname(fname);
			if (null != junctionList && junctionList.size() > 0) {
				for (Junction j : junctionList) {
					json.put("id", j.getFid());
					json.put("text", j.getJunction_name());
					array.add(json);
				}
			}
		}
		JSONObject object = new JSONObject();
		object.put("array", array);
		String respondata = JSON.toJSONString(object);
		try {
			String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback + "(" + respondata + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有工艺名
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/findAllProductionName")
	public void findAllProductionName(HttpServletRequest request, HttpServletResponse response) {
		List<ProductionCraft> allProductionList = craftService.findAllProductionName();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		if (null != allProductionList && allProductionList.size() > 0) {
			for (ProductionCraft craft : allProductionList) {
				json.put("id", craft.getFNAME());
				json.put("text", craft.getFNAME());
				array.add(json);
			}
		}
		JSONObject object = new JSONObject();
		object.put("array", array);
		String respondata = JSON.toJSONString(object);
		try {
			String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback + "(" + respondata + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据焊缝id查询工艺参数
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/findCraftByJunctionId")
	public void findCraftByJunctionId(HttpServletRequest request, HttpServletResponse response) {
		String junction_id = request.getParameter("junction_id");
		String craft_param = "";
		if (null != junction_id && !"".equals(junction_id)) {
			Wps wps = wpsService.findCraftByJunctionId(BigInteger.valueOf(Long.parseLong(junction_id)));
			if (null != wps) {
				craft_param = wps.getCraft_param();
			}

		}
		JSONObject object = new JSONObject();
		object.put("craft_param", craft_param);
		String respondata = JSON.toJSONString(object);
		try {
			String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback + "(" + respondata + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}