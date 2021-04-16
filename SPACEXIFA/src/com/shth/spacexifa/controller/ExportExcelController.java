package com.shth.spacexifa.controller;

import com.shth.spacexifa.dto.WeldDto;
import com.shth.spacexifa.model.*;
import com.shth.spacexifa.service.*;
import com.shth.spacexifa.util.CommonExcelUtil;
import com.shth.spacexifa.util.IsnullUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/export", produces = { "text/json;charset=UTF-8" })
public class ExportExcelController {

	private static final long serialVersionUID = -4171187629012625142L;

	@Autowired
	private WeldingMachineService wmm;
	@Autowired
	private DataStatisticsService dss;
	@Autowired
	private MaintainService mm;
	@Autowired
	private InsframeworkService im;
	@Autowired
	private GatherService gs;
	@Autowired
	private PersonService ps;
	@Autowired
	private WeldedJunctionService wjm;
	@Autowired
	private ReportService reportService;
	@Autowired
	private WpsService wps;

	private String filename;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
	IsnullUtil iutil = new IsnullUtil();

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@RequestMapping("/exporWeldingMachine")
	@ResponseBody
	public ResponseEntity<byte[]> exporWeldingMachine(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		try {
			String str = (String) request.getSession().getAttribute("searchStr");
			List<WeldingMachine> list = wmm.getWeldingMachine(str);
			String dtime = null;
			String[] titles = new String[] { "设备名称", "设备类型", "入厂时间", "所属项目", "状态", "厂家", "是否在网", "采集序号", "位置", "ip地址",
					"型号" };
			Object[][] data = new Object[list.size()][11];
			for (int i = 0; i < list.size(); i++) {
				data[i][0] = list.get(i).getEquipmentNo();
				data[i][1] = list.get(i).getTypename();
				data[i][2] = list.get(i).getJoinTime();
				data[i][3] = list.get(i).getInsframeworkId().getName();
				data[i][4] = list.get(i).getStatusname();
				data[i][5] = list.get(i).getMvaluename();
				if (list.get(i).getIsnetworking() == 0) {
					data[i][6] = "是";
				} else {
					data[i][6] = "否";
				}
				Gather gather = list.get(i).getGatherId();
				if (gather != null) {
					data[i][7] = gather.getGatherNo();
				} else {
					data[i][7] = null;
				}
				data[i][8] = list.get(i).getPosition();
				data[i][9] = list.get(i).getIp();
				data[i][10] = list.get(i).getModelname();
			}
			filename = "焊机设备" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
//			 String contextpath=scontext. getContextPath() ; //获取虚拟路径

			String path = abpath + "excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "焊机设备数据");

			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";

			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	@RequestMapping("/exporWeldwps")
	@ResponseBody
	public ResponseEntity<byte[]> exporWeldwps(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		try {
			List<Wps> list = wps.getstepall();
			String dtime = null;
			String wps_lib_id = "";
			String employ_id = "";
			String step_id = "";
			int c = 0;
			String[] titles = new String[] { "产品图号", "产品名称", "产品版本号", "工艺规程编号", "工艺规程版本号", "工艺来源", "审核状态",
					"工序号", "工序版本号", "工序名称", "工步号", "工步名称", "工步版本", "焊缝编号", "焊接部位", "量化项目", "要求值", "上偏差", "下偏差", "计量单位" };
			Object[][] data = new Object[list.size()][20];
			for (int k = 0; k < list.size(); k++) {
				data[k][0] = list.get(k).getFproduct_drawing_no();
				data[k][1] = list.get(k).getFproduct_name();
				data[k][2] = list.get(k).getFproduct_version();
				data[k][3] = list.get(k).getFwpsnum();
				data[k][4] = list.get(k).getFwps_lib_version();
				if(list.get(k).getFlag() == 0) {
					data[k][5] = "自建";
				}else {
					data[k][5] = "MES";
				}
				if(list.get(k).getFstatus() == 2) {
					data[k][6] = "被驳回";
				}else if(list.get(k).getFstatus() == 1){
					data[k][6] = "已通过";
				}else {
					data[k][6] = "未审核";
				}
				data[k][7] = list.get(k).getFemployee_id();
				data[k][8] = list.get(k).getFemployee_name();
				data[k][9] = list.get(k).getFemployee_version();
				data[k][10] = list.get(k).getFstep_number();
				data[k][11] = list.get(k).getFstep_name();
				data[k][12] = list.get(k).getFstep_version();
				data[k][13] = list.get(k).getFjunction();
				data[k][14] = list.get(k).getFwelding_area();
				data[k][15] = list.get(k).getFquantitative_project();
				data[k][16] = list.get(k).getFrequired_value();
				data[k][17] = list.get(k).getFupper_deviation();
				data[k][18] = list.get(k).getFlower_deviation();
				data[k][19] = list.get(k).getFunit_of_measurement();
			}
			filename = "焊接工艺参数" + sdf.format(new Date()) + ".xls";
			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "焊接工艺数据");

			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";

			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			if(file!=null) {
				file.delete();
			}
		}
	}

	@RequestMapping("/exportTaskview")
	@ResponseBody
	public ResponseEntity<byte[]> exportTaskview(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		try {
			String str = java.net.URLDecoder.decode(request.getParameter("search"), "utf-8");
			//String str = (String) request.getSession().getAttribute("search");
			List<Wps> list = wps.gettaskview(str);
			String dtime = null;
			String[] titles = new String[] { "任务编号", "焊工姓名", "焊机编号", "电子跟踪卡号", "产品图号", "产品序号", "产品名称", "工艺规程编号", "版本", "焊缝编号",
					"焊接部位","工步号","组织机构","工艺来源","任务开始时间","任务结束时间","任务状态" };
			Object[][] data = new Object[list.size()][17];
			for (int i = 0; i < list.size(); i++) {
				data[i][0] = list.get(i).getFwpsnum();
				data[i][1] = list.get(i).getWeldername();
				data[i][2] = list.get(i).getConname();
				data[i][3] = list.get(i).getFproduct_drawing_no();
				data[i][4] = list.get(i).getDianame();
				data[i][5] = list.get(i).getFproduct_version();
				data[i][6] = list.get(i).getFproduct_name();
				data[i][7] = list.get(i).getFprocessname();
				data[i][8] = list.get(i).getFwps_lib_version();
				data[i][9] = list.get(i).getFjunction();
				data[i][11] = list.get(i).getFstep_number();
				data[i][12] = list.get(i).getFitem();
				if (list.get(i).getFlag() == 0) {
					data[i][13] = "自建";
				} else {
					data[i][13] = "MES";
				}
				data[i][14] = list.get(i).getFstarttime();
				data[i][15] = list.get(i).getEndtime();
				if (list.get(i).getFtorch() == 0) {
					data[i][16] = "完成";
				} else {
					data[i][16] = "未完成";
				}
			}
			filename = "生产任务明细" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "生产任务明细");

			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";

			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}
	
	@RequestMapping("/exportunstard")
	@ResponseBody
	public ResponseEntity<byte[]> exportunstard(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		try {
			String str = java.net.URLDecoder.decode(request.getParameter("search"), "utf-8");
			//String str = (String) request.getSession().getAttribute("search");
			List<Wps> list = wps.getunstard(str);
			String dtime = null;
			BigInteger unstandtime = null;
			String[] titles = new String[] { "任务编号", "焊工姓名", "焊机编号", "电子跟踪卡号", "产品图号", "产品序号", "产品名称", "工艺规程编号", "规程版本","工序号","工序名","工步号","工步名",
					"焊接部位","焊缝编号","组织机构","超标时长"};
			Object[][] data = new Object[list.size()][17];
			for (int i = 0; i < list.size(); i++) {
				data[i][0] = list.get(i).getFwpsnum();
				data[i][1] = list.get(i).getWeldername();
				data[i][2] = list.get(i).getConname();
				data[i][3] = list.get(i).getFproduct_drawing_no();
				data[i][4] = list.get(i).getDianame();
				data[i][5] = list.get(i).getFproduct_version();
				data[i][6] = list.get(i).getFproduct_name();
				data[i][7] = list.get(i).getFprocessname();
				data[i][8] = list.get(i).getFwps_lib_version();
				data[i][9] = list.get(i).getFemployee_id();
				data[i][10] = list.get(i).getFemployee_name();
				data[i][11] = list.get(i).getFstep_number();
				data[i][12] = list.get(i).getFstep_name();
				data[i][13] = list.get(i).getFwelding_area();
				data[i][14] = list.get(i).getFjunction();
				data[i][15] = list.get(i).getFitem();
				unstandtime = list.get(i).getUnstandardtime();
				if(unstandtime != null) {
					data[i][16] = getTimeStrBySecond(unstandtime);
				}else {
					data[i][16] = "00:00:00";
				}
			}
			filename = "超标统计报表" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "生产任务明细");

			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";

			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}
	
	@RequestMapping("/exporMaintain")
	@ResponseBody
	public ResponseEntity<byte[]> exporMaintain(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		try {
			String str = (String) request.getSession().getAttribute("searchStr");
			List<WeldingMaintenance> list = mm.getWeldingMaintenanceAll(im.getUserInsframework(), str);
			String dtime = null;
			String[] titles = new String[] { "设备编码", "维修人员", "维修起始时间", "维修结束时间", "维修类型", "维修说明" };
			Object[][] data = new Object[list.size()][6];
			for (int i = 0; i < list.size(); i++) {
				data[i][0] = list.get(i).getWelding().getEquipmentNo();
				data[i][1] = list.get(i).getMaintenance().getViceman();
				data[i][2] = list.get(i).getMaintenance().getStartTime();
				data[i][3] = list.get(i).getMaintenance().getEndTime();
				data[i][4] = list.get(i).getMaintenance().getTypename();
				data[i][5] = list.get(i).getMaintenance().getDesc();
			}
			filename = "焊机维修" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "焊机维修数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 班组生产数据报表导出
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportItemdata")
	@ResponseBody
	public ResponseEntity<byte[]> exporItemdata(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		DecimalFormat df = new DecimalFormat("#.00");
		String dtime = "统计日期：" + time1 + "--" + time2;
		List<DataStatistics> count = null;
		double weldernum =0; //刷卡焊工数量
		try {
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			List<DataStatistics> list = dss.getAllItemData();
			String[] titles = new String[] { "所属班组", "设备总数", "开机设备数", "实焊设备数", "设备利用率(%)", "焊接时间","人均焊接时长", "工作时间","人均工作时长",
					"焊接效率(%)", "焊丝消耗(KG)","人均焊丝消耗(KG)", "电能消耗(KWH)", "气体消耗(L)" };
			Object[][] data = new Object[list.size()][15];
			int ii = 0;
			for (DataStatistics i : list) {
				if (ii < list.size()) {
					data[ii][0] = i.getName();// 所属班组
					data[ii][1] = i.getTotal();// 设备总数
					int machinenum = 0;
					BigInteger starttime = null;
					DataStatistics weldtime = null;
					//DataStatistics junction = dss.getWorkJunctionNum(i.getId(), dto);// 获取工作(焊接)的焊口数
					DataStatistics parameter = dss.getParameter();// 获取参数
					BigInteger standytime = null;
//					if (junction.getJunctionnum() != 0) {
						machinenum = dss.getStartingUpMachineNum(i.getId(), dto);// 获取开机焊机总数
						starttime = dss.getStaringUpTime(i.getId(), dto);// 获取开机总时长
						data[ii][2] = machinenum;// 开机设备数
						data[ii][7] = getTimeStrBySecond(starttime);// 工作时间
						standytime = dss.getStandytime(i.getId(), dto);// 获取待机总时长
						weldtime = dss.getItemWorkTime(i.getId(), dto);// 获取焊接时长，平均电流电压
					    count = dss.getPersonWorkTime(i.getId(), time1);//获取工区或班组的打卡人数
					    weldernum = count.get(0).getNum();
						double standytimes = 0, time = 0, electric = 0;
						if (standytime != null) {
							standytimes = standytime.doubleValue() / 60 / 60;
							if(weldernum>0){
								data[ii][8] = df.format((starttime.doubleValue()/(weldernum*3600)));// 人均工作时间
							}else{
								data[ii][8] = 0;// 人均工作时间
							}
						}
						if (weldtime != null) {
							electric = (double) Math.round((weldtime.getWorktime().doubleValue() / 60 / 60 * (weldtime.getElectricity() * weldtime.getVoltage())/ 1000*0.8 + standytimes * parameter.getStandbypower() / 1000) * 100) / 100;// 电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
						} else {
							electric = (double) Math.round((time + standytimes * parameter.getStandbypower() / 1000) * 100) / 100;// 电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
						}
						data[ii][12] = electric;// 电能消耗
//					} else {
//						data[ii][2] = 0;
//						data[ii][6] = "00:00:00";
//						data[ii][9] = 0;
//					}
					if (i.getTotal() != 0 && weldtime != null) {
						DataStatistics machine = dss.getWorkMachineNum(i.getId(), dto);// 获取工作(焊接)的焊机数
						if (machine != null) {
							data[ii][3] = machine.getMachinenum();// 实焊设备数
							data[ii][5] = df.format(weldtime.getWorktime().doubleValue()/3600);// 焊接时间
							if(weldernum>0){
								data[ii][6] = df.format((weldtime.getWorktime().doubleValue()/(weldernum*3600)));// 人均焊接时间
							}else{
								data[ii][6] = 0;// 人均焊接时间
							}
							double useratio = (double) Math
									.round(Double.valueOf(machinenum) / Double.valueOf(i.getTotal()) * 100 * 100) / 100;
							double weldingproductivity = (double) Math
									.round(weldtime.getWorktime().doubleValue() / starttime.doubleValue() * 100 * 100)
									/ 100;
							data[ii][4] = useratio;// 设备利用率
							data[ii][9] = weldingproductivity;// 焊接效率
						}
						if (parameter != null) {
							time = weldtime.getWorktime().doubleValue() / 60;
							String[] str = parameter.getWireweight().split(",");
							double wireweight = Double.valueOf(str[0]);
							//double wire = (double) Math.round(wireweight * parameter.getSpeed() * time * 100) / 100;// 焊丝消耗量=焊丝|焊丝重量*送丝速度*焊接时间
							double wire =weldtime.getWirefeedrate();
							double air = (double) Math.round(parameter.getAirflow() * time * 100) / 100;// 气体消耗量=气体流量*焊接时间
							data[ii][10] = df.format(wire);// 焊丝消耗
							if(weldernum>0){
								data[ii][11] = df.format((wire/weldernum));// 人均焊丝消耗
							}else{
								data[ii][11] = 0;// 人均焊丝消耗
							}
							data[ii][13] = air;// 气体消耗
						}
					} else {
						data[ii][3] = 0;// 实焊设备数
						data[ii][5] = "00:00:00";// 焊接时间
						data[ii][4] = 0;// 设备利用率
						data[ii][9] = 0;// 焊接效率
						data[ii][2] = 0;// 开机设备数
						data[ii][10] = 0;// 焊丝消耗
						data[ii][13] = 0;// 气体消耗
					}
				}
				ii++;
			}
			filename = "班组生产数据" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "班组生产数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 班组焊接数据报表导出
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportItemWelddata")
	@ResponseBody
	public ResponseEntity<byte[]> exportItemWelddata(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期：" + time1 + "--" + time2;
		try {
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			List<DataStatistics> ilist = dss.getWeldItemInCountData(dto);
			String[] titles = new String[] { "所属班组", "累计焊接时间", "正常段时长", "超规范时长", "规范符合率(%)" };
			Object[][] data = new Object[ilist.size()][5];
			int ii = 0;
			for (DataStatistics i : ilist) {
				if (ii < ilist.size()) {
					data[ii][0] = i.getName();// 所属班组
					data[ii][1] = getTimeStrBySecond(i.getInsid());// 累计焊接时间
					data[ii][2] = getTimeStrBySecond(i.getInsid().subtract(i.getWorktime()));// 正常焊接时长
					data[ii][3] = getTimeStrBySecond(i.getWorktime());// 超规范焊接时长
					if (Integer.valueOf(i.getInsid().toString()) + Integer.valueOf(i.getWorktime().toString()) != 0) {
						data[ii][4] = new DecimalFormat("0.00")
								.format((float) Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())
										/ (Integer.valueOf(i.getInsid().toString())) * 100);// 规范符合率
					} else {
						data[ii][4] = 0;
					}
				}
				ii++;
			}
			filename = "班组焊接数据" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "班组焊接数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 设备生产数据报表导出
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportMachineData")
	@ResponseBody
	public ResponseEntity<byte[]> exportMachineData(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String item = request.getParameter("item");
		WeldDto dto = new WeldDto();
		BigInteger itemid = null;
		String dtime = "统计日期：" + time1 + "--" + time2;
		try {
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			if (iutil.isNull(item)) {
				itemid = new BigInteger(item);
			}
			List<DataStatistics> list = dss.getAllMachineData(itemid);
			String[] titles = new String[] { "所属班组", "设备编号", "焊接时间", "工作时间", "焊接效率(%)", "焊丝消耗(KG)",
					"电能消耗(KWH)", "气体消耗(L)" };
			Object[][] data = new Object[list.size()][9];
			int ii = 0;
			for (DataStatistics i : list) {
				if (ii < list.size()) {
					dto.setMachineid(i.getId());
					data[ii][0] = i.getInsname();
					data[ii][1] = i.getName();
					//DataStatistics junctionnum = dss.getWorkJunctionNum(null, dto);
					DataStatistics parameter = dss.getParameter();
					BigInteger worktime = null, standytime = null;
					DataStatistics weld = null;
					//if (junctionnum.getJunctionnum() != 0) {
						//data[ii][2] = junctionnum.getJunctionnum();// 焊接焊缝数
						worktime = dss.getStaringUpTime(i.getInsid(), dto);
						data[ii][3] = getTimeStrBySecond(worktime);// 工作时间
						standytime = dss.getStandytime(i.getInsid(), dto);// 获取待机总时长
						weld = dss.getWorkTimeAndEleVol(i.getInsid(), dto);
						double standytimes = 0, time = 0, electric = 0;
						if (standytime != null) {
							standytimes = standytime.doubleValue() / 60 / 60;
						}
						if (weld != null) {
							electric = (double) Math.round((weld.getWorktime().doubleValue() / 60 / 60
									* (weld.getElectricity() * weld.getVoltage()) / 1000
									+ standytimes * parameter.getStandbypower() / 1000) * 100) / 100;// 电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
						} else {
							electric = (double) Math
									.round((time + standytimes * parameter.getStandbypower() / 1000) * 100) / 100;// 电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
						}
						data[ii][6] = electric;// 电能消耗
//					} else {
//						data[ii][2] = 0;
//						data[ii][3] = "00:00:00";
//						data[ii][6] = 0;
//					}
					if (weld != null) {
						data[ii][2] = getTimeStrBySecond(weld.getWorktime());// 焊接时间
						data[ii][3] = getTimeStrBySecond(worktime);// 工作时间
						double weldingproductivity = (double) Math
								.round(weld.getWorktime().doubleValue() / worktime.doubleValue() * 100 * 100) / 100;
						data[ii][4] = weldingproductivity;// 焊接效率
						if (parameter != null) {
							time = weld.getWorktime().doubleValue() / 60;
							String[] str = parameter.getWireweight().split(",");
							double wireweight = Double.valueOf(str[0]);
							double wire = (double) Math.round(wireweight * parameter.getSpeed() * time * 100) / 100;// 焊丝消耗量=焊丝|焊丝重量*送丝速度*焊接时间
							double air = (double) Math.round(parameter.getAirflow() * time * 100) / 100;// 气体消耗量=气体流量*焊接时间
							data[ii][5] = wire;// 焊丝消耗
							data[ii][7] = air;// 气体消耗
						}
					} else {
						data[ii][2] = "00:00:00";
						data[ii][4] = 0;
						data[ii][5] = 0;
						data[ii][7] = 0;
					}
				}
				ii++;
			}
			filename = "设备生产数据" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "设备生产数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 设备焊接数据报表导出
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportMachineWelddata")
	@ResponseBody
	public ResponseEntity<byte[]> exportMachineWelddata(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String item = request.getParameter("item");
		WeldDto dto = new WeldDto();
		BigInteger itemid = null;
		String dtime = "统计日期：" + time1 + "--" + time2;
		try {
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			if (iutil.isNull(item)) {
				itemid = new BigInteger(item);
			}
			List<DataStatistics> ilist = dss.getWeldMachineInCountData(dto, itemid);
			String[] titles = new String[] { "设备编码", "所属班组", "累计焊接时间", "正常段时长", "超规范时长", "规范符合率(%)" };
			Object[][] data = new Object[ilist.size()][6];
			int ii = 0;
			for (DataStatistics i : ilist) {
				if (ii < ilist.size()) {
					data[ii][0] = i.getName();// 焊机编号
					data[ii][1] = i.getInsname();// 所属班组
					data[ii][2] = getTimeStrBySecond(i.getInsid());// 累计焊接时间
					data[ii][3] = getTimeStrBySecond(i.getInsid().subtract(i.getWorktime()));// 正常焊接时长
					data[ii][4] = getTimeStrBySecond(i.getWorktime());// 超规范焊接时长
					if (Integer.valueOf(i.getInsid().toString()) + Integer.valueOf(i.getWorktime().toString()) != 0) {
						data[ii][5] = new DecimalFormat("0.00")
								.format((float) Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())
										/ (Integer.valueOf(i.getInsid().toString())) * 100);// 规范符合率
					} else {
						data[ii][5] = 0;
					}
				}
				ii++;
			}
			filename = "设备焊接数据" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "设备焊接数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 人员生产数据报表导出
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportPersonData")
	@ResponseBody
	public ResponseEntity<byte[]> exportPersonData(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String weldername = request.getParameter("weldername");
		String weldernum = request.getParameter("weldernum");
		String item = request.getParameter("item");
		WeldDto dto = new WeldDto();
		BigInteger itemid = null;
		String dtime = "统计日期：" + time1 + "--" + time2;
		try {
			if (iutil.isNull(item)) {
				itemid = new BigInteger(item);
			} else {
				itemid = im.getUserInsframework();
			}
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			if (iutil.isNull(weldername)) {
				dto.setJunctionno('%'+weldername+'%');
			}
			if (iutil.isNull(weldernum)) {
				dto.setWelderno('%'+weldernum+'%');
			}
			List<DataStatistics> list = dss.getAllPersonData(itemid,dto);
			String[] titles = new String[] { "焊工编号", "焊工名称", "焊接时间", "工作时间", "焊接效率(%)", "焊丝消耗(KG)",
					"电能消耗(KWH)", "气体消耗(L)", "规范符合率(%)" };
			Object[][] data = new Object[list.size()][9];
			int ii = 0;
			for (DataStatistics i : list) {
				if (ii < list.size()) {
					dto.setPersonid(i.getId());
					data[ii][0] = i.getSerialnumber();
					data[ii][1] = i.getName();
					DataStatistics weld = null;
					BigInteger worktime = null, standytime = null;
					//DataStatistics junctionnum = dss.getWorkJunctionNumByWelder(null, dto);
					DataStatistics parameter = dss.getParameter();
					worktime = dss.getStaringUpTimeByWelder(null, dto);
					if (worktime != null) {
						data[ii][3] = getTimeStrBySecond(worktime);// 工作时间
					}else {
						data[ii][3] = "00:00:00";
					}
					standytime = dss.getStandytimeByWelder(null, dto);
					weld = dss.getWorkTimeAndEleVolByWelder(null, dto);
					double standytimes = 0, time = 0, electric = 0;
					if (standytime != null) {
						standytimes = standytime.doubleValue() / 60 / 60;
					}
					if (weld != null) {
						data[ii][2] = getTimeStrBySecond(weld.getWorktime());// 焊接时间
						if(weld.getWorktime().doubleValue()!=0 && worktime.doubleValue()!=0){
							double weldingproductivity = (double) Math.round(weld.getWorktime().doubleValue() / worktime.doubleValue() * 100 * 100) / 100;
							data[ii][4] = weldingproductivity;// 焊接效率
						}else{
							data[ii][4] = "00:00:00";//焊接效率
						}
						time = weld.getWorktime().doubleValue() / 60 / 60;
						electric = (double) Math.round((time * (weld.getElectricity() * weld.getVoltage()) / 1000 + standytimes * parameter.getStandbypower() / 1000) * 100) / 100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
						double wire = weld.getWirefeedrate();
						double air = (double) Math.round(parameter.getAirflow() * time * 100) / 100;//气体消耗量=气体流量*焊接时间
						if (String.valueOf(weld.getWorktime()).equals("0")) {
							data[ii][8]=0;//规范符合率
						} else {
							String sperate = new DecimalFormat("0.00").format((float) Integer.valueOf(weld.getWorktime().subtract(new BigInteger(weld.getTime())).toString()) / (Integer.valueOf(weld.getWorktime().toString())) * 100);
							data[ii][8]=sperate;//规范符合率
						}
						data[ii][5]=wire;
						data[ii][7]=air;
					}else{
						electric = (double) Math.round((time + standytimes * parameter.getStandbypower() / 1000) * 100) / 100;
						data[ii][2] = "00:00:00";
						data[ii][5]=0;
						data[ii][6] = 0;
						data[ii][7]=0;
						data[ii][8]=0;
					}
					data[ii][6] = electric;// 电能消耗
				}
				ii++;
			}
			filename = "人员生产数据" + sdf.format(new Date()) + ".xls";
			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "人员生产数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 人员焊接数据报表导出
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportPersonWelddata")
	@ResponseBody
	public ResponseEntity<byte[]> exportPersonWelddata(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期：" + time1 + "--" + time2;
		try {
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			dto.setParent(im.getUserInsframework());
			List<DataStatistics> ilist = dss.getWeldPersonInCountData(dto);
			String[] titles = new String[] { "焊工编号", "焊工姓名", "累计焊接时间", "正常段时长", "超规范时长", "规范符合率(%)" };
			Object[][] data = new Object[ilist.size()][6];
			int ii = 0;
			for (DataStatistics i : ilist) {
				if (ii < ilist.size()) {
					data[ii][0] = i.getInsname();// 焊工编号
					data[ii][1] = i.getName();// 焊工 姓名
					data[ii][2] = getTimeStrBySecond(i.getInsid());// 累计焊接时间
					data[ii][3] = getTimeStrBySecond(i.getInsid().subtract(i.getWorktime()));// 正常焊接时长
					data[ii][4] = getTimeStrBySecond(i.getWorktime());// 超规范焊接时长
					if (Integer.valueOf(i.getInsid().toString()) + Integer.valueOf(i.getWorktime().toString()) != 0) {
						data[ii][5] = new DecimalFormat("0.00")
								.format((float) Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())
										/ (Integer.valueOf(i.getInsid().toString())) * 100);// 规范符合率
					} else {
						data[ii][5] = 0;
					}
				}
				ii++;
			}
			filename = "人员焊接数据" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "人员焊接数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 工件生产数据报表导出
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportWorkpieceData")
	@ResponseBody
	public ResponseEntity<byte[]> exportWorkpieceData(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String junctionno = request.getParameter("junctionno");
		String search="";
		if(iutil.isNull(request.getParameter("searchStr"))){
			try {
				search =URLDecoder.decode(request.getParameter("searchStr"),"utf-8");
				} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		WeldDto dto = new WeldDto();
		double temp1=0,temp2=0,time=0,count=0, air=0;
		double electric=0,weldingproductivity=0,electricity=0,voltage=0;
		DecimalFormat format = new DecimalFormat("0.00");
		String dtime = "统计日期：" + time1 + "--" + time2;
		try {
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			List<WeldedJunction> list = wjm.getExportJunctionstandtime(search);
			DataStatistics parameter = dss.getParameter();
			//List<DataStatistics> list = dss.getAllJunctionData("%" + junctionno + "%");
			String[] titles = new String[] { "工作号", "部套号", "零件图号","零件名","工艺编号","焊缝名称","焊工姓名","焊接时长", "工作时长(%)", "焊丝消耗(KG)", "电能消耗(KWH)", "气体消耗(L)",
					"焊接效率(%)" };
			Object[][] data = new Object[list.size()][13];
			int ii = 0;
			for (WeldedJunction w : list) {
				if (ii < list.size()) {
					data[ii][0] = w.getFprefix_number();// 工作号
					data[ii][1] = w.getFsuffix_number();// 部套号
					data[ii][2] = w.getRoomNo();// 零件图号
					data[ii][3] = w.getArea();// 零件名
					data[ii][4] = w.getFproduct_number();// 工艺编号
					data[ii][5] = w.getWeldedJunctionno();// 焊缝名称
					data[ii][6] = w.getFwelder_name();// 焊工姓名
					data[ii][7] = getTimeStrBySecond(w.getHours());// 焊接时长
					data[ii][8] = getTimeStrBySecond(w.getHours().add(w.getCounts()));// 工作时长
					if (w.getHours() != null) {
						time = w.getHours().doubleValue() / 60 / 60;//焊接时长(h)
						count = w.getCounts().doubleValue() / 60 / 60;//待机时长(h)
						weldingproductivity = (double) Math.round(w.getHours().doubleValue() / (w.getHours().doubleValue()+w.getCounts().doubleValue()) * 100 );
						air = (double) Math.round(parameter.getAirflow() * time * 100) / 100;//气体消耗量=气体流量*焊接时间
					}
					if(w.getMaxElectricity()!=0){
						electricity = w.getMaxElectricity();
						voltage = w.getMaxValtage();
						electric = (double) Math.round((time * electricity * voltage / 1000 +w.getCounts().doubleValue()*parameter.getStandbypower() / 1000) * 100) / 100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
					}else{
						electric = (double) Math.round((count * parameter.getStandbypower() / 1000) * 100) / 100;
					}
					data[ii][9] = format.format(new BigDecimal(w.getMaterial()));// 焊丝消耗
					data[ii][10] = electric;// 电能消耗
					data[ii][11] = air;// 气体消耗
					data[ii][12] = weldingproductivity;// 焊接效率
				}
				ii++;
			}
			filename = "工件生产数据" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "工件生产数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 工件焊接数据报表导出
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportWorkpieceWelddata")
	@ResponseBody
	public ResponseEntity<byte[]> exportWorkpieceWelddata(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String junctionno = request.getParameter("junctionno");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期：" + time1 + "--" + time2;
		try {
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			List<DataStatistics> ilist = dss.getWeldWorkpieceInCountData(dto, "%" + junctionno + "%");
			String[] titles = new String[] { "焊缝编号", "累计焊接时间", "正常段时长", "超规范时长", "规范符合率(%)" };
			Object[][] data = new Object[ilist.size()][5];
			int ii = 0;
			for (DataStatistics i : ilist) {
				if (ii < ilist.size()) {
					data[ii][0] = i.getInsname();// 工件编号
					data[ii][1] = getTimeStrBySecond(i.getInsid());// 累计焊接时间
					data[ii][2] = getTimeStrBySecond(i.getInsid().subtract(i.getWorktime()));// 正常焊接时长
					data[ii][3] = getTimeStrBySecond(i.getWorktime());// 超规范焊接时长
					if (Integer.valueOf(i.getInsid().toString()) + Integer.valueOf(i.getWorktime().toString()) != 0) {
						data[ii][4] = new DecimalFormat("0.00")
								.format((float) Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())
										/ (Integer.valueOf(i.getInsid().toString())));// 规范符合率
					} else {
						data[ii][4] = 0;
					}
				}
				ii++;
			}
			filename = "工件焊接数据" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "工件焊接数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			file.delete();
		}
	}

	public String getTimeStrBySecond(BigInteger timeParam) {
		if (timeParam == null) {
			return "00:00:00";
		}
		BigInteger[] str = timeParam.divideAndRemainder(new BigInteger("60"));// divideAndRemainder返回数组。第一个是商第二个时取模
		BigInteger second = str[1];
		BigInteger minuteTemp = timeParam.divide(new BigInteger("60"));// subtract：BigInteger相减，multiply：BigInteger相乘，divide
																		// : BigInteger相除
		if (minuteTemp.compareTo(new BigInteger("0")) > 0) {// compareTo：比较BigInteger类型的大小，大则返回1，小则返回-1 ，等于则返回0
			BigInteger[] minstr = minuteTemp.divideAndRemainder(new BigInteger("60"));
			BigInteger minute = minstr[1];
			BigInteger hour = minuteTemp.divide(new BigInteger("60"));
			if (hour.compareTo(new BigInteger("0")) > 0) {
				return (hour.compareTo(new BigInteger("9")) > 0 ? (hour + "") : ("0" + hour)) + ":"
						+ (minute.compareTo(new BigInteger("9")) > 0 ? (minute + "") : ("0" + minute)) + ":"
						+ (second.compareTo(new BigInteger("9")) > 0 ? (second + "") : ("0" + second));
			} else {
				return "00:" + (minute.compareTo(new BigInteger("9")) > 0 ? (minute + "") : ("0" + minute)) + ":"
						+ (second.compareTo(new BigInteger("9")) > 0 ? (second + "") : ("0" + second));
			}
		} else {
			return "00:00:" + (second.compareTo(new BigInteger("9")) > 0 ? (second + "") : ("0" + second));
		}
	}

	/**
	 * 设备生产数据报表导出
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportTaskDetail")
	@ResponseBody
	public ResponseEntity<byte[]> exportTaskDetail(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		BigInteger itemid = null;
		String welderno = "", taskno = "", dtoTime1 = "", dtoTime2 = "";
		if (iutil.isNull(request.getParameter("dtoTime1"))) {
			dtoTime1 = request.getParameter("dtoTime1");
		}
		if (iutil.isNull(request.getParameter("dtoTime2"))) {
			dtoTime2 = request.getParameter("dtoTime2");
		}
		if (iutil.isNull(request.getParameter("welderno"))) {
			welderno = "'%" + request.getParameter("welderno") + "%'";
		}
		if (iutil.isNull(request.getParameter("taskno"))) {
			taskno = "'%" + request.getParameter("taskno") + "%'";
		}
		if (iutil.isNull(request.getParameter("item"))) {
			itemid = new BigInteger(request.getParameter("item"));
		} else {
			itemid = im.getUserInsframework();
		}
		WeldDto dto = new WeldDto();
		String dtime = "统计日期：" + dtoTime1 + "--" + dtoTime2;
		try {
			List<DataStatistics> list = dss.getTask(itemid, welderno, taskno, dtoTime1, dtoTime2);

			List<DataStatistics> task = dss.getTaskDetail(itemid, welderno, taskno, dtoTime1, dtoTime2);
			String[] titles = new String[] { "焊工班组", "焊工编号", "焊工姓名", "焊机编号", "任务编号", "开始时间", "结束时间", "使用通道", "良好段",
					"报警段", "平均焊接电流", "平均焊接电压", "规范符合率(%)" };
			Object[][] data = new Object[list.size()][13];
			int ii = 0;
			for (int i = 0; i < list.size(); i++) {
				if (i != list.size() - 1) {
					if (list.get(i).getTaskid().equals(list.get(i + 1).getTaskid())) {
						if (list.get(i).getType() != 1) {
							if (list.get(i + 1).getType() == 1) {
								list.get(i).setEndtime(list.get(i + 1).getEndtime());
							} else {
								list.get(i).setEndtime(list.get(i + 1).getStarttime());
							}
						}
					}
				}
				for (int j = 0; j < task.size(); j++) {
					if (list.get(i).getType() != 1) {
						if (list.get(i).getTaskid().equals(task.get(j).getTaskid())
								&& list.get(i).getWelderid().equals(task.get(j).getWelderid())
								&& list.get(i).getMachineid().equals(task.get(j).getMachineid())) {
							data[ii][0] = task.get(j).getName();
							data[ii][1] = task.get(j).getWelderno();
							data[ii][2] = task.get(j).getWeldername();
							data[ii][3] = task.get(j).getMachineno();
							data[ii][4] = task.get(j).getTaskno();
							data[ii][5] = task.get(j).getStarttime();
							data[ii][6] = list.get(i).getEndtime();
							data[ii][7] = task.get(j).getChannel();
							if (task.get(j).getWorktime() != null && !"".equals(task.get(j).getWorktime())) {
								data[ii][8] = getTimeStrBySecond(task.get(j).getWorktime());
							} else {
								data[ii][8] = "00:00:00";
							}
							if (task.get(j).getWarntime() != null && !"".equals(task.get(j).getWarntime())) {
								data[ii][9] = getTimeStrBySecond(task.get(j).getWarntime());
							} else {
								data[ii][9] = "00:00:00";
							}
							data[ii][10] = (double) Math.round(task.get(j).getElectricity() * 100) / 100;
							data[ii][11] = (double) Math.round(task.get(j).getVoltage() * 100) / 100;
							double ratio = 0;
							if (task.get(j).getWarntime() != null && !"".equals(task.get(j).getWarntime())) {
								ratio = (double) Math.round(task.get(j).getWorktime().doubleValue()
										/ (task.get(j).getWorktime().doubleValue()
												+ task.get(j).getWarntime().doubleValue())
										* 10000) / 100;
							}
							data[ii][12] = ratio;// 规范符合率
						}
					}
				}
				ii++;
			}
			filename = "生产任务详情" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "生产任务详情");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}
	
	/**
	 * 故障报表导出
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportFauit")
	@ResponseBody
	public ResponseEntity<byte[]> exportFauit(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期：" + time1 + "--" + time2;
		try {
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			int fauit = 0;
			if (iutil.isNull(request.getParameter("fauit"))) {
				fauit = Integer.parseInt(request.getParameter("fauit"));
			}
			List<DataStatistics> list = dss.getFauit(dto, fauit);
			String[] titles = new String[] { "焊机编号", "焊机归属", "故障类型", "故障次数" };
			Object[][] data = new Object[list.size()][4];
			int ii = 0;
			for (DataStatistics i : list) {
				if (ii < list.size()) {
					data[ii][0] = i.getName();// 焊机编号
					data[ii][1] = i.getInsname();// 焊机归属
					data[ii][2] = i.getValuename();// 故障类型
					data[ii][3] = i.getNum();// 故障次数
				}
				ii++;
			}
			filename = "故障报表" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "故障报表");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 导出采集模块
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exporGather")
	@ResponseBody
	public ResponseEntity<byte[]> exporGather(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		try {
			String str = (String) request.getSession().getAttribute("searchStr");
			String parentid = request.getParameter("parent");
			BigInteger parent = null;
			if (iutil.isNull(parentid)) {
				parent = new BigInteger(parentid);
			}
			List<Gather> list = gs.getGatherAll(str, parent);
			String dtime = null;
			String[] titles = new String[] { "采集模块编号", "所属项目", "采集模块状态", "采集模块通讯协议", "采集模块IP地址", "采集模块MAC地址",
					"采集模块出厂时间" };
			Object[][] data = new Object[list.size()][7];
			for (int i = 0; i < list.size(); i++) {
				data[i][0] = list.get(i).getGatherNo();
				data[i][1] = list.get(i).getItemname();
				data[i][2] = list.get(i).getStatus();
				data[i][3] = list.get(i).getProtocol();
				data[i][4] = list.get(i).getIpurl();
				data[i][5] = list.get(i).getMacurl();
				data[i][6] = list.get(i).getLeavetime();
			}
			filename = "采集模块" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "采集模块数据");

			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";

			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 导出焊工
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exporWelder")
	@ResponseBody
	public ResponseEntity<byte[]> exporWelder(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		try {
			String str = (String) request.getSession().getAttribute("searchStr");
			String parentid = request.getParameter("parent");
			BigInteger parent = null;
			if (iutil.isNull(parentid)) {
				parent = new BigInteger(parentid);
			}
			List<Person> list = ps.findAll(parent, str);
			String dtime = null;
			String[] titles = new String[] { "姓名", "编号", "手机", "级别", "卡号", "资质", "部门", "备注" };
			Object[][] data = new Object[list.size()][8];
			for (int i = 0; i < list.size(); i++) {
				data[i][0] = list.get(i).getName();
				data[i][1] = list.get(i).getWelderno();
				data[i][2] = list.get(i).getCellphone();
				data[i][3] = list.get(i).getValuename();
				data[i][4] = list.get(i).getCardnum();
				data[i][5] = list.get(i).getValuenamex();
				data[i][6] = list.get(i).getInsname();
				data[i][7] = list.get(i).getBack();
			}
			filename = "焊工管理" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "焊工管理数据");

			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";

			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 导出派工任务
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exporWeldTask")
	@ResponseBody
	public ResponseEntity<byte[]> exporWeldTask(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		try {
			String serach = "";
			MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			int instype = im.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
			BigInteger userinsid = im.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
			int bz = 0;
			if (instype == 20) {
			} else if (instype == 23) {
				serach = "j.fitemId=" + userinsid;
			} else {
				List<Insframework> ls = im.getInsIdByParent(userinsid, 24);
				for (Insframework inns : ls) {
					if (bz == 0) {
						serach = serach + "(j.fitemId=" + inns.getId();
					} else {
						serach = serach + " or j.fitemId=" + inns.getId();
					}
					bz++;
				}
				serach = serach + " or j.fitemId=" + userinsid + ")";
			}
			if (request.getParameter("searchStr") != null && serach != null && serach != "") {
				serach = serach + " and " + request.getParameter("searchStr");
			}
			if (request.getParameter("searchStr") != null && (serach == null || serach == "")) {
				serach = serach + request.getParameter("searchStr");
			}
			List<WeldedJunction> list = wjm.getWeldedJunctionAll(serach);
			String dtime = null;
			String[] titles = new String[] { "任务编号", "计划开始时间", "工程符号", "焊接方法", "焊接位置", "母材型号", "焊材型号", "分配焊工", "工艺设计",
					"预热要求", "道间温度", "碳刨要求", "后热要求", "退火焊道", "装配间隙", "碳刨深度", "碳刨宽度", "后热温度", "后热时间", "工艺库名称" };
			Object[][] data = new Object[list.size()][20];
			for (int i = 0; i < list.size(); i++) {
				data[i][0] = list.get(i).getWeldedJunctionno();
				data[i][1] = list.get(i).getStartTime();
				data[i][2] = list.get(i).getFengineering_symbol();
				data[i][3] = list.get(i).getFweld_method();
				data[i][4] = list.get(i).getFweld_position();
				data[i][5] = list.get(i).getFbase_material_type();
				data[i][6] = list.get(i).getFweld_material_model();
				data[i][7] = list.get(i).getFwelder_name();
				data[i][8] = list.get(i).getFtechnological_design();
				data[i][9] = list.get(i).getFwarm_up_requirement();
				data[i][10] = list.get(i).getFinter_channel_temperature();
				data[i][11] = list.get(i).getFcarbon_requirement();
				data[i][12] = list.get(i).getFpost_heat_requirement();
				data[i][13] = list.get(i).getFannealed_weld();
				data[i][14] = list.get(i).getFassembly_clearance();
				data[i][15] = list.get(i).getFcarbon_depth();
				data[i][16] = list.get(i).getFcarbon_width();
				data[i][17] = list.get(i).getFpost_heat_temperature();
				data[i][18] = list.get(i).getFafter_hot_time();
				data[i][19] = list.get(i).getFwps_lib_name();
			}
			filename = "派工任务管理" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "派工任务管理数据");

			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";

			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 设备任务表导出
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportMachineTask")
	@ResponseBody
	public ResponseEntity<byte[]> exportMachineTask(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String item = request.getParameter("item");
		int status = Integer.parseInt(request.getParameter("status"));
		WeldDto dto = new WeldDto();
		BigInteger itemid = null;
		String dtime = "统计日期：" + time1 + "--" + time2;
		try {
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			if (iutil.isNull(item)) {
				itemid = new BigInteger(item);
			}
			List<DataStatistics> list = dss.getMachineTask(itemid, dss.getDay(time1, time2), status);
			String[] titles = new String[] { "所属班组", "设备编号", "日期", "任务号", "状态" };
			Object[][] data = new Object[list.size()][9];
			int ii = 0;
			for (DataStatistics i : list) {
				if (ii < list.size()) {
					data[ii][0] = i.getInsname();
					data[ii][1] = i.getMachineno();
					data[ii][2] = i.getTime();
					data[ii][3] = i.getTaskno();
					data[ii][4] = i.getType() == 1 ? "未分配" : "已分配";
				}
				ii++;
			}
			filename = "焊机任务表" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "设备任务表");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}

	/**
	 * 实时数据导出
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportLiveData")
	@ResponseBody
	public ResponseEntity<byte[]> exportLiveData(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String solder_layer = request.getParameter("solder_layer");
		String weld_bead = request.getParameter("weld_bead");
		String taskno = request.getParameter("taskno");
		String welderid = request.getParameter("welderid");
		BigInteger mach = new BigInteger(request.getParameter("mach"));
		String board = request.getParameter("board");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期：" + time1 + "--" + time2 + "\r\n";
		double avgEle = 0, avgVol = 0;
		try {
			if (iutil.isNull(time1)) {
				dto.setDtoTime1(time1);
			}
			if (iutil.isNull(time2)) {
				dto.setDtoTime2(time2);
			}
			List<Report> list = reportService.historyData(dto, taskno, mach, welderid, solder_layer, weld_bead);
//			dtime += "平均电流：" + String.valueOf(list.get(0).getFrealele()) + "；平均电压：" + String.valueOf(list.get(0).getFrealvol());
			String[] titles = new String[] { "焊层号", "焊道号", "电流", "电压", "时间" };
			Object[][] data = new Object[list.size()][5];
			int ii = 0;
			double tempMaxEle = 0, tempMinEle = 0, tempMaxVol = 0, tempMinVol = 0;
			for (Report i : list) {
				if (ii < list.size()) {
					data[ii][0] = i.getId();// 焊层号
					data[ii][1] = i.getInsid();// 焊道号
					data[ii][2] = i.getFstandardele();// 电流
					data[ii][3] = i.getFstandardvol();// 电压
					data[ii][4] = i.getFweldingtime();// 时间
					avgEle += i.getFstandardele();
					avgVol += i.getFstandardvol();
					if (ii == 0) {
						tempMinEle = i.getFstandardele();
						tempMinVol = i.getFstandardvol();
					}
					if (i.getFstandardele() > tempMaxEle) {
						tempMaxEle = i.getFstandardele();
					}
					if (i.getFstandardele() < tempMinEle && i.getFstandardele() != 0) {
						tempMinEle = i.getFstandardele();
					}
					if (i.getFstandardvol() > tempMaxVol) {
						tempMaxVol = i.getFstandardvol();
					}
					if (i.getFstandardvol() < tempMinVol && i.getFstandardvol() != 0) {
						tempMinVol = i.getFstandardvol();
					}
				}
				ii++;
			}
			DecimalFormat df = new DecimalFormat("0.0");
			if (board == null || "".equals(board)) {
				board = "0.0";
			}
			String weldSpeed = df.format(Double.valueOf(board) / (list.size() / 60.0));
			String lineEnergy = "0.0";
			if (!weldSpeed.equals("0.0")) {
				lineEnergy = df.format(
						(avgEle / list.size()) * (avgVol / list.size()) / Double.valueOf(weldSpeed) * 60 / 1000);
			}
			if (list.size() != 0) {
				dtime += "任务编号：" + taskno + "；平均电流：" + df.format(avgEle / list.size()) + "A；平均电压："
						+ df.format(avgVol / list.size()) + "V" + "；平均焊接速度：" + weldSpeed + "cm/min；平均线能量：" + lineEnergy
						+ "KJ/cm\r\n" + "最大电流：" + tempMaxEle + "A；最小电流：" + tempMinEle + "A" + "；最大电压：" + tempMaxVol
						+ "V；最小电压：" + tempMinVol + "V";
			} else {
				dtime += "任务编号：" + taskno + "；平均电流：0A；平均电压：0V；最大电流：0V；最小电流：0V；最大电压：0V；最小电压：0V";
			}
			filename = "历史数据" + sdf.format(new Date()) + ".xls";

			ServletContext scontext = request.getSession().getServletContext();
			// 获取绝对路径
			String abpath = scontext.getRealPath("");
			// String contextpath=scontext. getContextPath() ; 获取虚拟路径

			String path = abpath + "excelfiles/" + filename;

			new CommonExcelUtil(dtime, titles, data, path, "历史焊接数据详情");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"), "iso-8859-1");

			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			// 处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName);
			ServletOutputStream o = response.getOutputStream();
			o.flush();

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}
}
