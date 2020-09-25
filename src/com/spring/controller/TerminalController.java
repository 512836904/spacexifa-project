package com.spring.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.neethi.util.Service;
import org.aspectj.weaver.ast.Call;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spring.model.Person;
import com.spring.model.WeldedJunction;
import com.spring.model.WeldingMachine;
import com.spring.model.Wps;
import com.spring.page.Page;
import com.spring.service.PersonService;
import com.spring.service.UserService;
import com.spring.service.WeldedJunctionService;
import com.spring.service.WeldingMachineService;
import com.spring.service.WpsService;

import net.sf.json.JSONArray;
import java.text.DecimalFormat; 


@Controller
@RequestMapping(value = "/terminal",produces = { "text/json;charset=UTF-8" })
public class TerminalController {
	
	private Page page;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int total = 0;
	
	@Autowired
	private UserService userService;
	@Autowired
	private WeldedJunctionService wjm;
	@Autowired
	private PersonService welderService;
	@Autowired
	private WeldedJunctionService wjs;
	@Autowired
	private WpsService wpsService;
	@Autowired
	private WeldingMachineService wmm;
	/**
	 * 验证登陆
	 * @param request
	 */
	@RequestMapping("/login")
	@ResponseBody
	public void login(HttpServletRequest request,HttpServletResponse response){
		JSONObject serrespon = new JSONObject();
		JSONObject data = new JSONObject();
		
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		String username = JSON.parseObject(jsondata.getString("data")).getString("username");
		String password = JSON.parseObject(jsondata.getString("data")).getString("password");
		
        try {
			MessageDigest md5 = MessageDigest.getInstance("md5");
			md5.update(password.getBytes());
			String passwordmd5 = new BigInteger(1, md5.digest()).toString(16);
			
			com.spring.model.User user = userService.LoadUser(username);
			
			if(null == user){
				serrespon.put("type", "loginrespond");
				serrespon.put("respondtype", "default");
				data.put("datalength", "0");
				serrespon.put("data", data);
			}else if(null != user && passwordmd5.equals(user.getUserPassword())){
				serrespon.put("type", "loginrespond");
				serrespon.put("respondtype", "succeed");
				data.put("datalength", "2");
				data.put("name", user.getUserName());
				data.put("userid", user.getId());
				serrespon.put("data", data);
			}else{
				serrespon.put("type", "loginrespond");
				serrespon.put("respondtype", "default");
				data.put("datalength", "0");
				serrespon.put("data", data);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
        String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	/**
	 * 扫描跟踪卡获取任务信息
	 * @param request
	 * @return
	 */
	
	@RequestMapping("/scan")
	public void scan(HttpServletRequest request,HttpServletResponse response){
		JSONObject serrespon = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		
		String cardnumber = request.getParameter("cardnumber");
		String search = "";
		search = " AND p.FCARD_ID="+cardnumber;
		List<WeldedJunction> productList = wjm.getProductList(search);
		for(WeldedJunction wjm:productList){
			json.put("fid", wjm.getId());
			if("".equals(wjm.getFsuffix_number())||wjm.getFsuffix_number()==null) {
				json.put("fproduct_number", wjm.getFprefix_number()+wjm.getFproduct_number());
			}else{
				json.put("fproduct_number", wjm.getFprefix_number()+wjm.getFproduct_number()+wjm.getFsuffix_number());
			}
			json.put("fwps_lib_name", wjm.getFwps_lib_name());
			json.put("fwps_lib_version", wjm.getFwps_lib_version());
			json.put("fwps_lib_id", wjm.getFwpslib_id());
			json.put("fproduct_drawing_no", wjm.getFproduct_drawing_no());
			json.put("fproduct_name", wjm.getFproduct_name());
			json.put("fproduct_version", wjm.getFproduct_version());
			json.put("fstatus", wjm.getFstatus());
			ary.add(json);
		}
		
		serrespon.put("rows", ary);

        String respondata = JSON.toJSONString(serrespon);
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 开始任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/starttask")
	public void starttask(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		try {
			String flag = request.getParameter("flag");
			String cardId = jsondata.getString("cardId");
			String machId = jsondata.getString("machId");
			String welderId = jsondata.getString("welderId");
			String wpsId = jsondata.getString("wpsId");
			String productId = jsondata.getString("productId");
			String employeeId = jsondata.getString("employeeId");
			String stepId = jsondata.getString("stepId");
			String junctionId = jsondata.getString("junctionId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_name(productId);
			wps.setFemployee_id(employeeId);
			wps.setFstatus(Integer.parseInt(flag));
			wps.setFjunction(junctionId);
			wps.setFstep_id(stepId);
			if("1".equals(flag)){     //最小单位为焊缝
				//String stepId = jsondata.getString("stepId");
				wps.setFstep_id(stepId);
			}else if("2".equals(flag)){     //最小单位为工步
				//String junctionId = jsondata.getString("junctionId");
				//String stepId = jsondata.getString("stepId");
				wps.setFjunction(junctionId);
				wps.setFstep_id(stepId);
			}
			wpsService.addTaskresultRow(wps);
			serrespon.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serrespon.put("success", false);
		}
		String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 下一工步任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/nextstep")
	public void nextstep(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("oldJson"));
		JSONObject newJsondata = JSON.parseObject(request.getParameter("newJson"));
		try {
			String flag = request.getParameter("flag");
			String cardId = jsondata.getString("cardId");
			String machId = jsondata.getString("machId");
			String welderId = jsondata.getString("welderId");
			String wpsId = jsondata.getString("wpsId");
			String productId = jsondata.getString("productId");
			String employeeId = jsondata.getString("employeeId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_name(productId);
			wps.setFemployee_id(employeeId);
			wps.setFstatus(Integer.parseInt(flag));
			if("0".equals(flag)){     //最小单位为焊缝
				String stepId = jsondata.getString("stepId");
				wps.setFstep_id(stepId);
			}else if("1".equals(flag)){     //最小单位为工步
				String junctionId = jsondata.getString("junctionId");
				String stepId = jsondata.getString("stepId");
				wps.setFjunction(junctionId);
				wps.setFstep_id(stepId);
			}
			//wpsService.updateTaskresult(wps);
			cardId = newJsondata.getString("cardId");
			machId = newJsondata.getString("machId");
			welderId = newJsondata.getString("welderId");
			wpsId = newJsondata.getString("wpsId");
			productId = newJsondata.getString("productId");
			employeeId = newJsondata.getString("employeeId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_name(productId);
			wps.setFemployee_id(employeeId);
			wps.setFstatus(Integer.parseInt(flag));
			if("0".equals(flag)){     //最小单位为焊缝
				String stepId = jsondata.getString("stepId");
				wps.setFstep_id(stepId);
			}else if("1".equals(flag)){     //最小单位为工步
				String junctionId = newJsondata.getString("junctionId");
				String stepId = newJsondata.getString("stepId");
				wps.setFjunction(junctionId);
				wps.setFstep_id(stepId);
			}
			wpsService.addTaskresultRow(wps);
			serrespon.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serrespon.put("success", false);
		}
		String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 结束任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/over")
	public void over(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject obj = new JSONObject();
		String flag = request.getParameter("flag");
		String cardId = jsondata.getString("cardId");
		String machId = jsondata.getString("machId");
		String welderId = jsondata.getString("welderId");
		String wpsId = jsondata.getString("wpsId");
		String productId = jsondata.getString("productId");
		String employeeId = jsondata.getString("employeeId");
		String stepId = jsondata.getString("stepId");
		String junctionId = jsondata.getString("junctionId");
		String search = "",search1 = "",search2 = "",fstarttime ="",fendtime = "";
		double ele = 0.0,vol = 0.0,gasflow = 0.0;
		 DecimalFormat df = new DecimalFormat("0.00");
		try {
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_name(productId);
			wps.setFemployee_id(employeeId);
			wps.setFstatus(Integer.parseInt(flag));
			wps.setFstep_id(stepId);
			wps.setFjunction(junctionId);
			if("1".equals(flag)){     //最小单位为焊缝
				//String stepId = jsondata.getString("stepId");
				//wps.setFstep_id(stepId);
			}else if("2".equals(flag)){     //最小单位为工步
				//String junctionId = jsondata.getString("junctionId");
				//String stepId = jsondata.getString("stepId");
				//wps.setFjunction(junctionId);
				//wps.setFstep_id(stepId);
			}
			wpsService.updateTaskresult(wps);
			//wpsService.overTaskresult(wps);
			serrespon.put("success", true);
			if(cardId != ""){
				if(search == ""){
					search += "  t.FCARD_ID ='" + cardId + "'";
				}else{
					search += " AND t.FCARD_ID = '" + cardId + "'";
				}
				if(search1 == ""){
					search1 += " t.FCARD_ID ='"+cardId+"'";
				}else{
					search1 += "  AND t.FCARD_ID = '" + cardId + "'";
				}
				if(search2 == ""){
					search2 += "  l.FCARD_ID ='" + cardId + "'";
				}else{
					search2 += " AND l.FCARD_ID = '" + cardId + "'";
				}
			}
			if(productId != ""){
				if(search == ""){
					search += " t.FPRODUCT_NUMBER_ID = '" + productId + "'";
				}else{
					search += " AND t.FPRODUCT_NUMBER_ID = '" + productId + "'";
				}
				if(search1 == ""){
					search1 += " t.FTASKID ='" + productId + "'";
				}else{
					search1 += " AND t.FTASKID = '" + productId + "'";
				}
				if(search2 == ""){
					search2 += " l.FPRODUCT_NUMBER_ID ='" + productId + "'";
				}else{
					search2 += " AND l.FPRODUCT_NUMBER_ID = '" + productId + "'";
				}
			}
			if(employeeId != ""){
				if(search == ""){
					search += " t.FEMPLOYEE_ID = '" + employeeId + "'";
				}else{
					search += " AND t.FEMPLOYEE_ID = '" + employeeId + "'";
				}
				if(search1 == ""){
					search1 += " t.FEMPLOYEE_ID = '" + employeeId + "'";
				}else{
					search1 += " AND t.FEMPLOYEE_ID = '" + employeeId + "'";
				}
				if(search2 == ""){
					search2 += " l.FEMPLOYEE_ID = '" + employeeId + "'";
				}else{
					search2 += " AND l.FEMPLOYEE_ID = '" + employeeId + "'";
				}
				
			}
			if(stepId != ""){
				if(search == ""){
					search += " t.FSTEP_ID = '" + stepId + "'";
				}else{
					search += " AND t.FSTEP_ID = '" + stepId + "'";
				}
				if(search1 == ""){
					search1 += " t.FSTEP_ID = '" + stepId + "'";
				}else{
					search1 += " AND t.FSTEP_ID = '" + stepId + "'";
				}
				if(search2 == ""){
					search2 += " l.FSTEP_ID = '" + stepId + "'";
				}else{
					search2 += " AND l.FSTEP_ID = '" + stepId + "'";
				}
			}
			if(junctionId != ""){
				if(search == ""){
					search += " t.FJUNCTION_ID = '" + junctionId + "'";
				}else{
					search += " AND t.FJUNCTION_ID = '" + junctionId + "'";
				}
				if(search1 == ""){
					search1 += " t.FJUNCTION_ID = '" + junctionId + "'";
				}else{
					search1 += " AND t.FJUNCTION_ID = '" + junctionId + "'";
				}
				if(search2 == ""){
					search2 += " l.FJUNCTION_ID = '" + junctionId + "'";
				}else{
					search2 += " AND l.FJUNCTION_ID = '" + junctionId + "'";
				}
			}
			List<Wps> weldtime = wpsService.getWeldTime(search1);
			try {
				fstarttime = weldtime.get(0).getFstarttime().substring(0,19);
				fendtime = weldtime.get(0).getEndtime().substring(0,19);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			if(fstarttime != ""){
				if(search == ""){
					search += " t.FWELDTIME >to_date('" +fstarttime+"', 'yyyy-mm-dd hh24:mi:ss')";
				}else{
					search += " AND t.FWELDTIME >to_date('"+fstarttime+"', 'yyyy-mm-dd hh24:mi:ss')";
				}
				if(search2 == ""){
					search2 += " l.FWELDTIME >to_date('" +fstarttime+"', 'yyyy-mm-dd hh24:mi:ss')";
				}else{
					search2 += " AND l.FWELDTIME >to_date('"+fstarttime+"', 'yyyy-mm-dd hh24:mi:ss')";
				}
			}
			if(fendtime != ""){
				if(search == ""){
					search += " t.FWELDTIME < to_date('"+fendtime+"', 'yyyy-mm-dd hh24:mi:ss')";
				}else{
					search += " AND t.FWELDTIME <to_date('"+fendtime+"', 'yyyy-mm-dd hh24:mi:ss')";
				}
				if(search2 == ""){
					search2 += " l.FWELDTIME < to_date('"+fendtime+"', 'yyyy-mm-dd hh24:mi:ss')";
				}else{
					search2 += " AND l.FWELDTIME <to_date('"+fendtime+"', 'yyyy-mm-dd hh24:mi:ss')";
				}
			}
			List<Wps> list = wpsService.getTaskParameter(search,search2);
			try {
				for(Wps w:list) {
					json.put("ele", new DecimalFormat("0.00").format(w.getFweld_ele()));
					json.put("vol", new DecimalFormat("0.00").format(w.getFweld_vol()));
					json.put("gasflow", new DecimalFormat("0.00").format(w.getFadvance()));
					ary.add(json);
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serrespon.put("success", false);
		}
		obj.put("ary", ary);
		String respondata = JSON.toJSONString(obj);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 任务数据上传
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload")
	public void upload(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		try {
			String cardId = jsondata.getString("cardId");
			String machId = jsondata.getString("machId");
			String welderId = jsondata.getString("welderId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wpsService.overCard(wps);
			serrespon.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serrespon.put("success", false);
		}
		String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Description 获取所有电子跟踪卡
	 * @author Bruce
	 * @date 2020年6月23日下午7:19:13
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getAllCards")
	public void getAllCards(HttpServletRequest request,HttpServletResponse response){
		String search = request.getParameter("search");
		search = "(t.fid IN (SELECT MAX(fid) FROM tb_taskresult GROUP BY FCARD_ID) OR t.foperatetype IS null) ";
		List<WeldedJunction> cardList = wjm.getCardList(search);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction wjm:cardList){
				
				json.put("fid", wjm.getId());
				json.put("fwelded_junction_no", wjm.getWeldedJunctionno());
				json.put("type", wjm.getRoomNo());
//				if(wjm.getMaterial()!=null&&("").equals(wjm.getMaterial())) {
					json.put("fstatus", wjm.getMaterial());
//				}
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		String respondata = JSON.toJSONString(obj);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 参数保存
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveprameter")
	public void saveprameter(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		String search1 = "",fstarttime ="00:00:00",fendtime = "00:00:00";
		try {
			String cardId = jsondata.getString("cardId");
			String machId = jsondata.getString("machId");
			String welderId = jsondata.getString("welderId");
			String wpsId = jsondata.getString("wpsId");
			String productId = jsondata.getString("productId");
			String employeeId = jsondata.getString("employeeId");
			String stepId = jsondata.getString("stepId");
			String junctionId = jsondata.getString("junctionId");
			String weldradio = jsondata.getString("weldradio");
			String weldmaterial = jsondata.getString("weldmaterial");
			if(cardId != ""){if(search1 == ""){
					search1 += " t.FCARD_ID = '" + cardId + "'";
				}else{
					search1 += " AND t.FCARD_ID = '" + cardId + "'";
				}
			}
			if(productId != ""){
				if(search1 == ""){
					search1 += " t.FTASKID ='" + productId + "'";
				}else{
					search1 += " AND t.FTASKID = '" + productId + "'";
				}
			}
			if(employeeId != ""){if(search1 == ""){
					search1 += " t.FEMPLOYEE_ID = '" + employeeId + "'";
				}else{
					search1 += " AND t.FEMPLOYEE_ID = '" + employeeId + "'";
				}
			}
			if(stepId != ""){if(search1 == ""){
					search1 += " t.FSTEP_ID = '" + stepId + "'";
				}else{
					search1 += " AND t.FSTEP_ID = '" + stepId + "'";
				}
			}
			if(junctionId != ""){if(search1 == ""){
					search1 += " t.FJUNCTION_ID = '" + junctionId + "'";
				}else{
					search1 += " AND t.FJUNCTION_ID = '" + junctionId + "'";
				}
			}
			List<Wps> weldtime = wpsService.getWeldTime(search1);
			try {
				fstarttime = weldtime.get(0).getFstarttime().substring(0,19);
				fendtime = weldtime.get(0).getEndtime().substring(0,19);
			}catch (Exception e) {
				// TODO: handle exception
			}
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFstarttime(fstarttime);
			wps.setEndtime(fendtime);
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_number(productId);
			wps.setFjunction(junctionId);
			wps.setFemployee_id(employeeId);
			wps.setFstep_id(stepId);
			wps.setF001(weldradio);
			wps.setF002(weldmaterial);
			wpsService.addWeldpramatia(wps);
			serrespon.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serrespon.put("success", false);
		}
		String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 任务判断避免领取同一个任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/taskstatus")
	public void taskstatus(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		String search1 = "";
		String cardId = jsondata.getString("cardId");
		String machId = jsondata.getString("machId");
		String welderId = jsondata.getString("welderId");
		String wpsId = jsondata.getString("wpsId");
		String productId = jsondata.getString("productId");
		String employeeId = jsondata.getString("employeeId");
		String stepId = jsondata.getString("stepId");
		String junctionId = jsondata.getString("junctionId");
		String weldradio = jsondata.getString("weldradio");
		String weldmaterial = jsondata.getString("weldmaterial");
			if(cardId != "")
			{if(search1 == ""){
					search1 += " t.FCARD_ID = '" + cardId + "'";
				}else{
					search1 += " AND t.FCARD_ID = '" + cardId + "'";
				}
			}
			if(productId != ""){
				if(search1 == ""){
					search1 += " t.FTASKID ='" + productId + "'";
				}else{
					search1 += " AND t.FTASKID = '" + productId + "'";
				}
			}
			if(employeeId != ""){if(search1 == ""){
					search1 += " t.FEMPLOYEE_ID = '" + employeeId + "'";
				}else{
					search1 += " AND t.FEMPLOYEE_ID = '" + employeeId + "'";
				}
			}
			if(stepId != ""){if(search1 == ""){
					search1 += " t.FSTEP_ID = '" + stepId + "'";
				}else{
					search1 += " AND t.FSTEP_ID = '" + stepId + "'";
				}
			}
			if(junctionId != ""){if(search1 == ""){
					search1 += " t.FJUNCTION_ID = '" + junctionId + "'";
				}else{
					search1 += " AND t.FJUNCTION_ID = '" + junctionId + "'";
				}
			}
			List<Wps> weldtime = wpsService.getTaskstatus(search1);
			try{
				for(Wps w:weldtime){
					json.put("count", w.getInsid());
					ary.add(json);
				}
			}catch(Exception e){
				e.getMessage();
			}
		obj.put("rows", ary);
		String respondata = JSON.toJSONString(obj);
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取所有焊工
	 * @Description
	 * @author Bruce
	 * @date 2020年7月2日上午9:49:46
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getAllWelders")
	public void getAllWelders(HttpServletRequest request,HttpServletResponse response){
		List<Person> findAll = welderService.getWelder();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Person welder:findAll){
				json.put("id", welder.getId());
				json.put("name", welder.getName());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		String respondata = JSON.toJSONString(obj);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取所有焊机
	 * @Description
	 * @author Bruce
	 * @date 2020年7月2日上午9:49:46
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getAllMachine")
	public void getAllMachine(HttpServletRequest request,HttpServletResponse response){
		List<WeldingMachine> list = wmm.getWeldingMachine(null);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldingMachine wm:list){
				json.put("id", wm.getId());
				json.put("equipmentNo", wm.getEquipmentNo());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		String respondata = JSON.toJSONString(obj);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 手持终端任务上传
	 * @Description
	 * @author chen
	 * @date 2020年8月21日下午7:10:17
	 * @param request 任务上传
	 * @param response 
	 */
	@RequestMapping("/TaskUpload")
	public void TaskUpload(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		String search = "";
		String search1 = "",search2 = "",fstarttime ="",fendtime = "";
		String ele = "0.0",vol = "0.0",gasflow = "0.0",airflow = "0.0",wireele = "0.0",laserpower = "0.0",wirerate = "0.0",weldrate = "0.0";
		DecimalFormat df = new DecimalFormat("0.00");
		String Files = "";
		String condition = "";
		try {
			//String flag = request.getParameter("flag");
			String cardId = jsondata.getString("cardId");
			String machId = jsondata.getString("machId");
			String welderId = jsondata.getString("welderId");
			String wpsId = jsondata.getString("wpsId");
			String productId = jsondata.getString("productId");
			String employeeId = jsondata.getString("employeeId");
			String stepId = jsondata.getString("stepId");
			String junctionId = jsondata.getString("junctionId");
			if(cardId != ""){
				if(search == ""){
					search += " t.FCARD_ID ='"+cardId+"'";
				}else{
					search += " AND t.FCARD_ID = '" + cardId + "'";
				}
				if(search1 == ""){
					search1 += " t.FCARD_ID ='"+cardId+"'";
				}else{
					search1 += "  AND t.FCARD_ID = '" + cardId + "'";
				}
				if(search2 == ""){
					search2 += " l.FCARD_ID ='"+cardId+"'";
				}else{
					search2 += " AND l.FCARD_ID = '" + cardId + "'";
				}
				if(condition == ""){
					condition += " w.FID = '" + cardId + "'";
				}else{
					condition += " AND w.FID = '" + cardId + "'";
				}
			}
			if(productId != ""){
				if(search == ""){
					search += " t.FPRODUCT_NUMBER_ID ='" + productId + "'";
				}else{
					search += " AND t.FPRODUCT_NUMBER_ID = '" + productId + "'";
				}
				if(search1 == ""){
					search1 += " t.FTASKID ='" + productId + "'";
				}else{
					search1 += " AND t.FTASKID = '" + productId + "'";
				}
				if(search2 == ""){
					search2 += " l.FPRODUCT_NUMBER_ID ='" + productId + "'";
				}else{
					search2 += " AND l.FPRODUCT_NUMBER_ID = '" + productId + "'";
				}
			}
			if(employeeId != ""){
				if(search == ""){
					search += " t.FEMPLOYEE_ID = '" + employeeId + "'";
				}else{
					search += " AND t.FEMPLOYEE_ID = '" + employeeId + "'";
				}
				if(search1 == ""){
					search1 += " t.FEMPLOYEE_ID = '" + employeeId + "'";
				}else{
					search1 += " AND t.FEMPLOYEE_ID = '" + employeeId + "'";
				}
				if(search2 == ""){
					search2 += " l.FEMPLOYEE_ID = '" + employeeId + "'";
				}else{
					search2 += " AND l.FEMPLOYEE_ID = '" + employeeId + "'";
				}
			}
			if(stepId != ""){
				if(search == ""){
					search += " t.FSTEP_ID = '" + stepId + "'";
				}else{
					search += " AND t.FSTEP_ID = '" + stepId + "'";
				}
				if(search1 == ""){
					search1 += " t.FSTEP_ID = '" + stepId + "'";
				}else{
					search1 += " AND t.FSTEP_ID = '" + stepId + "'";
				}
				if(condition == ""){
					condition += " S.FSTEP_ID = '" + stepId + "'";
				}else{
					condition += " AND S.FSTEP_ID = '" + stepId + "'";
				}
				if(search2 == ""){
					search2 += " l.FSTEP_ID = '" + stepId + "'";
				}else{
					search2 += " AND l.FSTEP_ID = '" + stepId + "'";
				}
			}
			if(junctionId != ""){
				if(search == ""){
					search += " t.FJUNCTION_ID = '" + junctionId + "'";
				}else{
					search += " AND t.FJUNCTION_ID= '" + junctionId + "'";
				}
				if(search1 == ""){
					search1 += " t.FJUNCTION_ID = '" + junctionId + "'";
				}else{
					search1 += " AND t.FJUNCTION_ID = '" + junctionId + "'";
				}
				if(search2 == ""){
					search2 += " l.FJUNCTION_ID = '" + junctionId + "'";
				}else{
					search2 += " AND l.FJUNCTION_ID= '" + junctionId + "'";
				}
			}
			
		List<Wps> Filex = wpsService.findStepFile(condition);//获取文件名
		Files = Filex.get(0).getFmode();
			//获取任务开始时间和结束时间
		List<Wps> weldtime = wpsService.getWeldTime(search1);
		try {
			fstarttime = weldtime.get(0).getFstarttime().substring(0,19);
			fendtime = weldtime.get(0).getEndtime().substring(0,19);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		if(fstarttime != ""){
			if(search == ""){
				search += " t.FWELDTIME >to_date('" +fstarttime+"', 'yyyy-mm-dd hh24:mi:ss')";
			}else{
				search += " AND t.FWELDTIME >to_date('"+fstarttime+"', 'yyyy-mm-dd hh24:mi:ss')";
			}
			if(search2 == ""){
				search2 += " l.FWELDTIME >to_date('" +fstarttime+"', 'yyyy-mm-dd hh24:mi:ss')";
			}else{
				search2 += " AND l.FWELDTIME >to_date('"+fstarttime+"', 'yyyy-mm-dd hh24:mi:ss')";
			}
		}
		if(fendtime != ""){
			if(search == ""){
				search += " t.FWELDTIME < to_date('"+fendtime+"', 'yyyy-mm-dd hh24:mi:ss')";
			}else{
				search += " AND t.FWELDTIME <to_date('"+fendtime+"', 'yyyy-mm-dd hh24:mi:ss')";
			}
			if(search2 == ""){
				search2 += " l.FWELDTIME < to_date('"+fendtime+"', 'yyyy-mm-dd hh24:mi:ss')";
			}else{
				search2 += " AND l.FWELDTIME <to_date('"+fendtime+"', 'yyyy-mm-dd hh24:mi:ss')";
			}
		}
		List<Wps> list = wpsService.getTaskParameter(search,search2);
		try {
				ele = new DecimalFormat("0.00").format(list.get(0).getFweld_ele());
				vol = new DecimalFormat("0.00").format(list.get(0).getFweld_vol());
				gasflow = new DecimalFormat("0.00").format(list.get(0).getFadvance());
				airflow =  new DecimalFormat("0.00").format(list.get(0).getF002());
			    wireele =  new DecimalFormat("0.00").format(list.get(0).getF003());
			    laserpower =  new DecimalFormat("0.00").format(list.get(0).getF004());
			    wirerate =  new DecimalFormat("0.00").format(list.get(0).getF005());
			    if(!"".equals(list.get(0).getF006())) {
			    weldrate =  new DecimalFormat("0.00").format(list.get(0).getF006());
			    }
		}
		catch (Exception e) {
			// TODO: handle exception
		}
			serrespon.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serrespon.put("success", false);
		}
		String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		// TODO Auto-generated method stub
		
        String path = "C:\\WDMS\\apache-tomcat-8.0.53\\webapps\\CardList"+"\\"+Files;
        //String path = "E:\\Xmlfile\\card.xml";
        byte[] buffer = null;  
        try {  
        	File file = new File(path);  
        	SAXReader reader = new SAXReader();
			// 读取xml文件到Document中
			Document doc = null;
			try {
				doc = reader.read(file);
				// 获取xml文件的根节点
				Element rootElement = doc.getRootElement();
				Element tableContent = rootElement.element("tableContent");
				Element starttime = rootElement.element("starttime");
				Element endtime = rootElement.element("endtime");
				starttime.setText(fstarttime);
				endtime.setText(fendtime);
				Element contentvalue = tableContent.addElement("contentvalue");
				Element content = contentvalue.addElement("content");
				content.setText("焊接电流");
				Element value = contentvalue.addElement("value");
				value.setText(String.valueOf(ele));
				contentvalue = tableContent.addElement("contentvalue");
				content = contentvalue.addElement("content");
				content.setText("焊接电压");
				value = contentvalue.addElement("value");
				value.setText(String.valueOf(vol));
				contentvalue = tableContent.addElement("contentvalue");
				content = contentvalue.addElement("content");
				content.setText("保护气流量");
				value = contentvalue.addElement("value");
				value.setText(String.valueOf(gasflow));
				contentvalue = tableContent.addElement("contentvalue");
				content = contentvalue.addElement("content");
				content.setText("离子气流量");
				value = contentvalue.addElement("value");
				value.setText(String.valueOf(airflow));
				contentvalue = tableContent.addElement("contentvalue");
				content = contentvalue.addElement("content");
				content.setText("热丝电流");
				value = contentvalue.addElement("value");
				value.setText(String.valueOf(wireele));
				contentvalue = tableContent.addElement("contentvalue");
				content = contentvalue.addElement("content");
				content.setText("激光功率");
				value = contentvalue.addElement("value");
				value.setText(String.valueOf(laserpower));
				contentvalue = tableContent.addElement("contentvalue");
				content = contentvalue.addElement("content");
				content.setText("送丝速度");
				value = contentvalue.addElement("value");
				value.setText(String.valueOf(wirerate));
				contentvalue = tableContent.addElement("contentvalue");
				content = contentvalue.addElement("content");
				content.setText("焊接速度");
				value = contentvalue.addElement("value");
				value.setText(String.valueOf(weldrate));
				//Format格式输出格式刷
				//FileOutputStream outStream = new FileOutputStream(file);
				OutputFormat format = OutputFormat.createPrettyPrint();
				//设置xml编码
				format.setEncoding("UTF-8");
				//另一个参数表示设置xml的格式
				XMLWriter xw = new XMLWriter(new FileOutputStream(file),format);
				//将组合好的xml封装到已经创建好的document对象中，写出真实存在的xml文件中
				xw.write(doc);
				//清空缓存关闭资源
				xw.flush();
				xw.close();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient("http://10.137.2.50/OtherToMes/MesToWeldServices.asmx?WSDL");
//            http://192.168.0.96:8080/CIWJN_Service/cIWJNWebService?wsdl
//			util.Authority(client);
            Object[] objects = null;
            try {
				objects = client.invoke(new QName("http://tempuri.org/", "UploadWeldFile"), new Object[]{Files,Integer.parseInt(String.valueOf(file.length())),buffer});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if(objects[0].toString().equals("OK")){
            	System.out.println("OK!");
            }else{
            	System.out.println("NO!");
            }
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
	}
	
	/**
	 * 根据工艺规程id获取焊缝、工序、工步信息
	 * @Description
	 * @author Bruce
	 * @date 2020年7月2日上午9:49:15
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getParams")
	public void getParams(HttpServletRequest request,HttpServletResponse response){
		String search = request.getParameter("search");
		String valueFlag = request.getParameter("valueFlag");
		String wpsid = request.getParameter("wpsid");
		String cardId = request.getParameter("cardId");
		String productId = request.getParameter("productId");
		String stepId = request.getParameter("stepId");
		String employeeId = request.getParameter("employeeId");
		JSONObject json = new JSONObject();
		JSONArray junAry = new JSONArray();
		JSONArray empAry = new JSONArray();
		JSONArray stepAry = new JSONArray();
		JSONArray paramAry = new JSONArray();
		JSONObject obj = new JSONObject();
		String search1 = "";
		long stepid=0,junctionid=0;
		try{
			if(valueFlag.equals("0")) {
				String junctionId = request.getParameter("junctionId");
				List<Wps> empList = wpsService.getEmployeStep(search,productId,junctionId);//根据工步查询工序
				for(Wps wps:empList){
					json.put("fid", wps.getFid());
					json.put("fstep", wps.getFstep_number()+"--"+wps.getFstep_name());
					json.put("fstep_name", wps.getFstep_name());
					json.put("fstatus", wps.getF001());
//					json.put("femployee_version", wps.getFemployee_version());
//					json.put("femployee_name", wps.getFemployee_name());
					stepAry.add(json);
				}
				
//				List<Wps> junList = wpsService.getJunction(String.valueOf(stepList.get(0).getFid()));//根据工步查询焊缝
//				for(Wps wps:junList){
//					json.put("fid", wps.getFid());
//					json.put("fjunction", wps.getFjunction()+"--"+wps.getFwelding_area());
////					json.put("fwelding_area", wps.getFwelding_area());
//					junAry.add(json);
//				}
				List<Wps> paramList = wpsService.getDetail(search);
				for(Wps wps:paramList){
					json.put("fid", wps.getFid());
					json.put("fquantitative_project", wps.getFquantitative_project());
					json.put("frequired_value", wps.getFrequired_value());
					json.put("fupper_deviation", wps.getFupper_deviation());
					json.put("flower_deviation", wps.getFlower_deviation());
					json.put("funit_of_measurement", wps.getFunit_of_measurement());
					paramAry.add(json);
				}
			}
			if(valueFlag.equals("1")) {
//				List<Wps> stepList = wpsService.getStep(search);//根据工序查工步
//				for(Wps wps:stepList){
//					json.put("fid", wps.getFid());
//					json.put("fstep", wps.getFstep_number()+"--"+wps.getFstep_name());
//					json.put("fstep_name", wps.getFstep_name());
//					stepAry.add(json);
//				}
				if(search != ""){
					if(search1 == ""){
						search1 += " s.FJUNCTION_ID = '" + search + "'";
					}else{
						search1 += " AND s.FJUNCTION_ID = '" + search + "'";
					}
				}
			
			if(wpsid != ""){
				if(search1 == ""){
					search1 += " E.FWPS_LIB_ID='"+ wpsid + "'";
				}else{
					search1 += " AND E.FWPS_LIB_ID='"+ wpsid + "'";
				}
			}
				List<Wps> junList = wpsService.getJunctionWeld(search);
				for(Wps wps:junList){
					//if(!("1".equals(wps.getF001()))) {
					json.put("fid", wps.getFid());
					//json.put("fjunction", wps.getFjunction()+"--"+wps.getFwelding_area());
					json.put("fwelding_area", wps.getFwelding_area());
					junAry.add(json);
					//}
				}
				List<Wps> empList = wpsService.getEmployee1(search,productId);
				for(Wps wps:empList){
					json.put("fid", wps.getFid());
					json.put("femployee", wps.getFemployee_id()+"--"+wps.getFemployee_name());
					json.put("fstatus", wps.getF001());
//					json.put("femployee_version", wps.getFemployee_version());
//					json.put("femployee_name", wps.getFemployee_name());
					empAry.add(json);
				}
				
				if(search1 == ""){
					search1 += " E.FID='"+ empList.get(0).getFid() + "'";
				}else{
					search1 += " AND E.FID='"+ empList.get(0).getFid() + "'";
				}
				
				List<Wps> stepList = wpsService.getJunctionStep(search1,productId);//根据焊缝查工步
				for(Wps wps:stepList){
					if(!("1".equals(wps.getF001()))) {
						json.put("employid", wps.getFemployee_id());
						json.put("fid", wps.getFid());
						json.put("fstep", wps.getFstep_number()+"--"+wps.getFstep_name());
						json.put("fstep_name", wps.getFstep_name());
						json.put("fstatus", wps.getF001());
						json.fluentPut("productid", wps.getFproduct_number());
						stepAry.add(json);
					}
				}
				List<Wps> paramList = wpsService.getDetail(String.valueOf(stepList.get(0).getFid()));
				for(Wps wps:paramList){
					json.put("fid", wps.getFid());
					json.put("fquantitative_project", wps.getFquantitative_project());
					json.put("frequired_value", wps.getFrequired_value());
					json.put("fupper_deviation", wps.getFupper_deviation());
					json.put("flower_deviation", wps.getFlower_deviation());
					json.put("funit_of_measurement", wps.getFunit_of_measurement());
					paramAry.add(json);
				}
			}
			if(valueFlag.equals("3")) {
				List<Wps> junList = wpsService.getJunctionByStepid(search,productId);
				for(Wps wps2:junList){
					json.put("fid", wps2.getFid());
					if(wps2.getFjunction()!=null &&!("").equals(wps2.getFjunction())) {
						json.put("fjunction", wps2.getFjunction()+"————"+wps2.getFstep_name());
						json.put("fwelding_area", wps2.getFwelding_area());
					}
//					if(wps2.getF001()!=null&&!("").equals(wps2.getF001())) {
						json.put("fstatus", wps2.getF001());
//					}
					junAry.add(json);
//					if(junctionid != wps2.getFid()) {
//						junctionid = wps2.getFid();
//					}
				}
//				List<Wps> empList = wpsService.getEmployee(search);
//				for(Wps wps:empList){
//					json.put("fid", wps.getFid());
//					json.put("femployee", wps.getFemployee_id()+"--"+wps.getFemployee_name());
////					json.put("femployee_version", wps.getFemployee_version());
////					json.put("femployee_name", wps.getFemployee_name());
//					empAry.add(json);
//					
//					List<Wps> stepList = wpsService.getStep(String.valueOf(wps.getFid()));
//					for(Wps wps1:stepList){
//						json.put("fid", wps1.getFid());
//						json.put("fstep", wps1.getFstep_number()+"--"+wps1.getFstep_name());
////						json.put("fstep_name", wps.getFstep_name());
//						if(stepid != wps1.getFid()) {
//							stepid = wps1.getFid();
//							stepAry.add(json);
//						}
//						List<Wps> junList = wpsService.getJunctionByStepid(String.valueOf(wps1.getFid()));
//						for(Wps wps2:junList){
//							json.put("fid", wps2.getFid());
//							json.put("fjunction", wps2.getFjunction());
//							json.put("fwelding_area", wps2.getFwelding_area());
//							if(junctionid != wps2.getFid()) {
//								junctionid = wps2.getFid();
//								junAry.add(json);
//							}
//						}
//					}
//				}
				
//				List<Wps> paramList = wpsService.getDetail(String.valueOf(stepList.get(0).getFid()));
//				for(Wps wps:paramList){
//					json.put("fid", wps.getFid());
//					json.put("fquantitative_project", wps.getFquantitative_project());
//					json.put("frequired_value", wps.getFrequired_value());
//					json.put("fupper_deviation", wps.getFupper_deviation());
//					json.put("flower_deviation", wps.getFlower_deviation());
//					json.put("funit_of_measurement", wps.getFunit_of_measurement());
//					paramAry.add(json);
//				}
			}
//			if(valueFlag.equals("3")) {
//				List<Wps> wpsList = wpsService.getDetail(search);
//				for(Wps wps:wpsList){
//					json.put("fid", wps.getFid());
//					json.put("fquantitative_project", wps.getFquantitative_project());
//					json.put("frequired_value", wps.getFrequired_value());
//					json.put("fupper_deviation", wps.getFupper_deviation());
//					json.put("flower_deviation", wps.getFlower_deviation());
//					json.put("funit_of_measurement", wps.getFunit_of_measurement());
//					paramAry.add(json);
//				}
//			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("junAry", junAry);
		obj.put("empAry", empAry);
		obj.put("stepAry", stepAry);
		obj.put("paramAry", paramAry);
		String respondata = JSON.toJSONString(obj);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}